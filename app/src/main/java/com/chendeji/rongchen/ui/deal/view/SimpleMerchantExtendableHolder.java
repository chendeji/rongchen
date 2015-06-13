package com.chendeji.rongchen.ui.deal.view;

import android.content.Context;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.model.groupbuy.SimpleMerchantInfo;
import com.chendeji.rongchen.ui.common.ExtendableHolder;

/**
 * Created by chendeji on 30/5/15.
 */
public class SimpleMerchantExtendableHolder extends ExtendableHolder {

    private Context mContext;
    private Deal mDeal;
    public SimpleMerchantExtendableHolder(Context context, Deal deal) {
        super(context);
        this.mContext = context;
        this.mDeal = deal;
        this.listener = new OnShowMoreListener() {
            @Override
            public void showMore() {
                //TODO 显示更多的商户列表
                fillData(mDeal);
                hideFootButton();
            }
        };

        fillData(mDeal);
    }

    private void fillData(Deal deal) {
        if (getItemHolder() == null) {
            Logger.i("chendeji","getItemHolder为空了");
            return;
        }

        int showCount = 0;
        int childeCount = getItemHolder().getChildCount();
        int index = childeCount - 1;

        if (index <= 0){
            index = 0;
            if (deal.businesses.size() > 1){
                showCount = 1;
            } else {
                showCount = deal.businesses.size();
            }
        } else {
            showCount = deal.businesses.size();
        }

        SimpleMerchantItem item;
        for (;index < showCount; index++){
            //TODO 添加商户的item
            SimpleMerchantInfo info = deal.businesses.get(index);
            item = new SimpleMerchantItem(mContext, info);
            getItemHolder().addView(item);
        }

    }
}
