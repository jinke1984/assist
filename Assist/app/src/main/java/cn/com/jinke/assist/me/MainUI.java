package cn.com.jinke.assist.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.view.KeyEvent;

import org.json.JSONException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.function.GzfwUI;
import cn.com.jinke.assist.function.JybfUI;
import cn.com.jinke.assist.function.SearchUI;
import cn.com.jinke.assist.customview.ListViewForScrollView;
import cn.com.jinke.assist.function.ZcfgUI;
import cn.com.jinke.assist.function.manager.ZcfgManager;
import cn.com.jinke.assist.function.model.Zcfg;
import cn.com.jinke.assist.manager.VersionManager;
import cn.com.jinke.assist.me.adapter.MessageAdapter;
import cn.com.jinke.assist.me.manager.MasterManager;
import cn.com.jinke.assist.utils.MessageProxy;
import cn.com.jinke.assist.web.WebUI;
import cn.com.jinke.assist.customview.AppDialog;

/**
 * Created by apple on 2017/1/19.
 */

public class MainUI extends ProjectBaseUI implements OnItemClickListener {

    @ViewInject(R.id.sjcj_iv)
    private RelativeLayout mSjcjLayout = null;

    @ViewInject(R.id.jybf_iv)
    private RelativeLayout mJybfLayout = null;

    @ViewInject(R.id.zcfg_iv)
    private RelativeLayout mZcfgLayout = null;

    @ViewInject(R.id.gzfw_iv)
    private RelativeLayout mGzfwLayout = null;

    @ViewInject(R.id.tc_iv)
    private RelativeLayout mExitLayout = null;

    @ViewInject(R.id.listview)
    private ListViewForScrollView mListView = null;

    private MessageAdapter mAdapter = null;
    private long mLastPressBackTime;
    private int[] MSG = new int[]{MAINPAGE_MSG, MAINPAGE_EXIT};

    @Override
    protected boolean handleMessage(Message msg) {
        switch (msg.what){
            case MAINPAGE_MSG:
                dismissLoading();
                mAdapter.setData(ZcfgManager.getInstance().getMainPageList());
                mAdapter.notifyDataSetChanged();
                break;
            case MAINPAGE_EXIT:
                finish();
                break;
            default:
                break;
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main);
        registerMessages(MSG);
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

        mAdapter = new MessageAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void onInitData() {
        if(isNetworkConnected()){
            showLoading();
            try{
                ZcfgManager.getInstance().getMainPageZcfgData("", 2);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            showToast(R.string.wlywt);
        }
    }

    public static final void startActivity(Context aContext){
        Intent intent = new Intent(aContext, MainUI.class);
        aContext.startActivity(intent);
    }

    @Event(value = {R.id.sjcj_iv, R.id.jybf_iv, R.id.zcfg_iv, R.id.gzfw_iv, R.id.tc_iv})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.sjcj_iv:
                //XxcjUI.startActivity(this);
                SearchUI.startActivity(this);
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
            case R.id.tc_iv:
                logout();
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
                ZcfgManager.getInstance().mainPageClear();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Zcfg zcfg = ZcfgManager.getInstance().getMainPageList().get(position);
        WebUI.startActivity(this, zcfg.getInfoid(), zcfg.getInfoname());
    }

    private void logout(){
        AppDialog dialog = new AppDialog(this,  getString(R.string.qdtcm));
        dialog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.ok){
                    MasterManager.getInstance().logout();
                    LoginUI.startActivity(MainUI.this);
                    MessageProxy.sendEmptyMessage(MAINPAGE_EXIT);
                }
            }
        });
        dialog.show();
    }
}
