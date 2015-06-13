package com.chendeji.rongchen.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chendeji.rongchen.R;

/**
 * Created by chendeji on 4/2/15.
 */
public class CommonFooterView extends RelativeLayout {
    private TextView textView;
    private ProgressBar progressBar;

    public void showLoading(){
        this.progressBar.setVisibility(View.VISIBLE);
        this.textView.setText(R.string.loading);
    }

    public void showLoadFinished(){
        this.progressBar.setVisibility(View.GONE);
        this.textView.setText(R.string.loaded);
    }

    public CommonFooterView(Context context) {
        super(context);
        init(context, null);
    }

    public CommonFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_footer_layout, this, true);
        textView = (TextView) view.findViewById(R.id.tv_footer_text);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_footer_progress_bar);

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setBackgroundResource(android.R.color.transparent);
    }

//    public void setVisitable(){
//        boolean isShow = isShown();
//        if (isShow){
//            this.setVisibility(View.INVISIBLE);
//        }else {
//            this.setVisibility(View.VISIBLE);
//        }
//    }
}
