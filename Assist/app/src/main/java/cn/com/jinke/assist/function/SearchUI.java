package cn.com.jinke.assist.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import org.json.JSONException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.function.adapter.SearchAdapter;
import cn.com.jinke.assist.function.manager.SearchManager;
import cn.com.jinke.assist.function.model.Persion;
import cn.com.jinke.assist.manager.PullToRefreshHelper;

/**
 * Created by jinke on 2017/3/13.
 */

public class SearchUI extends ProjectBaseUI implements OnItemClickListener, OnRefreshListener2<ListView> {

    @ViewInject(R.id.search_edittext)
    private EditText mSearchET = null;

    @ViewInject(R.id.back_btn)
    private ImageView mBackIV = null;

    @ViewInject(R.id.search_tv)
    private TextView mSearchTV = null;

    @ViewInject(R.id.list_view)
    private PullToRefreshListView mPtrListView = null;
    private SearchAdapter mAdapter = null;

    private String mSearch = null;
    private int[] MSG = new int[]{PERSION_MSG};

    @Override
    protected boolean handleMessage(Message msg) {
        switch (msg.what){
            case PERSION_MSG:
                dismissLoading();
                mPtrListView.onRefreshComplete();
                mAdapter.setData(SearchManager.getInstance().getsList());
                mAdapter.notifyDataSetChanged();
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
        registerMessages(MSG);
        setContentView(R.layout.ui_gzfw);
    }

    public static final void startActivity(Context aContext){
        Intent intent = new Intent(aContext, SearchUI.class);
        aContext.startActivity(intent);
    }

    @Override
    protected void onInitView() {

        PullToRefreshHelper.initHeader(mPtrListView);
        PullToRefreshHelper.initFooter(mPtrListView);

        SpannableString hint_string = new SpannableString(getString(R.string.qsrnycxdxf));
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(16, true);
        hint_string.setSpan(ass, 0, hint_string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSearchET.setHint(new SpannedString(hint_string));

        mPtrListView.setOnRefreshListener(this);
        mAdapter = new SearchAdapter(this);
        mPtrListView.setAdapter(mAdapter);
        mPtrListView.setOnItemClickListener(this);

        setFooter();

    }

    @Event(value = {R.id.back_btn, R.id.search_tv}, type = View.OnClickListener.class)
    private void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.search_tv:
                mSearch = mSearchET.getText().toString().trim();
                if(TextUtils.isEmpty(mSearch)){
                    showToast(R.string.qsrnycxdxf);
                    return;
                }

                if(isNetworkConnected()){
                    showLoading();
                    try{
                        SearchManager.getInstance().getPersionData(mSearch, true);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else{
                    showToast(R.string.wlywt);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Persion persion = SearchManager.getInstance().getsList().get(position-1);
        XxcjUI.startActivity(this, persion.getEntityid());

    }

    private void setFooter() {
        if (mAdapter.getCount() == 0) {
            mPtrListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
        else {
            if (SearchManager.isFinish()) {
                mPtrListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            else {
                mPtrListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }
    }

    @Override
    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
        if(isNetworkConnected()){
            try{
                SearchManager.getInstance().getPersionData(mSearch, true);
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
                SearchManager.getInstance().getPersionData(mSearch, false);
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
}
