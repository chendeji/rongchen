package com.chendeji.rongchen.ui.merchant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.chendeji.rongchen.MyApplication;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ImageLoaderOptionsUtil;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.StatusBarUtil;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.common.view.MaterialTopImageView;
import com.chendeji.rongchen.common.view.scrollview.MyScrollView;
import com.chendeji.rongchen.common.view.scrollview.ObservableScrollViewCallbacks;
import com.chendeji.rongchen.common.view.scrollview.ScrollState;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.comment.Comment;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.chendeji.rongchen.ui.map.MapActivity;
import com.chendeji.rongchen.ui.merchant.task.GetCommentListTask;
import com.chendeji.rongchen.ui.merchant.task.GetMerchantDetailInfoTask;
import com.chendeji.rongchen.ui.merchant.view.CommentExtendableHolder;
import com.chendeji.rongchen.ui.merchant.view.MerchantTopImageView;
import com.chendeji.rongchen.ui.common.ExtendableHolder;
import com.chendeji.rongchen.ui.merchant.view.DealExtendableHolder;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rey.material.widget.FloatingActionButton;

import java.util.List;

/**
 * 商户详情界面
 *
 * @author ChenDJ
 * @ClassName:MerchantDetailActivity
 * @Function:
 * @Reason:
 * @date 2015 -01-02 22:24:41
 * @see
 */
public class MerchantDetailActivity extends AppCompatActivity {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    /**
     * key
     */
    public static final String MERCHANT_ID = "merchant_id";

    private Merchant merchant;

    private ImageView imageView;
    private TextView address;
    private Button call_merchant;
    private TextView groupPurchaseCount;
    private LinearLayout ll_deal_list;
    private LinearLayout ll_comment_list;

    private AsyncTask getCommentListTask;
    private AsyncTask getMerchantTask;
    //    private ScrollView scrollView;
    private Toolbar merchantTitleToolBar;
    private RelativeLayout marchant_contact_info;
    private View deal_list_hoder;
    private View comment_list_hoder;
    private int mFlexibleSpaceImageHeight;
    private int mFlexibleSpaceShowFabOffset;
    private int mActionBarSize;
    private MyScrollView myScrollView;
    private TextView titleView;
    private FloatingActionButton phone_button;
    private View mOverlayView;
    private boolean fabIsShown;

    @Override
    protected void onPause() {
        if (!getCommentListTask.isCancelled())
            getCommentListTask.cancel(true);
        if (!getMerchantTask.isCancelled())
            getMerchantTask.cancel(true);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        fabIsShown = false;
        if (merchant != null) {
            merchant = null;
        }

        if (getCommentListTask != null) {
            if (!getCommentListTask.isCancelled()) {
                getCommentListTask.cancel(true);
            }
            getCommentListTask = null;
        }

        if (getMerchantTask != null) {
            if (!getMerchantTask.isCancelled()) {
                getMerchantTask.cancel(true);
            }
            getMerchantTask = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.translucentStatusBar(this);
        setContentView(R.layout.activity_material_merchant_detail);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = StatusBarUtil.getActionBarSize(this);

        //1，头部图像视图 （完成）
        //2，商户地址，右边有一个电话按钮（完成）
        //3, 团购列表，超过3个，显示底部最后一栏，点击最后一栏展开剩余的所有团购（未完成）
        //4，评价列表显示评论的第一个，底部显示一个评论总数（未完成）
        initComponent();
        initEvent();
        getIntentData();
    }

    /**
     * 初始化控件值，控件需要用到的后台信息，放在控件加载完毕之后
     *
     * @函数名称 :initComponent
     * @brief
     * @author : ChenDJ
     * @date : 2015-01-10 15:45:03
     * @see
     */
    private void initComponent() {
//        merchantTitleToolBar = (Toolbar) findViewById(R.id.tb_merchant_detail);
//        setSupportActionBar(merchantTitleToolBar);
//        merchantTitleToolBar.setTitle(getString(R.string.merchant_info));

//        scrollView = (ScrollView) findViewById(R.id.sv_detail_container);

        //version 1.0.3
        //加载控件
        imageView = (ImageView) findViewById(R.id.iv_top_image);
        mOverlayView = findViewById(R.id.overlay);
        myScrollView = (MyScrollView) findViewById(R.id.scroll);
        titleView = (TextView) findViewById(R.id.tv_toolbar_title);
        phone_button = (FloatingActionButton) findViewById(R.id.fab_button_phone);
        phone_button.setIcon(getResources().getDrawable(R.drawable.ic_local_phone_white_48dp), false);


        //version 1.0.0
        marchant_contact_info = (RelativeLayout) findViewById(R.id.rl_merchant_contact_info);
        address = (TextView) findViewById(R.id.tv_address);
        call_merchant = (Button) findViewById(R.id.bt_call_merchant);
        groupPurchaseCount = (TextView) findViewById(R.id.tv_group_purchase_count);
        ll_deal_list = (LinearLayout) findViewById(R.id.ll_deal_list);
        deal_list_hoder = findViewById(R.id.cv_deal_list_hoder);
        ll_comment_list = (LinearLayout) findViewById(R.id.ll_comment_list);
        comment_list_hoder = findViewById(R.id.cv_comment_list_hoder);
        TextView comment_title = (TextView) findViewById(R.id.tv_comment_title);
        comment_title.setText(getString(R.string.comment_title));
    }

    private void initEvent() {
        phone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转到拨号界面，拨打电话

            }
        });
        myScrollView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                int flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
                int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();

                ViewHelper.setTranslationY(imageView, Math.min(0, Math.max(-scrollY / 2, minOverlayTransitionY)));

                ViewHelper.setTranslationY(mOverlayView, Math.min(0, Math.max(-scrollY, minOverlayTransitionY)));
                ViewHelper.setAlpha(mOverlayView,1 - Math.max(0 ,(float)(flexibleRange - scrollY) / flexibleRange));

                float titleAlpha = 1 - Math.min(1, Math.max(0, (float)(flexibleRange - scrollY) / flexibleRange));
                ViewHelper.setAlpha(titleView, titleAlpha);

                //计算出titleview最多能在Y轴上移动多少
                int maxTitleTranY = (int) (mFlexibleSpaceImageHeight - titleView.getHeight());
                int titleTranY = Math.max(0, maxTitleTranY - scrollY);
                ViewHelper.setTranslationY(titleView, titleTranY);

                //计算FAB的位置还有可移动范围
                int maxFABTranY = mFlexibleSpaceImageHeight - phone_button.getHeight() / 2;
                int FABTranY = Math.min(maxFABTranY, Math.max(mActionBarSize - phone_button.getHeight() / 2, maxFABTranY - scrollY));
                ViewHelper.setTranslationY(phone_button, FABTranY);

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

        myScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                myScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);
                ViewHelper.setAlpha(mOverlayView, 0);
                ViewHelper.setTranslationY(titleView, mFlexibleSpaceImageHeight - titleView.getHeight());
                ViewHelper.setTranslationY(phone_button, mFlexibleSpaceImageHeight - phone_button.getHeight() / 2);
                showFAB();
            }
        });

        marchant_contact_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启地图导航
//                try {
//                    NaviPara para = new NaviPara();
//                    para.setTargetPoint(new LatLng(merchant.latitude, merchant.longitude));
//                    AMapUtils.openAMapNavi(para, MerchantDetailActivity.this);
//                } catch (AMapException e) {
//                    ToastUtil.showLongToast(MerchantDetailActivity.this, e.getErrorMessage());
//                }
                Intent intent = new Intent(MerchantDetailActivity.this, MapActivity.class);
                Logger.i("chendeji", "latitude:" + merchant.latitude + "longitude:" + merchant.longitude);
                intent.putExtra(MapActivity.LOCATION_KEY, merchant);
                MerchantDetailActivity.this.startActivity(intent);
            }
        });
    }

    private void showFAB() {
        if (!fabIsShown) {
            ViewPropertyAnimator.animate(phone_button).cancel();
            ViewPropertyAnimator.animate(phone_button).scaleX(1).scaleY(1).setDuration(200).start();
            fabIsShown = true;
        }
    }

    private void hideFAB() {
        if (fabIsShown) {
            ViewPropertyAnimator.animate(phone_button).cancel();
            ViewPropertyAnimator.animate(phone_button).scaleX(0).scaleY(0).setDuration(200).start();
            fabIsShown = false;
        }
    }

    /**
     * 填充数据
     *
     * @param merchant
     * @函数名称 :setData
     * @brief
     * @author : chendeji
     * @date : Wed Apr 15 22:08:13 CST 2015
     * @see
     */
    private void setData(Merchant merchant) {
        this.merchant = merchant;
//        merchantTitleToolBar.setTitle(merchant.name);
//        merchantTitleToolBar.setTitleTextColor(getResources().getColor(R.color.white));
//        imageView.setComponentValue(merchant);
        ImageLoader.getInstance().displayImage(merchant.photo_url, imageView,
                ImageLoaderOptionsUtil.topImageOptions, MyApplication.imageLoadingListener);
        titleView.setText(merchant.name);
        address.setText(merchant.address);
        groupPurchaseCount.setText(String.format("团购数量(%d)", merchant.deal_count));

        //填充团购数据
        if (merchant.deal_count != 0) {
            ExtendableHolder dealHolder;
            dealHolder = new DealExtendableHolder(this, merchant);
            dealHolder.setFooterButtonText(getString(R.string.showmore));
            ll_deal_list.addView(dealHolder);
        } else {
            deal_list_hoder.setVisibility(View.GONE);
        }

        //填充商户评论数，这里需要重新去访问评论接口
        getCommentListTask = new GetCommentListTask(this, merchant.business_id, new UITaskCallBack<ReturnMes<List<Comment>>>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(ReturnMes<List<Comment>> returnMes) {
                if (returnMes == null)
                    return;
                List<Comment> comments = returnMes.object;
                if (comments.size() > 0) {
                    ExtendableHolder commentHolder;
                    commentHolder = new CommentExtendableHolder(MerchantDetailActivity.this, returnMes);
                    commentHolder.setFooterButtonText(getString(R.string.showmore_comments));
                    ll_comment_list.addView(commentHolder);
                } else {
                    comment_list_hoder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNetWorkError() {
                ToastUtil.showLongToast(MerchantDetailActivity.this, getString(R.string.no_net_work_toast));
            }
        }).excuteProxy((Void[]) null);

    }


    /**
     * 解析intent传回来的数据
     *
     * @函数名称 :getIntentData
     * @brief
     * @author : ChenDJ
     * @date : 2015-01-02 21:19:06
     * @see
     */
    private void getIntentData() {
        long merchant_id = getIntent().getLongExtra(MERCHANT_ID, -1);
        getMerchantTask = new GetMerchantDetailInfoTask(this, new UITaskCallBack<ReturnMes<Merchant>>() {
            @Override
            public void onPreExecute() {
                //TODO 显示正在加载信息
            }

            @Override
            public void onPostExecute(ReturnMes<Merchant> returnMes) {
                if (returnMes == null)
                    return;
                Merchant merchant = returnMes.object;
                if (merchant == null)
                    return;
                setData(merchant);
            }

            @Override
            public void onNetWorkError() {

            }
        }, merchant_id).excuteProxy((Void[]) null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_merchant_detail, menu);
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
