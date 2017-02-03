package cn.com.jinke.assist.booter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import org.xutils.x;

import java.lang.ref.WeakReference;

import cn.com.jinke.assist.thread.Dispatcher;


/**
 * Created by jinke on 16/8/1.
 */
public class ProjectApplication extends Application{

    private static Context mContext;
    private static WeakReference<Activity> mCurrentActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        Dispatcher.init(Thread.currentThread());
        //Application初始化
        mContext = this;
        x.Ext.init((Application)mContext);
        x.Ext.setDebug(true); // 是否输出debug日志

        int pid = android.os.Process.myPid();
        //log("App onCreate()---> myPid: " + pid + "  appId: " + BuildConfig.APPLICATION_ID);
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                String processName = appProcess.processName;
                //log("App onCreate()---> processName: " + processName);
                if (processName.equals("cn.com.jinke.assist")) {
                    APPManager.initSync();
                    APPManager.initAsync();
                }
            }
        }

    }

    public static Context getContext(){
        return mContext;
    }

    public static void setCurrentActivity(Activity activity){
        if(activity == null){
            if (mCurrentActivity != null) {
                mCurrentActivity.clear();
                mCurrentActivity = null;
            }
        }else{
            if (mCurrentActivity != null) {
                Activity act = mCurrentActivity.get();
                if (act != null && act.hashCode() == activity.hashCode()) {
                    return;
                }
            }
            mCurrentActivity = new WeakReference<Activity>(activity);
        }
    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity != null ? mCurrentActivity.get() : null;
    }

    public static void showToast(int resId) {
        showToast(getContext().getString(resId));
    }

    public static void showToast(final CharSequence text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
