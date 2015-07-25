package com.chendeji.rongchen.ui.merchant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.util.SystemUtil;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.common.view.CommonFooterView;
import com.chendeji.rongchen.common.view.CommonProgressDialog;
import com.chendeji.rongchen.common.view.FAMLayout;
import com.chendeji.rongchen.common.view.swipy.SwipyRefreshLayout;
import com.chendeji.rongchen.common.view.swipy.SwipyRefreshLayoutDirection;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.model.Platform;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.Html5WebActivity;
import com.chendeji.rongchen.ui.category.DealCategoryActivity;
import com.chendeji.rongchen.ui.city.ChooseCityActivity;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.chendeji.rongchen.ui.deal.DealHistoryActivity;
import com.chendeji.rongchen.ui.merchant.adpter.MerchantRecycleAdapter;
import com.chendeji.rongchen.ui.merchant.task.GetMerchantListTask;

import com.chendeji.rongchen.model.Sort;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户列表界面
 *
 * @author ChenDJ
 * @ClassName:MerchantListActivity
 * @Function:
 * @Reason:
 * @date 2015 -01-02 16:10:58
 * @see
 */
public class MerchantListActivity extends AppCompatActivity implements UITaskCallBack<List<Merchant>>, SwipyRefreshLayout.OnRefreshListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int DEFUALT_PAGENUM = 1;
    public static final String CITY = "city";

    /**
     * 商户列表适配器
     */
//    private MerchantListAdapter merchantListAdapter;

    private SwipyRefreshLayout refreshLayout;

//    private ListView mMerchantListview;

    private CommonFooterView footerView;

    private int mCurrentPage = DEFUALT_PAGENUM;
    private static final int DEFAULT_LIMITE = 20;
    private AsyncTask getMerchantListTask;
    private RecyclerView mMerchantRecycleView;
    private MerchantRecycleAdapter merchantAdapter;
    private String mCity;

    private Sort defualtSort = Sort.DEFAULT;
    private FAMLayout menuLayout;
    private String mCurrentCategory;
    private CategoryDialogBuilder builder;
    private CommonProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.translucentStatusBar(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        //加载控件
        refreshLayout = (SwipyRefreshLayout) findViewById(R.id.sr_reflesh_holder);
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setColorSchemeResources(R.color.blue, R.color.yellow, R.color.light_red, R.color.green);
//        mMerchantListview = (ListView) findViewById(R.id.lv_merchants);
        mMerchantRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        mMerchantRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mMerchantRecycleView.addItemDecoration(new MerchantItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.horizontal_10dp)));

//        actionButton = (FloatingActionButton) findViewById(R.id.main_menu_button);
        menuLayout = (FAMLayout) findViewById(R.id.fam_menu_layout);
        initData();
        initEvent();
        getData();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(SettingFactory.CITY_SETTING)){
            defualtSort = Sort.DEFAULT;
            refreshData();
        }
    }

    public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private int demotextDy = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            //show views if first item is first visible position and views are hidden
            if (firstVisibleItem == 0) {
                if (!controlsVisible) {
                    onShow();
                    controlsVisible = true;
                }
            } else {
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    onHide();
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    onShow();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }
            }
            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }

            demotextDy += dy;

        }

        public abstract void onHide();

        public abstract void onShow();
    }

    class MerchantItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public MerchantItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {

        //刷新数据
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            refreshData();
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            loadMoreData();
        }
    }

    public void stopRefresh() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private class LoadMoreClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //开始加载更多的数据
            loadMoreData();
        }
    }

    /**
     * 从服务端拉数据
     */
    private void getData() {

        //城市和定位混合使用的接口
        getMerchantListTask = new GetMerchantListTask(this, this, defualtSort, mCurrentPage, DEFAULT_LIMITE
                , Offset_Type.GAODE, Offset_Type.GAODE, Platform.HTML5).excuteProxy((Void[]) null);
    }

    /**
     * 初始化数据以及适配器
     */
    private void initData() {
        //初始化筛选数据
        this.mCity = getIntent().getStringExtra(CITY);
        if (TextUtils.isEmpty(mCity)) {
            this.mCity = AppConst.RequestParams.DEFUALT_CITY;
        }

        if (merchantAdapter == null) {
            //适配器管理数据
//            merchantAdapter = new MerchantRecycleAdapter(this, new ArrayList<Merchant>());
            merchantAdapter = new MerchantRecycleAdapter(this, new ArrayList<Merchant>());
        }
        mMerchantRecycleView.setAdapter(merchantAdapter);
    }

    private void hideViews() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) menuLayout.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        ViewPropertyAnimator.animate(menuLayout)
                .translationY(menuLayout.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2))
                .setDuration(200)
                .start();
    }

    private void showViews() {
        ViewPropertyAnimator.animate(menuLayout)
                .translationY(0).setInterpolator(new OvershootInterpolator())
                .setDuration(200)
                .start();
    }

    /**
     * 初始化控件的各种事件监听
     */
    private void initEvent() {
        menuLayout.setChildViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLayout.togleMenu(200);
                Intent intent = null;
                int id = v.getId();
                switch (id) {
                    case R.id.fab_botton_deal_history:
                        //进入到团购浏览历史记录
                        intent = new Intent(MerchantListActivity.this, DealHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.fab_botton_sort:
                        //显示一个选择分类和排序的对话框
                        showSortDialog();
                        break;
                    case R.id.fab_location:
                        //进入到城市选择界面
                        intent = new Intent(MerchantListActivity.this, ChooseCityActivity.class);
                        intent.putExtra(ChooseCityActivity.CHOOSE_MODE, ChooseCityActivity.CHOSE_CITY);
                        startActivity(intent);
                        break;
                    case R.id.fab_user:
                        //进入到用户首页
                        intent = new Intent(MerchantListActivity.this, Html5WebActivity.class);
                        intent.putExtra(Html5WebActivity.URL_KEY, AppConst.AppBaseConst.APP_LOGIN_URL);
                        intent.putExtra(Html5WebActivity.SHOW_MODE, Html5WebActivity.SHOW_USER);
                        startActivity(intent);
                        break;
                }
            }
        });
        refreshLayout.setOnRefreshListener(this);
        mMerchantRecycleView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        SettingFactory.getInstance().registSharePreferencesListener(this);

    }

    @Override
    protected void onResume() {
        mCurrentCategory = SettingFactory.getInstance().getCurrentChoosedCategory();
        if (builder != null) {
            builder.refreshCurrentButton();
        }
        super.onResume();
    }

    private class CategoryDialogBuilder extends SimpleDialog.Builder {
        public CategoryDialogBuilder(int styleId) {
            super(styleId);
        }

        public Button currentCategory;
        public RadioButton button_sortPriceHighFirst;
        public RadioButton button_sortPriceLowFirst;
        public RadioButton button_sortDistanceFirst;
        public RadioButton button_sortStarFirst;
        public RadioButton button_sortDefault;

        @Override
        protected void onBuildDone(Dialog dialog) {
            //初始化控件
            dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            currentCategory = (Button) dialog.findViewById(R.id.bt_current_category);
            refreshCurrentButton();

            button_sortDefault = (RadioButton) dialog.findViewById(R.id.sort_default);
            button_sortStarFirst = (RadioButton) dialog.findViewById(R.id.sort_star_first);
            button_sortDistanceFirst = (RadioButton) dialog.findViewById(R.id.sort_distance_first);
            button_sortPriceLowFirst = (RadioButton) dialog.findViewById(R.id.sort_avg_price_low_to_high);
            button_sortPriceHighFirst = (RadioButton) dialog.findViewById(R.id.sort_avg_price_high_to_low);

            initCheck();

            CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        switch (buttonView.getId()) {
                            case R.id.sort_default:
                                defualtSort = Sort.DEFAULT;
                                break;
                            case R.id.sort_star_first:
                                defualtSort = Sort.STARTS_FIRST;
                                break;
                            case R.id.sort_distance_first:
                                defualtSort = Sort.DISTANCE_FIRST;
                                break;
                            case R.id.sort_avg_price_low_to_high:
                                defualtSort = Sort.AVG_PRICE_LOWER_FIRST;
                                break;
                            case R.id.sort_avg_price_high_to_low:
                                defualtSort = Sort.AVG_PRICE_HIGHER_FIRST;
                                break;
                        }
                        button_sortDefault.setChecked(button_sortDefault == buttonView);
                        button_sortStarFirst.setChecked(button_sortStarFirst == buttonView);
                        button_sortDistanceFirst.setChecked(button_sortDistanceFirst == buttonView);
                        button_sortPriceLowFirst.setChecked(button_sortPriceLowFirst == buttonView);
                        button_sortPriceHighFirst.setChecked(button_sortPriceHighFirst == buttonView);
                    }
                }
            };
            button_sortDefault.setOnCheckedChangeListener(listener);
            button_sortStarFirst.setOnCheckedChangeListener(listener);
            button_sortDistanceFirst.setOnCheckedChangeListener(listener);
            button_sortPriceLowFirst.setOnCheckedChangeListener(listener);
            button_sortPriceHighFirst.setOnCheckedChangeListener(listener);

            currentCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MerchantListActivity.this, DealCategoryActivity.class);
                    intent.putExtra(DealCategoryActivity.LAUCHMODE, DealCategoryActivity.JUST_CHOOSE_CATEGORY);
                    startActivity(intent);
                }
            });

        }

        public void refreshCurrentButton() {
            if (currentCategory == null)
                return;
            if (mCurrentCategory == null) {
                currentCategory.setText(SettingFactory.getInstance().getCurrentChoosedCategory());
            } else {
                currentCategory.setText(mCurrentCategory);
            }
        }

        private void initCheck() {
            RadioButton checkedButton = null;
            if (defualtSort == Sort.DEFAULT) {
                checkedButton = button_sortDefault;
            } else if (defualtSort == Sort.STARTS_FIRST) {
                checkedButton = button_sortStarFirst;
            } else if (defualtSort == Sort.DISTANCE_FIRST) {
                checkedButton = button_sortDistanceFirst;
            } else if (defualtSort == Sort.AVG_PRICE_LOWER_FIRST) {
                checkedButton = button_sortPriceLowFirst;
            } else if (defualtSort == Sort.AVG_PRICE_HIGHER_FIRST) {
                checkedButton = button_sortPriceHighFirst;
            }
            if (checkedButton != null) {
                checkedButton.setChecked(true);
            }
        }

        @Override
        public void onNegativeActionClicked(DialogFragment fragment) {
            super.onNegativeActionClicked(fragment);
        }

        @Override
        public void onPositiveActionClicked(DialogFragment fragment) {
            refreshData();
            super.onPositiveActionClicked(fragment);
        }
    }

    private void showSortDialog() {
        if (builder == null) {
            builder = new CategoryDialogBuilder(R.style.SimpleDialogLight);
            builder.title(getString(R.string.screening))
                    .positiveAction(getString(R.string.positive))
                    .negativeAction(getString(R.string.negative))
                    .contentView(R.layout.dialog_merchant_screening_layout);
        }
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);

    }

    private void showLoadingProgress(){
        if (progressDialog == null) {
            progressDialog = new CommonProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void hideLoadingProgress(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPreExecute() {
//        showFooterViewLoading();
        //显示一个加载进度Dialog
        showLoadingProgress();
    }

    @Override
    public void onPostExecute(List<Merchant> returnMes) {
        stopRefresh();
        //隐藏加载进度Dialog
//        showFooterViewLoaded();
        hideLoadingProgress();
        if (returnMes == null) {
            return;
        }
        if (returnMes.isEmpty()) {
            hideFooterView();
            ToastUtil.showLongToast(this, getString(R.string.no_more_data));
            return;
        }
        if (merchantAdapter != null) {
            if (mCurrentPage > DEFUALT_PAGENUM) {
                merchantAdapter.setData(returnMes, true);
            } else {
                merchantAdapter.setData(returnMes, false);
            }
        }
    }

    @Override
    public void onExecuteError(String errorMsg) {
        hideLoadingProgress();
        ToastUtil.showLongToast(this, errorMsg);
    }

    private void hideFooterView() {
        if (footerView != null) {
            footerView.setVisibility(View.GONE);
        }
    }

    private void showFooterView() {
        if (footerView != null) {
            footerView.setVisibility(View.VISIBLE);
            footerView.showLoadFinished();
        }
    }

    /**
     * 底部加载按钮显示正在加载
     */
    private void showFooterViewLoading() {
        if (footerView != null && merchantAdapter.getCount() != 0) {
            footerView.showLoading();
        }
    }

    /**
     * 底部按钮显示加载完毕
     */
    private void showFooterViewLoaded() {
        if (footerView != null) {
            footerView.showLoadFinished();
        }
    }

    private boolean isFooterViewShow() {
        if (footerView != null) {
            return footerView.isShown();
        }
        return false;
    }

    /**
     * 刷新界面上的数据
     */
    private void refreshData() {
        mCurrentPage = DEFUALT_PAGENUM;
        /*
        在刷新操作的时候需要将底部的控件显示出来
         */
        boolean isShown = isFooterViewShow();
        if (!isShown) {
            //如果没有显示底部的控件，那么就将底部的控件显示出来
            showFooterView();
        }
        getData();
    }

    private void loadMoreData() {
        mCurrentPage++;
        getData();
    }

    /**
     * 商户列表item点击事件
     */
    private class MerchantListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO 进入到商户的详细页面
            Merchant merchant = (Merchant) parent.getItemAtPosition(position);
            Intent intent = new Intent(MerchantListActivity.this, MerchantDetailActivity.class);
            intent.putExtra(MerchantDetailActivity.MERCHANT_ID, merchant.business_id);
            startActivity(intent);
        }
    }

    /**
     * 商户列表item长按事件监听
     */
    private class MerchantListItemLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO 长按显示一个圆环操作menu

            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goback();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goback() {
        boolean isLastActivity = SystemUtil.isLastActivityInTask(this);
        if (isLastActivity) {
            SystemUtil.showExistDialog(this);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (refreshLayout != null) {
            if (refreshLayout.isRefreshing())
                refreshLayout.setRefreshing(false);
            refreshLayout = null;
        }
        if (mMerchantRecycleView != null) {
            mMerchantRecycleView = null;
        }
        if (merchantAdapter != null) {
            merchantAdapter = null;
        }
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (getMerchantListTask != null) {
            if (!getMerchantListTask.isCancelled()) {
                getMerchantListTask.cancel(true);
            }
            getMerchantListTask = null;
        }
        SettingFactory.getInstance().unregistSharePreferencesListener(this);

        super.onDestroy();
    }
}
