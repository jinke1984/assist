package cn.com.jinke.assist.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import org.json.JSONException;
import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.function.adapter.ZcfgAdapter;
import cn.com.jinke.assist.function.manager.ZcfgManager;
import cn.com.jinke.assist.function.model.Zcfg;
import cn.com.jinke.assist.manager.PullToRefreshHelper;
import cn.com.jinke.assist.web.WebUI;

/**
 * Created by apple on 2017/1/19.
 */

public class ZcfgUI extends ProjectBaseUI implements OnItemClickListener, OnRefreshListener2<ListView>{

    @ViewInject(R.id.zcfg_no_data_iv)
    private ImageView mNoDataIV;

    @ViewInject(R.id.list_view)
    private PullToRefreshListView mListView;

    private ZcfgAdapter mZcfgAdapter = null;

    private int[] MSG = new int[]{ZCFG_MSG};

    @Override
    protected boolean handleMessage(Message msg) {
        switch (msg.what){
            case ZCFG_MSG:
                dismissLoading();
                mListView.onRefreshComplete();
                mZcfgAdapter.setData(ZcfgManager.getInstance().getsList());
                mZcfgAdapter.notifyDataSetChanged();
                setFooter();
                break;
            default:
                break;
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_zcfg);
        registerMessages(MSG);
    }
    
    public static final void startActivity(Context aContext){
        Intent intent = new Intent(aContext, ZcfgUI.class);
        aContext.startActivity(intent);
    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        if(header != null){
            header.titleText.setText(getString(R.string.zcfg));
            header.rightLayout.setVisibility(View.GONE);
        }

        PullToRefreshHelper.initHeader(mListView);
        PullToRefreshHelper.initFooter(mListView);
        mListView.setOnRefreshListener(this);
        mZcfgAdapter = new ZcfgAdapter(this);
        mListView.setAdapter(mZcfgAdapter);
        mListView.setOnItemClickListener(this);

        setFooter();
    }

    @Override
    protected void onInitData() {
        if(isNetworkConnected()){
            showLoading();
            try{
                ZcfgManager.getInstance().getZcfgData("", 1, true);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            showToast(R.string.wlywt);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Zcfg zcfg = ZcfgManager.getInstance().getsList().get(position-1);
        WebUI.startActivity(this, zcfg.getInfoid(), zcfg.getInfoname());
    }

    @Override
    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
        if(isNetworkConnected()){
            try{
                ZcfgManager.getInstance().getZcfgData("", 1, true);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            showToast(R.string.wlywt);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    refreshView.onRefreshComplete();
                }
            });
        }
    }


    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
        if(isNetworkConnected()){
            try{
                ZcfgManager.getInstance().getZcfgData("", 1, false);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            showToast(R.string.wlywt);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    refreshView.onRefreshComplete();
                }
            });
        }
    }

    private void setFooter() {
        if (mZcfgAdapter.getCount() == 0) {
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
        else {
            if (ZcfgManager.isFinish()) {
                mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            else {
                mListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }
    }
}
