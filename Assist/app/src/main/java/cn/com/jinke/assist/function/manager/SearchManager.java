package cn.com.jinke.assist.function.manager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.com.jinke.assist.function.model.Persion;
import cn.com.jinke.assist.function.model.Zcfg;
import cn.com.jinke.assist.utils.CodeConstants;
import cn.com.jinke.assist.utils.MD5Utils;
import cn.com.jinke.assist.utils.MessageProxy;
import cn.com.jinke.assist.utils.MsgKey;
import cn.com.jinke.assist.utils.UrlSetting;
import cn.com.jinke.assist.webinterface.core.CallbackWrapper;

/**
 * Created by jinke on 2017/3/13.
 */

public class SearchManager implements CodeConstants, MsgKey, UrlSetting {

    public static SearchManager instance;

    private List<Persion> sList = new ArrayList<>();
    private final static int FINAL_PAGE_SIZE = 10;    //服务器一次推送10条数据下来
    public static long sLastRefreshTime; // 上次刷新时间
    private static int sPageStart;
    private static int sTotalCount;
    private static int mPage = 0;

    public SearchManager(){

    }

    public static SearchManager getInstance(){
        if (instance == null) {
            synchronized (SearchManager.class) {
                if (instance == null) {
                    instance = new SearchManager();
                }
            }
        }
        return instance;
    }

    public List<Persion> getsList() {
        return sList;
    }

    public void setsList(List<Persion> sList) {
        this.sList = sList;
    }

    public void getPersionData(String aKeyword, boolean isRefresh)throws JSONException {

        if(isRefresh){
            sPageStart = 1;
            mPage = 0;
            clear();
            sLastRefreshTime = System.currentTimeMillis();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEYWORD, aKeyword);
        jsonObject.put(COMMUNITYID, 0);
        jsonObject.put(ENTITYTYPE, "");
        jsonObject.put(PAGESIZE, FINAL_PAGE_SIZE);
        jsonObject.put(PAGEINDEX, isRefresh ? 1 : sPageStart);
        String md5 = MD5Utils.getMD5(jsonObject.toString());
        RequestParams params = new RequestParams(BASURL);
        params.addParameter(COMMAND,GETPERSIONLIST);
        params.addParameter(DATA, jsonObject);
        params.addParameter(MD5, md5);
        x.http().post(params, new CallbackWrapper<List<Persion>>(PERSION_MSG, 2){

            @Override
            public void onSuccess(int state, String msg, List<Persion> object, int total) {
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
