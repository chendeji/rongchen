package com.chendeji.rongchen.ui.merchant.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.chendeji.rongchen.model.merchant.SimpleGroupBuyInfo;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.common.ExtendableHolder;
import com.chendeji.rongchen.ui.deal.DealDetailActivity;

/**
 *
 * Created by chendeji on 16/5/15.
 */
public class DealExtendableHolder extends ExtendableHolder {

    private final Merchant merchant;

    public DealExtendableHolder(Context context, Merchant merchant) {
        super(context);
        this.merchant = merchant;
        listener = new OnShowMoreListener(){
            @Override
            public void showMore() {
                ViewGroup group = getItemHolder();
                if (group == null)
                    return;
                showMoreDeal(group);
                //点击加载更多之后隐藏按钮
                hideFootButton();
            }
        };
        if (getItemHolder() == null)
            return;
        fillDate(merchant, getItemHolder());
    }

    private void showMoreDeal(ViewGroup listHolder) {
        fillDate(merchant, listHolder);
    }

    private void fillDate(Merchant merchant, ViewGroup listHolder){
        //遍历该商户的所有团购，并列表出来
        DealItemView itemView = null;
        //两种显示模式，如果数量操作三个，就显示底部的加载更多按钮，如果没有超过三个，直接全部显示，并隐藏底部的按钮
        int showCount = 0;
        int i = listHolder.getChildCount() - 1;
        if (i <= 0){
            i = 0;
            if (merchant.deal_count > 3){
                showCount = 3;
            } else {
                showCount = merchant.deal_count;
            }
        } else {
            showCount = merchant.deal_count;
        }

        for(; i < showCount; i++){
            final SimpleGroupBuyInfo info = merchant.deals.get(i);
            itemView = new DealItemView(mContext);
            itemView.setComponentValue(info);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DealDetailActivity.class);
                    intent.putExtra(DealDetailActivity.DEAL_KEY, info);
                    mContext.startActivity(intent);
                }
            });
            listHolder.addView(itemView);
        }
    }
}
