package com.chendeji.rongchen.ui.merchant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.StatusBarUtil;
import com.chendeji.rongchen.common.util.ToastUtil;
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

import java.util.List;

/**
 * 商户详情界面
 * @ClassName:MerchantDetailActivity
 * @Function:
 * @Reason:
 * @author ChenDJ
 * @date 2015 -01-02 22:24:41
 * @see
 */
public class MerchantDetailActivity extends AppCompatActivity {

    /**
     * key
     */
    public static final String MERCHANT_ID = "merchant_id";

    private Merchant merchant;

    private MerchantTopImageView imageView;
    private TextView address;
    private Button call_merchant;
    private TextView groupPurchaseCount;
    private LinearLayout ll_deal_list;
    private LinearLayout ll_comment_list;

    private AsyncTask getCommentListTask;
    private AsyncTask getMerchantTask;
    private ScrollView scrollView;
    private Toolbar merchantTitleToolBar;
    private RelativeLayout marchant_contact_info;

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
        if (merchant != null){
            merchant = null;
        }

        if (getCommentListTask != null){
            if (!getCommentListTask.isCancelled()){
                getCommentListTask.cancel(true);
            }
            getCommentListTask = null;
        }

        if (getMerchantTask != null){
            if (!getMerchantTask.isCancelled()){
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
        setContentView(R.layout.activity_merchant_detail);
        getIntentData();
        //1，头部图像视图 （完成）
        //2，商户地址，右边有一个电话按钮（完成）
        //3, 团购列表，超过3个，显示底部最后一栏，点击最后一栏展开剩余的所有团购（未完成）
        //4，评价列表显示评论的第一个，底部显示一个评论总数（未完成）
        initComponent();
        initEvent();
    }

    private void initEvent() {
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
                Logger.i("chendeji","latitude:" + merchant.latitude + "longitude:"+merchant.longitude);
                intent.putExtra(MapActivity.LOCATION_KEY, merchant);
                MerchantDetailActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * 填充数据
     * @函数名称  :setData
     * @brief
     * @see
     * @param merchant
     * @author  : chendeji
     * @date  : Wed Apr 15 22:08:13 CST 2015
     */
    private void setData(Merchant merchant) {
        this.merchant = merchant;
//        merchantTitleToolBar.setTitle(merchant.name);
//        merchantTitleToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        imageView.setComponentValue(merchant);
        address.setText(merchant.address);
        groupPurchaseCount.setText(String.format("团购数量(%d)", merchant.deal_count));

        //填充团购数据
        if (merchant.deal_count != 0){
            ExtendableHolder dealHolder;
            dealHolder = new DealExtendableHolder(this, merchant);
            dealHolder.setFooterButtonText(getString(R.string.showmore));
            ll_deal_list.addView(dealHolder);
        } else {
            ll_deal_list.setVisibility(View.GONE);
        }

        //填充商户评论数，这里需要重新去访问评论接口
        getCommentListTask = new GetCommentListTask(this,merchant.business_id,new UITaskCallBack<ReturnMes<List<Comment>>>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(ReturnMes<List<Comment>> returnMes) {
                if (returnMes == null)
                    return;
                List<Comment> comments = returnMes.object;
                if (comments.size() > 0){
                    ExtendableHolder commentHolder;
                    commentHolder = new CommentExtendableHolder(MerchantDetailActivity.this, returnMes);
                    commentHolder.setFooterButtonText(getString(R.string.showmore_comments));
                    ll_comment_list.addView(commentHolder);
                } else {
                    ll_comment_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNetWorkError() {
                ToastUtil.showLongToast(MerchantDetailActivity.this, getString(R.string.no_net_work_toast));
            }
        }).excuteProxy((Void[]) null);

    }

    /**
     * 初始化控件值，控件需要用到的后台信息，放在控件加载完毕之后
     * @函数名称  :initComponent
     * @brief
     * @see
     * @author  : ChenDJ
     * @date  : 2015-01-10 15:45:03
     */
    private void initComponent() {
        merchantTitleToolBar = (Toolbar) findViewById(R.id.tb_merchant_detail);
        setSupportActionBar(merchantTitleToolBar);
        merchantTitleToolBar.setTitle(getString(R.string.merchant_info));

        scrollView = (ScrollView) findViewById(R.id.sv_detail_container);

        marchant_contact_info = (RelativeLayout)findViewById(R.id.rl_merchant_contact_info);
        imageView = (MerchantTopImageView) findViewById(R.id.ctiv_commontopimageview);
        address = (TextView) findViewById(R.id.tv_address);
        call_merchant = (Button) findViewById(R.id.bt_call_merchant);
        groupPurchaseCount = (TextView) findViewById(R.id.tv_group_purchase_count);
        ll_deal_list = (LinearLayout) findViewById(R.id.ll_deal_list);
        ll_comment_list = (LinearLayout) findViewById(R.id.ll_comment_list);
    }

    /**
     * 解析intent传回来的数据
     * @函数名称  :getIntentData
     * @brief
     * @see
     * @author  : ChenDJ
     * @date  : 2015-01-02 21:19:06
     */
    private void getIntentData() {
        long merchant_id = getIntent().getLongExtra(MERCHANT_ID, -1);
        getMerchantTask = new GetMerchantDetailInfoTask(this,new UITaskCallBack<ReturnMes<Merchant>>() {
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
        }, merchant_id).excuteProxy((Void[])null);
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
