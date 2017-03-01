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

    private List<Zcfg> sList = new ArrayList<>();

    public static ZcfgManager instance;
    private final static int FINAL_PAGE_SIZE = 10;    //服务器一次推送10条数据下来
    public static long sLastRefreshTime; // 上次刷新时间
    private static int sPageStart;
    private static int sTotalCount;
    private static int mPage = 0;

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
     * @param isRefresh    是否刷新
     */
    public void getZcfgData(String aKeyword, int aInfotypeid, boolean isRefresh) throws JSONException {

        if(isRefresh){
            sPageStart = 1;
            mPage = 0;
            sList.clear();
            sLastRefreshTime = System.currentTimeMillis();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEYWORD, aKeyword);
        jsonObject.put(INFOTYPEID, aInfotypeid);
        jsonObject.put(PAGESIZE, FINAL_PAGE_SIZE);
        jsonObject.put(PAGEINDEX, isRefresh ? 1 : sPageStart);
        String md5 = MD5Utils.getMD5(jsonObject.toString());
        RequestParams params = new RequestParams(BASURL);
        params.addParameter(COMMAND,ZCFGLIST);
        params.addParameter(DATA, jsonObject);
        params.addParameter(MD5, md5);
        x.http().post(params, new CallbackWrapper<List<Zcfg>>(ZCFG_MSG, 2){

            @Override
            public void onSuccess(int state, String msg, List<Zcfg> object, int total) {
                if(state == SUCCESS && (object != null && object.size() != 0)){

                    sList.addAll(object);

                    if(sTotalCount >= 0){
                        mPage = mPage + 1;
                        sTotalCount = total;
                        int pageCount = sTotalCount/FINAL_PAGE_SIZE;
                        if(pageCount >= mPage){
                            sPageStart = sPageStart + 1;
                        }
                    }
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

    public void clear(){
        if(sList.size() != 0){
            sList.clear();
        }
    }

    public static boolean isFinish() {
        boolean result;
        if (sTotalCount == 0) {
            return true;
        }
        else {
            int pageCount = sTotalCount/PAGE_SIZE;
            if(pageCount >= mPage){
                result = false;
            }else{
                result = true;
            }
        }
        return  result;
    }

}
