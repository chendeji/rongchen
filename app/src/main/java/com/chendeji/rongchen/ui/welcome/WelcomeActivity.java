package com.chendeji.rongchen.ui.welcome;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.Html5WebActivity;
import com.chendeji.rongchen.ui.city.ChooseCityActivity;
import com.chendeji.rongchen.ui.merchant.MerchantListActivity;


public class WelcomeActivity extends AppCompatActivity implements IMap.IMapLocationListener {
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        boolean isFirstTime = SettingFactory.getInstance().getIsFirstTimeLogin();
        String currentCity = SettingFactory.getInstance().getCurrentCity();
        if (isFirstTime) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin();
                }
            }, 1500);
        } else {
            //如果当前选择的城市为空，也就是没有选过城市
            if (TextUtils.isEmpty(currentCity)) {
//                MapManager.getManager().getMap().startLocation(this);
                onLocationFail();
            } else {
                //如果城市选择过了，直接进入到商户列表界面
                goToMerchantList();
            }
        }
        super.onResume();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, Html5WebActivity.class);
        intent.putExtra(Html5WebActivity.URL_KEY, AppConst.AppBaseConst.APP_LOGIN_URL);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLocationSuccece() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //注销监听
                MapManager.getManager().getMap().unregisteListener();
                goToMerchantList();
            }
        }, 1000);
    }

    private void goToMerchantList() {
        Intent intent = new Intent(WelcomeActivity.this, MerchantListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLocationFail() {
        ToastUtil.showLongToast(this, "定位失败");
        Intent intent = new Intent(this, ChooseCityActivity.class);
        startActivity(intent);
        MapManager.getManager().getMap().unregisteListener();
        finish();
    }

    @Override
    protected void onDestroy() {
        Logger.i(TAG, "onDestroy");
        MapManager.getManager().getMap().unregisteListener();
        super.onDestroy();
    }
}
