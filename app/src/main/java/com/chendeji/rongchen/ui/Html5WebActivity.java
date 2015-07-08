package com.chendeji.rongchen.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.SystemUtil;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.welcome.WelcomeActivity;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.util.Set;

/**
 * 用于显示登入，或者团购订单生成的界面
 */
public class Html5WebActivity extends AppCompatActivity {
    public static final String URL_KEY = "deal_h5_url";

    public static final String SHOW_MODE = "show_mode";

    public static final int SHOW_USER = 0;
    public static final int LONGIN = 1;
    public static final int LONGOUT = 2;
    public static final int SHOW_COMMENT = 3;

    private int showMode = LONGIN;


    private WebView mDianpingView;
    private String mDeal_H5_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html5_web);
        mDeal_H5_url = getIntent().getStringExtra(URL_KEY);
        showMode = getIntent().getIntExtra(SHOW_MODE, showMode);
        mDianpingView = (WebView) findViewById(R.id.wv_dianping_source_view);
        WebSettings settings = mDianpingView.getSettings();
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        mDianpingView.setWebChromeClient(new MyWebChromeClient());
        mDianpingView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //TODO 加载网页
            Logger.i("chendeji", "url:" + url);
            if (AppConst.AppBaseConst.DIANPING_LOGOUT.equalsIgnoreCase(url)) {
                //兼容低版本
                CookieManager.getInstance().removeAllCookie();
                view.loadUrl(AppConst.AppBaseConst.APP_LOGIN_URL);
                return true;
            }

            //点击返回键能够跳到点评首页
            if (AppConst.AppBaseConst.DIANPING_INDEX.equalsIgnoreCase(url)
                    || AppConst.AppBaseConst.DIANPING_TUAN.equalsIgnoreCase(url)) {
                finish();
            }
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Logger.i("chendeji", "onPageFinished----url:" + url);
            String cookieStr = CookieManager.getInstance().getCookie(url);
            if (AppConst.AppBaseConst.APP_LOGIN_URL.equalsIgnoreCase(url)
                    && !TextUtils.isEmpty(cookieStr)) {
                if (!SettingFactory.getInstance().getIsLoginSuccess()) {
                    SettingFactory.getInstance().setIsFirstTimeLogin(false);
                    Logger.i("chendeji", "登入成功！！！");
                    ToastUtil.showLongToast(Html5WebActivity.this, getString(R.string.login_success));
                    //登入成功
                    if (showMode == LONGIN) {
                        SettingFactory.getInstance().setIsLoginSuccess(true);
                        Intent intent = new Intent(Html5WebActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Logger.i("chendeji", "onJsAlert-------------" + "url:" + url + "message:" + message + "result:" + result.toString());
//            Dialog.Builder builder = new SimpleDialog.Builder(R.style.common_dialog_style){
//                @Override
//                public void onPositiveActionClicked(DialogFragment fragment) {
//                    result.confirm();
//                    super.onPositiveActionClicked(fragment);
//                }
//
//                @Override
//                public void onNegativeActionClicked(DialogFragment fragment) {
////                    Toast.makeText(mActivity, "Canceled", Toast.LENGTH_SHORT).show();
//                    super.onNegativeActionClicked(fragment);
//                }
//            };
//
//            builder.title(message)
//                    .positiveAction(getString(R.string.positive))
//                    .negativeAction(getString(R.string.negative));
//            DialogFragment fragment = DialogFragment.newInstance(builder);
//            fragment.show(getSupportFragmentManager(), null);
            return false;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Logger.i("chendeji", "onJsConfirm-------------" + "url:" + url + "message:" + message + "result:" + result.toString());
            return false;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Logger.i("chendeji", "onJsPrompt-------------" + "url:" + url + "message:" + message + "defualtValue:" + defaultValue + "result:" + result.toString());
            return false;
        }

        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            Logger.i("chendeji", "onJsPrompt-------------" + "url:" + url + "message:" + message + "result:" + result.toString());
            return false;
        }

    }

    @Override
    protected void onResume() {
        mDianpingView.loadUrl(mDeal_H5_url);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mDianpingView.canGoBack()) {
                mDianpingView.goBack();
                return true;
            } else {
                //TODO 显示一个对话框，提示是否退出应用
                //在前面还有界面的情况下，应该只能finish这个界面，要判断栈中是否还有界面
                boolean isLastActivity = SystemUtil.isLastActivityInTask(this);
                if (isLastActivity) {
                    SystemUtil.showExistDialog(this);
                    return true;
                } else {
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
