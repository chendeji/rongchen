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
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.welcome.WelcomeActivity;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

/**
 * 用于显示登入，或者团购订单生成的界面
 */
public class Html5WebActivity extends AppCompatActivity {
    public static final String URL_KEY = "deal_h5_url";

    private WebView mDianpingView;
    private String mDeal_H5_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html5_web);
        mDeal_H5_url = getIntent().getStringExtra(URL_KEY);
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
            Logger.i("chendeji", "shouldOverrideUrlLoading" + "url:" + url);
            if (AppConst.AppBaseConst.DIANPING_LOGOUT.equalsIgnoreCase(url)) {
                //兼容低版本
                CookieManager.getInstance().removeAllCookie();
                view.loadUrl(AppConst.AppBaseConst.APP_LOGIN_URL);
                return true;
            }

            //点击返回键能够跳到点评首页
            if (AppConst.AppBaseConst.DIANPING_INDEX.equalsIgnoreCase(url)){
                finish();
            }
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            Logger.i("chendeji", "onFormResubmission");
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.i("chendeji", "onPageStarted-----url:" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Logger.i("chendeji", "onPageFinished----url:" + url);
            String cookieStr = CookieManager.getInstance().getCookie(url);
            if (AppConst.AppBaseConst.APP_LOGIN_URL.equalsIgnoreCase(url)
                    && !TextUtils.isEmpty(cookieStr)) {
                SettingFactory.getInstance().setIsFirstTimeLogin(false);
                Logger.i("chendeji", "登入成功！！！");
                //登入成功
                Intent intent = new Intent(Html5WebActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }

            Logger.i("chendeji", "cookieStr:" + cookieStr);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Logger.i("chendeji", "onLoadResource----url:" + url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Logger.i("chendeji", "shouldInterceptRequest----request:" + request.toString());
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            Logger.i("chendeji", "onLoadResource----url:" + url + "isReload:" + isReload);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            Logger.i("chendeji", "onLoadResource----realm:" + realm + "account:" + account + "args:" + args);
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
            if (mDianpingView.canGoBack()){
                mDianpingView.goBack();
                return true;
            } else {
                //TODO 显示一个对话框，提示是否退出应用
                //在前面还有界面的情况下，应该只能finish这个界面，要判断栈中是否还有界面
//                showExistDialog();
                boolean isLastActivity = SystemUtil.isLastActivityInTask(this);
                if (isLastActivity){
                    showExistDialog();
                    return true;
                } else {
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExistDialog() {
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.title(getString(R.string.exist))
                .positiveAction(getString(R.string.positive))
                .negativeAction(getString(R.string.negative));
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }
}