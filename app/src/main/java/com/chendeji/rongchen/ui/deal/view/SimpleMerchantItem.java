package com.chendeji.rongchen.ui.deal.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.model.groupbuy.SimpleMerchantInfo;
import com.chendeji.rongchen.ui.merchant.MerchantDetailActivity;

/**
 * Created by chendeji on 30/5/15.
 */
public class SimpleMerchantItem extends RelativeLayout {
    private final Context mContext;
    private final SimpleMerchantInfo mInfo;
    private TextView merchant_title;
    private TextView merchant_address;

    public SimpleMerchantItem(Context context, SimpleMerchantInfo info) {
        super(context);
        this.mContext = context;
        this.mInfo = info;
        init();
    }

    private void init() {
        View viewContainer = LayoutInflater.from(mContext).inflate(R.layout.merchant_item_view, this, true);
        merchant_title = (TextView) viewContainer.findViewById(R.id.tv_merchant_title);
        merchant_address = (TextView) viewContainer.findViewById(R.id.tv_merchant_address);
        int padding = mContext.getResources().getDimensionPixelSize(R.dimen.horizontal_10dp);
        setPadding(padding,padding,padding,padding);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MerchantDetailActivity.class);
                intent.putExtra(MerchantDetailActivity.MERCHANT_ID, mInfo.id);
                mContext.startActivity(intent);
            }
        });
        setComponentValue(mInfo);
    }

    public void setComponentValue(SimpleMerchantInfo info){
        merchant_title.setText(info.name);
        merchant_address.setText(info.address);
    }


}
