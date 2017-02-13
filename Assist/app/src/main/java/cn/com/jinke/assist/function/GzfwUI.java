package cn.com.jinke.assist.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.customview.AppListDialog;
import cn.com.jinke.assist.function.adapter.GzfwAdapter;
import cn.com.jinke.assist.function.manager.GzfwManager;
import cn.com.jinke.assist.function.model.Gzfw;
import cn.com.jinke.assist.manager.PullToRefreshHelper;

/**
 * Created by apple on 2017/1/20.
 */

public class GzfwUI extends ProjectBaseUI implements OnItemClickListener{

    @ViewInject(R.id.search_edittext)
    private EditText mSearchET = null;

    @ViewInject(R.id.back_btn)
    private ImageView mBackIV = null;

    @ViewInject(R.id.search_tv)
    private TextView mSearchTV = null;

    @ViewInject(R.id.list_view)
    private PullToRefreshListView mPtrListView = null;

    private GzfwAdapter mAdapter = null;

    private int[] MSG = new int[]{GZFW_MSG, ACCESS_NET_FAILED, GZFW_REFRESH};
    private final String BDS = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])" +
            "|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";

    @Override
    protected boolean handleMessage(Message msg) {
        switch (msg.what){
            case GZFW_MSG:
                dismissLoading();
                mAdapter.setData(GzfwManager.getInstance().getList());
                mAdapter.notifyDataSetChanged();
                break;
            case ACCESS_NET_FAILED:
                dismissLoading();
                break;
            case GZFW_REFRESH:
                showLoading();
                String search = mSearchET.getText().toString().trim();
                GzfwManager.getInstance().getGzfwData(search, true);
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

    @Override
    protected void onInitView() {

        PullToRefreshHelper.initHeader(mPtrListView);
        PullToRefreshHelper.initFooter(mPtrListView);

        SpannableString hint_string = new SpannableString(getString(R.string.qsrycxdsfz));
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(16, true);
        hint_string.setSpan(ass, 0, hint_string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSearchET.setHint(new SpannedString(hint_string));

        mAdapter = new GzfwAdapter(this);
        mPtrListView.setAdapter(mAdapter);
        mPtrListView.setOnItemClickListener(this);
    }

    @Event(value = {R.id.back_btn, R.id.search_tv}, type = View.OnClickListener.class)
    private void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.search_tv:
                String search = mSearchET.getText().toString().trim();
                if(TextUtils.isEmpty(search)){
                    showToast(R.string.qsrycxdsfz);
                    return;
                }

                Pattern pattern = Pattern.compile(BDS);
                Matcher matcher = pattern.matcher(search);
                boolean result = matcher.matches();

                if(!result){
                    showToast(R.string.sfzscl);
                    return;
                }

                if(isNetworkConnected()){
                    showLoading();
                    GzfwManager.getInstance().getGzfwData(search, true);
                }else{
                    showToast(R.string.wlywt);
                }
                break;
            default:
                break;
        }
    }

    public static final void startActivity(Context aContext){
        Intent intent = new Intent(aContext, GzfwUI.class);
        aContext.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Gzfw gzfw = GzfwManager.getInstance().getList().get(position-1);
        choiceperation(gzfw);
    }

    private void choiceperation(final Gzfw aGzfw){
        ArrayList<String> list = new ArrayList<String>();
        list.add(getString(R.string.xg));
        list.add(getString(R.string.xz));
        String tishi =  getString(R.string.xzfs);
        AppListDialog dialog = new AppListDialog(this, tishi, list, true);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //修改
                        GzfwDetailUI.startActivity(GzfwUI.this, aGzfw, true);
                        break;
                    case 1:
                        //新增
                        GzfwDetailUI.startActivity(GzfwUI.this, aGzfw, false);
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.show();
    }
}
