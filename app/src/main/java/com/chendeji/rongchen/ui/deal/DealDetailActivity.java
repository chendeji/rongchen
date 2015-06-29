package com.chendeji.rongchen.ui.deal;

import android.graphics.Typeface;
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
import android.widget.TextView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.StatusBarUtil;
import com.chendeji.rongchen.common.view.SpanableTextView;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.model.merchant.SimpleGroupBuyInfo;
import com.chendeji.rongchen.ui.common.ExtendableHolder;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.chendeji.rongchen.ui.deal.task.GetDealDetailInfoTask;
import com.chendeji.rongchen.ui.deal.view.DealInfoLayout;
import com.chendeji.rongchen.ui.deal.view.DealTopImageView;
import com.chendeji.rongchen.ui.deal.view.SimpleMerchantExtendableHolder;

public class DealDetailActivity extends AppCompatActivity {
    public static final String DEAL_KEY = "deal";
    public static final String TAG = DealDetailActivity.class.getSimpleName();
    private SimpleGroupBuyInfo info;

    private DealTopImageView topImageView;
    private DealInfoLayout infoLayout;

    private RelativeLayout bottom_buy_layout;
    private SpanableTextView bottom_current_price;
    private SpanableTextView bottom_list_price;
    private Button bottom_buy;

    private SpanableTextView current_price;
    private SpanableTextView list_price;
    private Button buy;
    private TextView is_refundable; //是否支持退款
    private TextView purchase_count; //销售数量

    private LinearLayout merchant_list;

    private TextView deal_detail;

    private TextView special_tips;
    private AsyncTask getDealDetailTask;
    private Toolbar deal_toolbar;

    @Override
    protected void onDestroy() {
        if (getDealDetailTask != null){
            if (!getDealDetailTask.isCancelled()){
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
        setContentView(R.layout.activity_deal_detail);
        info = (SimpleGroupBuyInfo) getIntent().getExtras().getSerializable(DEAL_KEY);

        //初始化控件
        initComponent();

        //通过服务端拉取数据
        getDealData();
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

        topImageView.setComponentValue(deal);
        infoLayout.setComponentValue(deal);

        //底部视图当前价格
        bottom_current_price.addPiece(new SpanableTextView.Piece.Builder(String.valueOf(deal.current_price))
                .textColor(this.getResources().getColor(R.color.dark_red))
                .textSizeRelative(1.5f)
                .style(Typeface.BOLD)
                .build());
        bottom_current_price.addPiece(new SpanableTextView.Piece.Builder("元")
                .textColor(this.getResources().getColor(R.color.dark_red))
                .textSizeRelative(0.5f)
                .style(Typeface.BOLD)
                .build());
        bottom_current_price.display();

        //底部之前的价格
        bottom_list_price.addPiece(new SpanableTextView.Piece.Builder(String.format("%d元", deal.list_price))
                .textColor(this.getResources().getColor(R.color.gray_text))
                .textSizeRelative(0.9f)
                .strike()
                .build());
        bottom_list_price.display();

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

    private void initComponent() {
        deal_toolbar = (Toolbar) findViewById(R.id.tb_deal_title);
        deal_toolbar.setTitle(getString(R.string.deal_info));

        //顶部的图片
        topImageView = (DealTopImageView) findViewById(R.id.iv_deal_top_image);

        //底部的团购购买按钮
        bottom_buy_layout = (RelativeLayout) findViewById(R.id.rl_bottom_buy);
        bottom_current_price = (SpanableTextView) findViewById(R.id.st_bottom_current_price);
        bottom_list_price = (SpanableTextView) findViewById(R.id.st_bottom_list_price);
        bottom_buy = (Button) findViewById(R.id.bt_bottom_buy);
        bottom_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBuyActivity();
            }
        });

        //顶部的购买视图
        infoLayout = (DealInfoLayout) findViewById(R.id.dil_deal_info);

        //商家信息列表
        merchant_list = (LinearLayout) findViewById(R.id.ll_merchant_list);

        //团购详情
        deal_detail = (TextView) findViewById(R.id.tv_deal_detail);

        //团购须知
        special_tips = (TextView) findViewById(R.id.tv_special_tips);
    }

    private void startBuyActivity() {
        //TODO 跳到支付页面


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
