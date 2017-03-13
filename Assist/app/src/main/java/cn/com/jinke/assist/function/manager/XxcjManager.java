package cn.com.jinke.assist.function.manager;

import org.xutils.http.RequestParams;

import cn.com.jinke.assist.utils.CodeConstants;
import cn.com.jinke.assist.utils.UrlSetting;

/**
 * Created by jinke on 2017/3/8.
 */

public class XxcjManager implements UrlSetting, CodeConstants {

    public static XxcjManager instance;

    public XxcjManager(){

    }

    public static XxcjManager getInstance(){
        if (instance == null) {
            synchronized (XxcjManager.class) {
                if (instance == null) {
                    instance = new XxcjManager();
                }
            }
        }
        return instance;
    }

    public String getXxcjUrl(String aUserId, int id){
        String url = "";
        RequestParams params = new RequestParams(COLLECT);
        params.addParameter(DATAID, id);
        params.addParameter(UUID, aUserId);
        url = params.toString();
        return url;
    }
}
