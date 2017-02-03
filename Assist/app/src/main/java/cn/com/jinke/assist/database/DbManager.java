package cn.com.jinke.assist.database;


import java.io.IOException;

import cn.com.jinke.assist.share.ShareUtils;
import cn.com.jinke.assist.share.ShareUtils.ESHARE;
import cn.com.jinke.assist.share.ShareUtils.ETYPE;
import cn.com.jinke.assist.utils.ShareConstant;

/**
 * @Author: lufengwen
 * @Date: 2016年3月30日 下午18:03:25
 * @Description: 数据库管理器
 */
public class DbManager implements ShareConstant {

    private final static String TAG = "database";

    public static final int VERSION = 2;
    public static final String DATABASE_NAME_COMMON = "eeout_common.sqlite";
    public static final String DATABASE_NAME_USER = "eeout_user.sqlite";

    private static CommonDb sCommonDb;
    private static UserDb sUserDb;

    private DbManager() {

    }

    public static void initCommonDb() {
        //log("init CommonDB---");
        sCommonDb = new CommonDb(DATABASE_NAME_COMMON, VERSION, sListener);
    }

    public static CommonDb getCommonDb() {
        return sCommonDb;
    }

    public static UserDb getUserDb() {
        return sUserDb;
    }

    public static void initUserDb() {
        String lastLoginId = ShareUtils.getInstance().get(ESHARE.SYS, LAST_LOGIN_ID, ETYPE.STRING);
        sUserDb = new UserDb(lastLoginId + "_" + DATABASE_NAME_USER, VERSION, sListener);
    }

    public static void closeUserDb() {
        if (sUserDb != null) {
            try {
                sUserDb.getDbUtils().close();
            }
            catch (IOException e) {
                //log("close user db error. " + e.toString());
                e.printStackTrace();
            }
            sUserDb = null;
        }
    }

    public static void closeMasterDb() {
        if (sUserDb != null) {
            try {
                sUserDb.getDbUtils().close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            sUserDb = null;
        }
    }

    public static void clearCache() {
        sCommonDb.clearCache();
    }

    private static org.xutils.DbManager.DbUpgradeListener sListener = new org.xutils.DbManager.DbUpgradeListener() {

        @Override
        public void onUpgrade(org.xutils.DbManager dbManager, int oldVersion, int newVersion) {
            //数据库的升级
//            if(oldVersion <  2){
//                try{
//                    dbManager.addColumn(UserCard.class, "path");
//                }catch(DbException e){
//                    e.printStackTrace();
//                }
//            }
        }
    };

//    public static void log(String msg) {
//        AppLogger.d(TAG, msg, false);
//    }

}
