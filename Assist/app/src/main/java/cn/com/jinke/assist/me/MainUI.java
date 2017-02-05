package cn.com.jinke.assist.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.function.GzfwUI;
import cn.com.jinke.assist.function.JybfUI;
import cn.com.jinke.assist.function.ZcfgUI;
import cn.com.jinke.assist.web.WebUI;

/**
 * Created by apple on 2017/1/19.
 */

public class MainUI extends ProjectBaseUI {

    @ViewInject(R.id.one_iv)
    private RelativeLayout mSjcjLayout = null;

    @ViewInject(R.id.two_iv)
    private RelativeLayout mGzfwLayout = null;

    @ViewInject(R.id.three_iv)
    private RelativeLayout mZcfgLayout = null;

    @ViewInject(R.id.four_iv)
    private RelativeLayout mJtbfLayout = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main);
    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        if(header != null){
            header.leftLayout.setVisibility(View.GONE);
            header.titleText.setText(getString(R.string.tbt));
            header.rightLayout.setVisibility(View.GONE);
        }
    }

    public static final void startActivity(Context aContext){
        Intent intent = new Intent(aContext, MainUI.class);
        aContext.startActivity(intent);
    }

    @Event(value = {R.id.one_iv, R.id.two_iv, R.id.three_iv, R.id.four_iv})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.one_iv:
                WebUI.startWebActivity(this);
                break;
            case R.id.two_iv:
                GzfwUI.startActivity(this);
                break;
            case R.id.three_iv:
                ZcfgUI.startActivity(this);
                break;
            case R.id.four_iv:
                JybfUI.startActivity(this);
                break;
            default:
                break;
        }
    }
}
