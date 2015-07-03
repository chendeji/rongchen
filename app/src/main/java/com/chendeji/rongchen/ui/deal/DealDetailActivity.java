package com.chendeji.rongchen.ui.deal;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chendeji.rongchen.MyApplication;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ImageLoaderOptionsUtil;
import com.chendeji.rongchen.common.util.StatusBarUtil;
import com.chendeji.rongchen.common.view.SpanableTextView;
import com.chendeji.rongchen.common.view.scrollview.MyScrollView;
import com.chendeji.rongchen.common.view.scrollview.ObservableScrollViewCallbacks;
import com.chendeji.rongchen.common.view.scrollview.ScrollState;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.model.merchant.SimpleGroupBuyInfo;
import com.chendeji.rongchen.ui.Html5WebActivity;
import com.chendeji.rongchen.ui.common.ExtendableHolder;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.chendeji.rongchen.ui.deal.task.GetDealDetailInfoTask;
import com.chendeji.rongchen.ui.deal.view.DealInfoLayout;
import com.chendeji.rongchen.ui.deal.view.SimpleMerchantExtendableHolder;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rey.material.widget.FloatingActionButton;

public class DealDetailActivity extends AppCompatActivity {
    public static final String DEAL_KEY = "deal";
    public static final String TAG = DealDetailActivity.class.getSimpleName();
    private SimpleGroupBuyInfo info;

    //    private DealTopImageView topImageView;
    private DealInfoLayout infoLayout;

    private RelativeLayout bottom_buy_layout;
//    private SpanableTextView bottom_current_price;
//    private SpanableTextView bottom_list_price;
//    private Button bottom_buy;

    private SpanableTextView current_price;
    private SpanableTextView list_price;
    private Button buy;
    private TextView is_refundable; //是否支持退款
    private TextView purchase_count; //销售数量

    private LinearLayout merchant_list;

    private TextView deal_detail;

    private TextView special_tips;
    private AsyncTask getDealDetailTask;
    private ImageView mTop_image;
    private MyScrollView scrollView;
    private View mOverlay;
    private TextView mToolbar_title;
    private FloatingActionButton mBuy_immediately;
    private int mFlexibleSpaceImageHeight;
    private int mFlexibleSpaceShowFabOffset;
    private int mActionBarSize;
    private boolean fabIsShown;
    private Deal mDeal;

    @Override
    protected void onDestroy() {
        if (getDealDetailTask != null) {
            if (!getDealDetailTask.isCancelled()) {
                getDealDetailTask.cancel(true);
            }
            getDealDetailTask = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.translucentStatusBar(this);
        setContentView(R.layout.activity_material_deal_detail);
        info = (SimpleGroupBuyInfo) getIntent().getExtras().getSerializable(DEAL_KEY);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = StatusBarUtil.getActionBarSize(this);

        //初始化控件
        initComponent();
        initEvent();

        //通过服务端拉取数据
        getDealData();
    }

    private void initComponent() {
        //----------------------------version 1.0.3
        mTop_image = (ImageView) findViewById(R.id.iv_top_image);
        scrollView = (MyScrollView) findViewById(R.id.scroll);
        mOverlay = findViewById(R.id.overlay);
        mToolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        mBuy_immediately = (FloatingActionButton) findViewById(R.id.fab_buy_immediaterly);
        mBuy_immediately.setIcon(getResources().getDrawable(R.drawable.ic_local_grocery_store_white_48dp), false);
        TextView merchantListTitle = (TextView) findViewById(R.id.tv_merchant_list_title);
        merchantListTitle.setText(getString(R.string.merchant_list_activity_title));

        //----------------------------version 1.0.0
        //顶部的购买视图
        infoLayout = (DealInfoLayout) findViewById(R.id.dil_deal_info);
        //商家信息列表
        merchant_list = (LinearLayout) findViewById(R.id.ll_merchant_list);
        //团购详情
        deal_detail = (TextView) findViewById(R.id.tv_deal_detail);
        //团购须知
        special_tips = (TextView) findViewById(R.id.tv_special_tips);
    }

    private void initEvent() {
        mBuy_immediately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBuyActivity();
            }
        });

        scrollView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                int flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
                int minOverlayTransitionY = mActionBarSize - mOverlay.getHeight();

                ViewHelper.setTranslationY(mTop_image, Math.min(0, Math.max(-scrollY / 2, minOverlayTransitionY)));
                ViewHelper.setTranslationY(mOverlay, Math.min(0, Math.max(-scrollY, minOverlayTransitionY)));
                ViewHelper.setAlpha(mOverlay,1 - Math.max(0 ,(float)(flexibleRange - scrollY) / flexibleRange));

                float titleAlpha = 1 - Math.min(1, Math.max(0, (float)(flexibleRange - scrollY) / flexibleRange));
                ViewHelper.setAlpha(mToolbar_title, titleAlpha);

                //计算出titleview最多能在Y轴上移动多少
                int maxTitleTranY = (int) (mFlexibleSpaceImageHeight - mToolbar_title.getHeight());
                int titleTranY = Math.max(0, maxTitleTranY - scrollY);
                ViewHelper.setTranslationY(mToolbar_title, titleTranY);

                int maxFABTranY = mFlexibleSpaceImageHeight - mBuy_immediately.getHeight() / 2;
                int FABTranY = Math.min(maxFABTranY, Math.max(mActionBarSize - mBuy_immediately.getHeight() / 2, maxFABTranY - scrollY));
                ViewHelper.setTranslationY(mBuy_immediately, FABTranY);

                if (FABTranY < mFlexibleSpaceShowFabOffset) {
                    //hideFAB
                    hideFAB();
                } else {
                    //showFAB
                    showFAB();
                }
            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

            }
        });

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewHelper.setAlpha(mOverlay, 0);
                ViewHelper.setAlpha(mToolbar_title, 0);
                ViewHelper.setTranslationY(mToolbar_title, mFlexibleSpaceImageHeight - mToolbar_title.getHeight());
                ViewHelper.setTranslationY(mBuy_immediately, mFlexibleSpaceImageHeight - mBuy_immediately.getHeight() / 2);
                showFAB();
                //刚刚进入到页面的时候设置完了，要记得将这个监听注销掉
                scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void showFAB() {
        if (!fabIsShown) {
            ViewPropertyAnimator.animate(mBuy_immediately).cancel();
            ViewPropertyAnimator.animate(mBuy_immediately).scaleX(1).scaleY(1).setDuration(200).start();
            fabIsShown = true;
        }
    }

    private void hideFAB() {
        if (fabIsShown) {
            ViewPropertyAnimator.animate(mBuy_immediately).cancel();
            ViewPropertyAnimator.animate(mBuy_immediately).scaleX(0).scaleY(0).setDuration(200).start();
            fabIsShown = false;
        }
    }

    private void getDealData() {
        // 编写接口
        getDealDetailTask = new GetDealDetailInfoTask(this, new UITaskCallBack<ReturnMes<Deal>>() {

            @Override
            public void onPreExecute() {
                //TODO 事先加载数据库的缓存数据

            }

            @Override
            public void onPostExecute(ReturnMes<Deal> returnMes) {
                if (returnMes == null) {
                    showErrorImage();
                    return;
                }
                Deal deal = returnMes.object;
                if (deal == null)
                    return;
                setComponentValue(deal);
            }

            @Override
            public void onNetWorkError() {

            }
        }, info.id).excuteProxy((Void[]) null);
    }

    private void setComponentValue(Deal deal) {
        this.mDeal = deal;
        ImageLoader.getInstance().displayImage(deal.image_url, mTop_image, ImageLoaderOptionsUtil.topImageOptions, MyApplication.imageLoadingListener);
        infoLayout.setComponentValue(deal);
        mToolbar_title.setText(deal.title);
        //商家列表
        ExtendableHolder holder = null;
        holder = new SimpleMerchantExtendableHolder(this, deal);
        holder.setFooterButtonText(getResources().getString(R.string.show_more_merchant));
        merchant_list.addView(holder);

        //团购详情
        deal_detail.setText(deal.details);

        //团购须知
        special_tips.setText(deal.getRestrictions().special_tips);
    }

    private void showErrorImage() {
        //TODO 显示整个页面的加载错误

    }

    private void startBuyActivity() {
        //TODO 跳到支付页面
        if (mDeal == null){
            return ;
        }
        Intent intent = new Intent(this, Html5WebActivity.class);
        intent.putExtra(Html5WebActivity.URL_KEY, mDeal.deal_h5_url);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_deal_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
