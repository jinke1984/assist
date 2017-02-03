package cn.com.jinke.assist.function.manager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.com.jinke.assist.function.model.Gzfw;
import cn.com.jinke.assist.utils.CodeConstants;
import cn.com.jinke.assist.utils.MD5Utils;
import cn.com.jinke.assist.utils.MessageProxy;
import cn.com.jinke.assist.utils.MsgKey;
import cn.com.jinke.assist.utils.UrlSetting;
import cn.com.jinke.assist.webinterface.core.CallbackWrapper;

/**
 * Created by jinke on 2017/1/20.
 */

public class GzfwManager implements CodeConstants, MsgKey, UrlSetting {

    public static GzfwManager instance;
    private List<Gzfw> sList = new ArrayList<>();

    public GzfwManager(){

    }

    public static GzfwManager getInstance(){
        if (instance == null) {
            synchronized (GzfwManager.class) {
                if (instance == null) {
                    instance = new GzfwManager();
                }
            }
        }
        return instance;
    }

    public List<Gzfw> getList() {
        return sList;
    }

    public void setList(List<Gzfw> sList) {
        this.sList = sList;
    }

    public void getGzfwData(String aNumber, boolean isRefresh){

        if(isRefresh){
            sList.clear();
        }
        String md5 = MD5Utils.getMD5(aNumber);
        RequestParams params = new RequestParams(BASURL);
        params.addParameter(COMMAND,GETSRVLIST);
        params.addParameter(DATA, aNumber);
        params.addParameter(MD5, md5);
        x.http().post(params, new CallbackWrapper<List<Gzfw>>(GZFW_MSG, 2){

            @Override
            public void onSuccess(int state, String msg, List<Gzfw> object, int total) {
                if(state == SUCCESS && object != null){
                    sList.addAll(object);
                }
                MessageProxy.sendMessage(mMsgCode, state, msg);
            }
        });
    }

    public void uploadGzfwData(int entityid, int personalprofileid, String jobcircs, String phoneno,
                               String srvcircs, String helpcircs, String staff, String examinetime) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("entityid", entityid);
        jsonObject.put("personalprofileid", personalprofileid);
        jsonObject.put("jobcircs", jobcircs);
        jsonObject.put("phoneno", phoneno);
        jsonObject.put("srvcircs", srvcircs);
        jsonObject.put("helpcircs", helpcircs);
        jsonObject.put("staff", staff);
        jsonObject.put("examinetime", examinetime);
        String md5 = MD5Utils.getMD5(jsonObject.toString());
        RequestParams params = new RequestParams(BASURL);
        params.addParameter(COMMAND,SAVESRV);
        params.addParameter(DATA, jsonObject);
        params.addParameter(MD5, md5);
        x.http().post(params, new CallbackWrapper<Void>(GZFW_UPLOAD, 2){
            @Override
            public void onSuccess(int state, String msg, Void object, int total) {
                MessageProxy.sendMessage(mMsgCode, state, msg);
            }
        });
    }

}
