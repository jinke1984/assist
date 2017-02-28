package cn.com.jinke.assist.utils;

/**
 * 这个接口是用来存放解析服务器返回数据字段
 * Created by apple on 16/8/1.
 */
public interface JsonConstans {


    //----------------  默认数据  -------------------
    public final static String RES_CODE = "result";
    public final static String RES_MESSAGE = "resultmsg";
    public final static String RES_DATA = "data";
    public final static String RES_EXTRA_DATA = "totalqty";
    public final static String PAGE_START = "start";
    public final static String PAGE_SIZE = "limit";
    public final static String LIST = "list";
    public final static String SUC =  "success";

    public final static String JSON = "json";
    public final static String RESULT = "result";
    public final static String RES_MSG =  "msg";

    //----------------- 登录需要的字段 ----------------
    public final static String USERID = "userid";
    public final static String USERNAME = "username";
    public final static String COMMUNITYID = "communityid";
    public final static String COMMUNITYNAME = "communityname";
    public final static String ROLE = "role";

}
