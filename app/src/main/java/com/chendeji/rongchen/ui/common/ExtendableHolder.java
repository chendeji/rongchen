package com.chendeji.rongchen.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.model.merchant.Merchant;

/**
 * Created by chendeji on 16/5/15.
 */
public class ExtendableHolder extends LinearLayout {

    protected final Context mContext;
    private Button bt_show_more;
    protected LinearLayout dealsContainer;    //列表holder


    protected OnShowMoreListener listener;
    protected interface OnShowMoreListener{
        void showMore();
    };

    public ExtendableHolder(Context context) {
        super(context);
        this.mContext = context;
        init();
    }



    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.extendable_view, this, true);
        dealsContainer = (LinearLayout) view.findViewById(R.id.deal_holder);
        bt_show_more = (Button) view.findViewById(R.id.bt_show_more);
        bt_show_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.showMore();
                }
//                hideFootButton();
            }
        });
        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    //设置底部控件的文本信息
    public void setFooterButtonText(String text){
        bt_show_more.setText(text);
    }

    protected void hideFootButton(){
        if (bt_show_more != null){
            bt_show_more.setVisibility(View.GONE);
        }
    }

    protected ViewGroup getItemHolder(){
        return dealsContainer;
    }
}
