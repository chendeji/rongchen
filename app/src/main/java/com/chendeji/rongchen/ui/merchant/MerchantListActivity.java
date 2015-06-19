package com.chendeji.rongchen.ui.merchant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.StatusBarUtil;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.common.view.CommonFooterView;
import com.chendeji.rongchen.common.view.SwipyRefreshLayout;
import com.chendeji.rongchen.common.view.SwipyRefreshLayoutDirection;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.model.Platform;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.chendeji.rongchen.ui.merchant.adpter.MerchantRecycleAdapter;
import com.chendeji.rongchen.ui.merchant.task.GetMerchantListTask;

import com.chendeji.rongchen.model.Sort;
import com.rey.material.widget.FloatingActionButton;

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
public class MerchantListActivity extends AppCompatActivity implements UITaskCallBack<List<Merchant>>, SwipyRefreshLayout.OnRefreshListener {

    private static final int DEFUALT_PAGENUM = 1;

    /**
     * 商户列表适配器
     */
//    private MerchantListAdapter merchantListAdapter;

    private SwipyRefreshLayout refreshLayout;

//    private ListView mMerchantListview;

    private CommonFooterView footerView;

    FloatingActionButton actionButton;

    private int mCurrentPage = DEFUALT_PAGENUM;
    private static final int DEFAULT_LIMITE = 20;
    private AsyncTask getMerchantListTask;
    private RecyclerView mMerchantRecycleView;
    private MerchantRecycleAdapter merchantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.translucentStatusBar(this);
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

        actionButton = (FloatingActionButton) findViewById(R.id.fab_image);
        actionButton.setIcon(getResources().getDrawable(R.drawable.ic_hourglass_empty_white_48dp), false);

        initData();
        initEvent();
        getData();
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
            Logger.i("chendeji", "dy-------------" + demotextDy);

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
        //精确定位接口
//        getMerchantListTask = new GetMerchantListTask(this, this, "福州", "", Sort.DISTANCE_FIRST, mCurrentPage, DEFAULT_LIMITE
//                , Offset_Type.GAODE, Offset_Type.GAODE, Platform.HTML5).excuteProxy((Void[]) null);

        //城市定位接口
        getMerchantListTask = new GetMerchantListTask(this, this, "福州", "", Sort.DEFAULT, mCurrentPage, DEFAULT_LIMITE
                , Offset_Type.DEFUALT, Offset_Type.DEFUALT, Platform.HTML5).excuteProxy((Void[]) null);
    }

    /**
     * 初始化数据以及适配器
     */
    private void initData() {
        if (merchantAdapter == null) {
            //适配器管理数据
//            merchantAdapter = new MerchantRecycleAdapter(this, new ArrayList<Merchant>());
            merchantAdapter = new MerchantRecycleAdapter(this, new ArrayList<Merchant>());
        }
        mMerchantRecycleView.setAdapter(merchantAdapter);
    }

    @SuppressLint("NewApi")
    private void hideViews() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) actionButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        actionButton.animate().translationY(actionButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @SuppressLint("NewApi")
    private void showViews() {
        actionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * 初始化控件的各种事件监听
     */
    private void initEvent() {
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

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //floating action button click!
                //show dialog
                //TODO 弹出半弧形的选项提示动画。

            }
        });
    }

    @Override
    public void onPreExecute() {
        showFooterViewLoading();
    }

    @Override
    public void onPostExecute(List<Merchant> returnMes) {
        stopRefresh();
        showFooterViewLoaded();
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
    public void onNetWorkError() {
        ToastUtil.showLongToast(this, getString(R.string.no_net_work_toast));
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

        if (getMerchantListTask != null) {
            if (!getMerchantListTask.isCancelled()) {
                getMerchantListTask.cancel(true);
            }
            getMerchantListTask = null;
        }

        super.onDestroy();
    }
}
