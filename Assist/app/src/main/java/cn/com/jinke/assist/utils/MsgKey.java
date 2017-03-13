package cn.com.jinke.assist.utils;

/**
 * 消息常量
 * Created by jinke on 16/8/1.
 */
public interface MsgKey {

    public final int BASE = 100000;

    //-------------------公共模块---------------------
    public final int COMMON_BASE = BASE + 1000;
    public final int FINISH_ALL_ACTIVITY = COMMON_BASE + 1;
    public final int ACCESS_NET_FAILED = COMMON_BASE + 2;

    //----------------- 登录消息 ---------------------
    public final int LOGIN_BASE = BASE + 2000;
    public final int LOGIN_MSG = LOGIN_BASE + 1;


    //----------------- 跟踪服务 ---------------------
    public final int GZFW_BASE = BASE + 3000;
    public final int GZFW_MSG = GZFW_BASE + 1;
    public final int GZFW_UPLOAD = GZFW_BASE + 2;
    public final int GZFW_REFRESH = GZFW_BASE + 3;

    //----------------- 政策法规 ---------------------
    public final int ZCFG_BASE = BASE + 4000;
    public final int ZCFG_MSG = ZCFG_BASE + 1;

    //----------------- 查找人 -----------------------
    public final int PERSION_BASE = BASE + 5000;
    public final int PERSION_MSG = PERSION_BASE + 1;
}
