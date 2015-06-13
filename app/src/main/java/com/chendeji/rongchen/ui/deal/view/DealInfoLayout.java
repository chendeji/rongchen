package com.chendeji.rongchen.ui.deal.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.view.SpanableTextView;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.model.groupbuy.Restriction;

/**
 * Created by chendeji on 30/5/15.
 */
public class DealInfoLayout extends RelativeLayout {

    private final Context mContext;
    private SpanableTextView current_price;
    private SpanableTextView list_price;
    private Button buy;
    private TextView is_refundable;
    private TextView purchase_count;

    public DealInfoLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public DealInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View viewContainer = LayoutInflater.from(mContext).inflate(R.layout.deal_info_layout,this,true);
        current_price = (SpanableTextView) viewContainer.findViewById(R.id.st_current_price);
        list_price = (SpanableTextView) viewContainer.findViewById(R.id.st_list_price);
        buy = (Button) viewContainer.findViewById(R.id.bt_buy);
        buy.setText(mContext.getString(R.string.buy_imediatly));
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBuyActivity();
            }
        });
        is_refundable = (TextView) viewContainer.findViewById(R.id.tv_is_refundable);
        purchase_count = (TextView) viewContainer.findViewById(R.id.tv_purchase_count);
    }

    public void setComponentValue(Deal deal){
        //顶部之前的价格
        current_price.addPiece(new SpanableTextView.Piece.Builder(String.valueOf(deal.current_price))
                .textColor(this.getResources().getColor(R.color.dark_red))
                .textSizeRelative(2.0f)
                .style(Typeface.BOLD)
                .build());
        current_price.addPiece(new SpanableTextView.Piece.Builder("元")
                .textColor(this.getResources().getColor(R.color.dark_red))
                .textSizeRelative(0.9f)
                .style(Typeface.BOLD)
                .build());
        current_price.display();

        //底部之前的价格
        list_price.addPiece(new SpanableTextView.Piece.Builder(String.format("%d元", deal.list_price))
                .textColor(this.getResources().getColor(R.color.gray_text))
                .textSizeRelative(0.9f)
                .strike()
                .build());
        list_price.display();
        purchase_count.setText(String.format("已售%d",deal.purchase_count));
        switch (deal.restrictions.is_refundable){
            case Restriction.REFUNDABLE:
                is_refundable.setVisibility(View.VISIBLE);
                break;
            case Restriction.CANT_REFUNDABLE:
                is_refundable.setVisibility(View.GONE);
                break;

        }
    }

    private void startBuyActivity() {
        //TODO 跳到支付页面

    }

}
