package cn.com.jinke.assist.webinterface.core;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

import cn.com.jinke.assist.utils.JsonConstans;


/**
 * Author: lufengwen
 * Date: 2016-04-09 10:10
 * Description: 通用解析器
 */
public class CommonParser implements ResponseParser {
    private String requestUri;

    @Override
    public void checkResponse(UriRequest uriRequest) throws Throwable {
        requestUri = uriRequest.getRequestUri();
    }

    @Override
    public Object parse(Type type, Class<?> resultClass, String jsonStr) throws Throwable {
        Log.d("response", "requestUri response: " + jsonStr);
        jsonStr = jsonStr.replaceAll("null", "\"\"");
        HttpResult result = new HttpResult();
        JSONObject jsonObj = new JSONObject(jsonStr);
        result.setRequestUri(requestUri);
        result.setState(jsonObj.optInt(JsonConstans.RES_CODE));
        result.setMsg(jsonObj.optString(JsonConstans.RES_MESSAGE));
        result.setExtraData(jsonObj.optInt(JsonConstans.RES_EXTRA_DATA));
        String data = jsonObj.optString(JsonConstans.RES_DATA);
        if (!TextUtils.isEmpty(data)) {
            if (data.equals("null") || data.equals("\"\"")) {
                result.setData("");
            }
            else {
                result.setData(data);
            }
        }
        return result;
    }
}
