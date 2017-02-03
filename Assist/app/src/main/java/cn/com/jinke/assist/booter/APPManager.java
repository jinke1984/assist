package cn.com.jinke.assist.booter;

import android.app.Application;

import org.xutils.x;

import cn.com.jinke.assist.database.DbManager;
import cn.com.jinke.assist.manager.PackageHelper;
import cn.com.jinke.assist.manager.VersionManager;
import cn.com.jinke.assist.me.manager.MasterManager;
import cn.com.jinke.assist.thread.Dispatcher;


/**
 * Created by apple on 2016/11/28.
 */

public class APPManager {

    public static void initAsync() {
        Dispatcher.runOnSingleThread(new Runnable() {

            @Override
            public void run() {

            }
        });
    }

    public static void initSync(){

        VersionManager.setVersionCode(PackageHelper.getVersionCode(ProjectApplication.getContext()));
        VersionManager.setVersionName(PackageHelper.getVersionName(ProjectApplication.getContext()));

        //初始化数据库
        x.Ext.init((Application) ProjectApplication.getContext());
        x.Ext.setDebug(false); // 是否输出debug日志
        DbManager.initCommonDb();
        MasterManager.getInstance().init();
    }

//    private static void initUserData(){
//        DbManager.initUserDb();
//    }
}
