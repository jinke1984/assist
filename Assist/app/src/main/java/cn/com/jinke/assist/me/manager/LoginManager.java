package cn.com.jinke.assist.me.manager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.com.jinke.assist.me.model.UserCard;
import cn.com.jinke.assist.utils.CodeConstants;
import cn.com.jinke.assist.utils.MD5Utils;
import cn.com.jinke.assist.utils.MessageProxy;
import cn.com.jinke.assist.utils.MsgKey;
import cn.com.jinke.assist.utils.UrlSetting;
import cn.com.jinke.assist.webinterface.core.CallbackWrapper;

/**
 * Created by apple on 2017/1/18.
 */

public class LoginManager implements CodeConstants, MsgKey, UrlSetting{

    private static LoginManager instance;

    private LoginManager() {

    }

    public static LoginManager getInstance(){
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    public void login(String aAccount, String aPassword) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USERNAME, aAccount);
        jsonObject.put(PWD, aPassword);
        String md5 = MD5Utils.getMD5(jsonObject.toString());
        RequestParams params = new RequestParams(BASURL);
        params.addParameter(COMMAND,LOGIN);
        params.addParameter(DATA, jsonObject);
        params.addParameter(MD5, md5);
        x.http().post(params, new CallbackWrapper<UserCard>(LOGIN_MSG, 2){

            @Override
            public void onSuccess(int state, String msg, UserCard object, int total) {

                if(state == SUCCESS && object != null){
                    MasterManager.getInstance().init(object);
                }

                MessageProxy.sendMessage(mMsgCode, state, msg);
            }
        });

    }
}
