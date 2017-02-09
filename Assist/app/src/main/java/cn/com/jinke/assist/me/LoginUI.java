package cn.com.jinke.assist.me;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;
import cn.com.jinke.assist.me.manager.LoginManager;
import cn.com.jinke.assist.me.manager.MasterManager;
import cn.com.jinke.assist.me.model.UserCard;

/**
 * Created by apple on 2017/1/17.
 */

public class LoginUI extends ProjectBaseUI {

    @ViewInject(R.id.account)
    private EditText mAccountET = null;

    @ViewInject(R.id.password)
    private EditText mPasswordET = null;

    @ViewInject(R.id.login_button)
    private Button mLoginBtn = null;

    private int[] MSG = new int[]{LOGIN_MSG, ACCESS_NET_FAILED};

    @Override
    protected boolean handleMessage(Message msg) {
        switch (msg.what){
            case LOGIN_MSG:
                int code = msg.arg1;
                if(code == 200){
                    MainUI.startActivity(this);
                }
                dismissLoading();
                break;
            case ACCESS_NET_FAILED:
                dismissLoading();
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
        UserCard aUserCard = MasterManager.getInstance().getUserCard();
        if(aUserCard != null){
            MainUI.startActivity(this);
        }else{
            setContentView(R.layout.ui_login);
        }
    }

    @Override
    protected void onInitView() {
        mAccountET.addTextChangedListener(watcher);
        mPasswordET.addTextChangedListener(watcher);
        loginBtnShow();
    }

    private TextWatcher watcher = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            loginBtnShow();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

    };

    private void loginBtnShow(){
        if(TextUtils.isEmpty(mAccountET.getText().toString().trim()) ||
                TextUtils.isEmpty(mPasswordET.getText().toString().trim())){
            mLoginBtn.setEnabled(false);
        }else{
            mLoginBtn.setEnabled(true);
        }
    }


    @Event(value = R.id.login_button)
    private void onClick(View view){
        switch (view.getId()){
            case R.id.login_button:
                try{
                    showLoading();
                    String accout = mAccountET.getText().toString().trim();
                    String password = mPasswordET.getText().toString().trim();
                    LoginManager.getInstance().login(accout, password);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

}
