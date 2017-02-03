package cn.com.jinke.assist.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.utils.MessageProxy;

/**
 * Created by apple on 2017/1/23.
 */

public class WebUI extends ProjectBaseUI {

//    @ViewInject(R.id.webview)
//    private ProgressWebView mWebView = null;

    private final int MESSAGE_FAIL = 10001;

    @Override
    protected boolean handleMessage(Message msg) {
        switch (msg.what){
            case MESSAGE_FAIL:
                dismissLoading();
                showToast("数据保存错误!");
                break;
            default:
                break;
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerMessages(MESSAGE_FAIL);
        setContentView(R.layout.ui_sjcj);
    }

    public static void startWebActivity(Context aContext) {
        Intent intent = new Intent(aContext, WebUI.class);
        aContext.startActivity(intent);
    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        if(header != null){
            header.titleText.setText(getString(R.string.sjcj));
            header.leftImageBtn.setVisibility(View.VISIBLE);
            header.rightLayout.setVisibility(View.GONE);
            header.leftText.setVisibility(View.GONE);
            header.rightLayout.setVisibility(View.VISIBLE);
            header.rightImageBtn.setVisibility(View.GONE);
            header.rightText.setVisibility(View.VISIBLE);
            header.rightText.setText("保存");
            header.rightLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                    MessageProxy.sendEmptyMessageDelay(MESSAGE_FAIL, 4000);
                }
            });

        }

//        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
//        mWebView.getSettings().setUseWideViewPort(false);
//        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.getSettings().setSavePassword(false);
    }

    @Override
    protected void onInitData() {
//        String url = "http://112.74.181.48:72/adminpages/PersonalFile_form.aspx";
//        mWebView.loadUrl(url);
    }
}
