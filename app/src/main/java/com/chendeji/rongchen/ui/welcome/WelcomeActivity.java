package com.chendeji.rongchen.ui.welcome;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.ui.merchant.MerchantListActivity;


public class WelcomeActivity extends AppCompatActivity implements IMap.IMapLocationListener {
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        Logger.i(TAG, "onResume");
        MapManager.getManager().getMap().startLocation(this);
        super.onResume();
    }

    @Override
    public void onLocationSuccece() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MerchantListActivity.class);
                startActivity(intent);
                //注销监听
                MapManager.getManager().getMap().unregisteListener();
                finish();
            }
        }, 1000);
    }

    @Override
    public void onLocationFail() {
        ToastUtil.showLongToast(this, "定位失败");
    }

    @Override
    protected void onDestroy() {
        Logger.i(TAG, "onDestroy");
        MapManager.getManager().getMap().unregisteListener();
        super.onDestroy();
    }
}
