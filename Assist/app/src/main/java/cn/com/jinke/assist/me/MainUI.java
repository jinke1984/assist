package cn.com.jinke.assist.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.KeyEvent;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.function.GzfwUI;
import cn.com.jinke.assist.function.JybfUI;
import cn.com.jinke.assist.function.ZcfgUI;
import cn.com.jinke.assist.manager.VersionManager;

/**
 * Created by apple on 2017/1/19.
 */

public class MainUI extends ProjectBaseUI {

    @ViewInject(R.id.sjcj_iv)
    private RelativeLayout mSjcjLayout = null;

    @ViewInject(R.id.jybf_iv)
    private RelativeLayout mJybfLayout = null;

    @ViewInject(R.id.zcfg_iv)
    private RelativeLayout mZcfgLayout = null;

    @ViewInject(R.id.gzfw_iv)
    private RelativeLayout mGzfwLayout = null;

    private long mLastPressBackTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main);

        VersionManager.startCheckUpdate();
    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        if(header != null){
            header.leftLayout.setVisibility(View.GONE);
            header.titleText.setText(getString(R.string.tbt));
            header.centerIV.setVisibility(View.VISIBLE);
            header.centerIV.setBackgroundResource(R.drawable.top_image);
            header.rightLayout.setVisibility(View.GONE);
        }
    }

    public static final void startActivity(Context aContext){
        Intent intent = new Intent(aContext, MainUI.class);
        aContext.startActivity(intent);
    }

    @Event(value = {R.id.sjcj_iv, R.id.jybf_iv, R.id.zcfg_iv, R.id.gzfw_iv})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.sjcj_iv:

                break;
            case R.id.gzfw_iv:
                GzfwUI.startActivity(this);
                break;
            case R.id.zcfg_iv:
                ZcfgUI.startActivity(this);
                break;
            case R.id.jybf_iv:
                JybfUI.startActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            long current = System.currentTimeMillis();
            if (current - mLastPressBackTime > 2000) {
                mLastPressBackTime = current;
                showToast(R.string.zcdjtc);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
