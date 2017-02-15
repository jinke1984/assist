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
import cn.com.jinke.assist.function.model.Zcfg;
import cn.com.jinke.assist.webinterface.core.CallbackWrapper;

/**
 * Created by jinke on 2017/2/13.
 */

public class ZcfgManager implements CodeConstants, MsgKey, UrlSetting {

    public static ZcfgManager instance;
    private List<Zcfg> sList = new ArrayList<>();

    public ZcfgManager(){

    }

    public static ZcfgManager getInstance(){
        if (instance == null) {
            synchronized (ZcfgManager.class) {
                if (instance == null) {
                    instance = new ZcfgManager();
                }
            }
        }
        return instance;
    }

    public List<Zcfg> getsList() {
        return sList;
    }

    public void setsList(List<Zcfg> sList) {
        this.sList = sList;
    }

    /**
     * 获取政策法规集合
     * @param aKeyword     关键字可为空
     * @param aInfotypeid  分类id 可以为0
     */
    public void getZcfgData(String aKeyword, int aInfotypeid) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEYWORD, aKeyword);
        jsonObject.put(INFOTYPEID, aInfotypeid);
        String md5 = MD5Utils.getMD5(jsonObject.toString());
        RequestParams params = new RequestParams(BASURL);
        params.addParameter(COMMAND,ZCFGLIST);
        params.addParameter(DATA, jsonObject);
        params.addParameter(MD5, md5);
        x.http().post(params, new CallbackWrapper<List<Zcfg>>(ZCFG_MSG, 2){

            @Override
            public void onSuccess(int state, String msg, List<Zcfg> object, int total) {
                if(state == SUCCESS && object != null){
                    sList.addAll(object);
                }
                MessageProxy.sendMessage(mMsgCode, state, msg);
            }
        });
    }

    public String getZcfgDetail(int aId){
        String id = String.valueOf(aId);
        String md5 = MD5Utils.getMD5(id);
        RequestParams params = new RequestParams(ZCFGYRL);
        params.addBodyParameter(DATAID, id);
        params.addParameter(MD5, md5);
        String url = params.toString();
        return url;
    }

}
