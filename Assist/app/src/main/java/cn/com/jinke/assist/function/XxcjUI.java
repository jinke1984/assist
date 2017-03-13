package cn.com.jinke.assist.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.customview.ProgressWebView;
import cn.com.jinke.assist.function.manager.XxcjManager;
import cn.com.jinke.assist.me.manager.MasterManager;

/**
 * Created by jinke on 2017/3/8.
 */

public class XxcjUI extends ProjectBaseUI {

    @ViewInject(R.id.webview)
    private ProgressWebView mProgressWebView;

    private int mId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_web);
    }

    public static final void startActivity(Context aContext, int id){
        Intent intent = new Intent(aContext, XxcjUI.class);
        intent.putExtra(B_ID, id);
        aContext.startActivity(intent);
    }

    @Override
    protected void onPreInitView() {

    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        Intent intent = getIntent();
        if(header != null && intent != null){
            header.titleText.setText(R.string.sjcj);
            header.rightLayout.setVisibility(View.GONE);
            mId = intent.getIntExtra(B_ID, mId);
        }


        String userId = MasterManager.getInstance().getUserCard().getUserid();
        String url = XxcjManager.getInstance().getXxcjUrl(userId, mId);

        if(TextUtils.isEmpty(url)){
            finish();
            showToast(R.string.gymbcz);
        }

        // 开启硬件加速
        getWindow().addFlags(0x01000000);

        WebSettings settings = mProgressWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setUseWideViewPort(false);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        settings.setSavePassword(false);
        settings.setPluginState(WebSettings.PluginState.ON);
        mProgressWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mProgressWebView.loadUrl(url);
        mProgressWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mProgressWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            mProgressWebView.goBack();
            if (!mProgressWebView.canGoBack()) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
