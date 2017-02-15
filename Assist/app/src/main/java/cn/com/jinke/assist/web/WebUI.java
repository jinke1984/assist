package cn.com.jinke.assist.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;

import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.customview.ProgressWebView;
import cn.com.jinke.assist.function.manager.ZcfgManager;

/**
 * Created by jinke on 2017/2/13.
 */

public class WebUI extends ProjectBaseUI {

    @ViewInject(R.id.webview)
    private ProgressWebView mProgressWebView;

    private int mId = 0;
    private String mTitle = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_web);
    }

    public static final void startActivity(Context aContext, int aId, String aTitle){
        Intent intent = new Intent(aContext, WebUI.class);
        intent.putExtra(B_ID, aId);
        intent.putExtra(B_TITLE, aTitle);
        aContext.startActivity(intent);
    }

    @Override
    protected void onPreInitView() {
        Intent intent = getIntent();
        if(intent != null){
            mId = intent.getIntExtra(B_ID, mId);
            mTitle = intent.getStringExtra(B_TITLE);
        }
    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        if(header != null){
            header.titleText.setText(mTitle);
            header.rightLayout.setVisibility(View.GONE);
        }

        if(mId == 0){
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
        mProgressWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        String url = ZcfgManager.getInstance().getZcfgDetail(mId);
        mProgressWebView.loadUrl(url);
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
