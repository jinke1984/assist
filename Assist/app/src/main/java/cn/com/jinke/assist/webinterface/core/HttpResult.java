package cn.com.jinke.assist.webinterface.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

import cn.com.jinke.assist.utils.JsonConstans;


/**
 * Author: lufengwen
 * Date: 2016-04-09 10:09
 * Description: Http请求结果
 */
@HttpResponse(parser = CommonParser.class)
public class HttpResult implements Serializable {
    @Expose
    @SerializedName(JsonConstans.RES_CODE)
    private int state;

    @Expose
    @SerializedName(JsonConstans.RES_MESSAGE)
    private String msg;

    @SerializedName(JsonConstans.RES_EXTRA_DATA)
    private int extraData;

    @Expose
    @SerializedName(JsonConstans.RES_DATA)
    private String data;

    private String requestUri;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public int getExtraData() {
        return extraData;
    }

    public void setExtraData(int extraData) {
        this.extraData = extraData;
    }

    @Override
    public String toString() {
        return "state: " + state + "  msg: " + msg + "  data: " + this.data;
    }
}
