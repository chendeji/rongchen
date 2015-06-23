package com.chendeji.rongchen.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.chendeji.rongchen.R;
import com.rey.material.widget.ProgressView;

/**
 * Created by chendeji on 23/6/15.
 */
public class CommonLoadingProgressView extends RelativeLayout {

    private ProgressView progressView;

    public CommonLoadingProgressView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CommonLoadingProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CommonLoadingProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        View view = LayoutInflater.from(context).inflate(R.layout.common_loading_layout, this, true);
        progressView = (ProgressView) view.findViewById(R.id.common_loading_progress);

    }


    public void setVisible(int visible){
        switch (visible){
            case View.VISIBLE:
                if (progressView != null){
                    progressView.setVisibility(visible);
                    progressView.start();
                }
                break;
            case View.INVISIBLE:
            case View.GONE:
                if (progressView != null){
                    progressView.setVisibility(visible);
                    progressView.stop();
                }
                break;
        }
        this.setVisibility(visible);
    }


}
