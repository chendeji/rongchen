package com.chendeji.rongchen.common.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chendeji.rongchen.R;
import com.rey.material.widget.ProgressView;

/**
 * Created by chendeji on 21/6/15.
 */
public class CommonProgressDialog extends Dialog implements DialogInterface.OnDismissListener {

    private ProgressView progressView;

    public CommonProgressDialog(Context context) {
        super(context, R.style.common_dialog_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.common_loading_dialog_layout);
        progressView = (ProgressView) findViewById(R.id.common_loading_progress);
        setOnDismissListener(this);
    }

    @Override
    protected void onStart() {
        progressView.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        progressView.stop();
        super.onStop();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (progressView != null){
            progressView.stop();
        }
    }
}
