package cn.com.jinke.assist.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.function.manager.GzfwManager;
import cn.com.jinke.assist.function.model.Gzfw;
import cn.com.jinke.assist.utils.MessageProxy;

/**
 * Created by jinke on 2017/1/20.
 */

public class GzfwDetailUI extends ProjectBaseUI {

    @ViewInject(R.id.jyqk_et)
    private EditText mJYET = null;

    @ViewInject(R.id.fwdx_et)
    private EditText mFWET = null;

    @ViewInject(R.id.gzqk_et)
    private EditText mGzET = null;

    @ViewInject(R.id.jyyz1_et)
    private EditText mYzET = null;

    @ViewInject(R.id.gzry_et)
    private EditText mRYET = null;

    @ViewInject(R.id.dcsj_et)
    private EditText mDHET = null;

    private Gzfw mGzfw = null;
    private boolean isShow = false;
    private int mEntityid = 0;   //跟踪服务数据的ID，新增时值传0，修改时这个值大于0

    private int[] MSG = new int[]{GZFW_UPLOAD};

    @Override
    protected boolean handleMessage(Message msg) {
        switch (msg.what){
            case GZFW_UPLOAD:
                dismissLoading();
                showToast(R.string.xgcg);
                MessageProxy.sendEmptyMessage(GZFW_REFRESH);
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
        registerMessages(MSG);
        setContentView(R.layout.ui_gzfw_detail);
    }

    public static final void startActivity(Context aContext, Gzfw aGzfw, boolean isCaoZuo){

        Intent intent = new Intent(aContext, GzfwDetailUI.class);
        intent.putExtra(B_ENTITY, aGzfw);
        intent.putExtra(B_CODE, isCaoZuo);
        aContext.startActivity(intent);
    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        if(header != null){
            header.leftImageBtn.setVisibility(View.VISIBLE);
            header.titleText.setText(getString(R.string.gzfwxq));
        }
    }

    @Override
    protected void onInitData() {
        Intent intent = getIntent();
        if(intent != null){
            mGzfw = (Gzfw)intent.getSerializableExtra(B_ENTITY);
            isShow = intent.getBooleanExtra(B_CODE, isShow);
        }

        if(isShow){
            mEntityid = mGzfw.getEntityid();
            mJYET.setText(mGzfw.getJobcircs());
            mFWET.setText(mGzfw.getPhoneno());
            mGzET.setText(mGzfw.getSrvcircs());
            mYzET.setText(mGzfw.getHelpcircs());
            mRYET.setText(mGzfw.getStaff());
            mDHET.setText(mGzfw.getExaminetime());
        }
    }

    @Event(value = R.id.okbtn)
    private void onViewClick(View view){
        switch (view.getId()){
            case R.id.okbtn:
                try{
                    String jv = mJYET.getText().toString().trim();
                    String fw = mFWET.getText().toString().trim();
                    String gz = mGzET.getText().toString().trim();
                    String yz = mYzET.getText().toString().trim();
                    String ry = mRYET.getText().toString().trim();
                    String dh = mDHET.getText().toString().trim();
                    if(TextUtils.isEmpty(jv) || TextUtils.isEmpty(fw) || TextUtils.isEmpty(gz)
                            || TextUtils.isEmpty(yz) || TextUtils.isEmpty(ry) || TextUtils.isEmpty(dh)){
                        showToast(R.string.tjshbwzqzxjc);
                        return;
                    }

                    showLoading();
                    int personalprofileid = mGzfw.getPersonalprofileid();
                    GzfwManager.getInstance().uploadGzfwData(mEntityid, personalprofileid, jv, fw, gz,
                            yz, ry, dh);
                }catch(JSONException e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
