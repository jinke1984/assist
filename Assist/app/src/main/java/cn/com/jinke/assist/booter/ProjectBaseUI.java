package cn.com.jinke.assist.booter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.x;

import java.util.HashSet;
import java.util.Set;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.utils.CodeConstants;
import cn.com.jinke.assist.utils.JsonConstans;
import cn.com.jinke.assist.utils.MessageProxy;
import cn.com.jinke.assist.utils.MsgKey;
import cn.com.jinke.assist.utils.NetworkHelper;
import cn.com.jinke.assist.utils.UrlSetting;
import cn.com.jinke.assist.utils.VersionHelper;


/**
 * Created by jinke on 16/8/1.
 *
 */
public class ProjectBaseUI extends Activity implements JsonConstans, MsgKey, CodeConstants, UrlSetting {

    private Header header;
    private ViewGroup mRootView;
    private ViewGroup mContentView;

    private ProjectLoadDialog loading;

    protected Context mContext;
    private boolean mIsVisible;
    private String mClassName;
    private final int CONS_REFRESH = -20000;

    private Set<Integer> mMessageSet = new HashSet<>();
    protected Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_ALL_ACTIVITY:
                    finish();
                    break;
            }
            return ProjectBaseUI.this.handleMessage(msg);
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerMessages(FINISH_ALL_ACTIVITY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        super.setContentView(R.layout.ui_base);
        onPreIinitialized(savedInstanceState);//2016.12.9 图片上传加上的，
        mRootView = (ViewGroup) findViewById(R.id.root_view);
        mContentView = (ViewGroup) findViewById(R.id.content_view);
        ProjectApplication.setCurrentActivity(this);
        mContext = this;
    }
    protected void onPreIinitialized(Bundle savedInstanceState) {
    }
    @Override
    public void setContentView(int layoutResID) {
        onPreInitView();
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        mContentView.addView(view);
        x.view().inject(this, view);
        initHeader();
        onInitView();
        onInitData();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        onPreInitView();
        mContentView.addView(view, params);
        x.view().inject(this, view);
        initHeader();
        onInitView();
        onInitData();
    }

    @Override
    public void setContentView(View view) {
        onPreInitView();
        mContentView.addView(view);
        x.view().inject(this, view);
        initHeader();
        onInitView();
        onInitData();
    }

    protected ViewGroup getRootView() {
        return mRootView;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsVisible = true;
        ProjectApplication.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsVisible = false;
        if (isFinishing()) {
            ProjectApplication.setCurrentActivity(null);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getHandler().removeCallbacksAndMessages(null);
        unregisterMessages(mMessageSet);
        System.gc();
    }

    protected boolean handleMessage(Message msg) {
        return true;
    }


    /**
     * 注册消息
     * @param msgs 待注册的消息数组
     */
    protected final void registerMessages(int... msgs) {
        if (msgs != null) {
            for (int msg : msgs) {
                if (!mMessageSet.contains(Integer.valueOf(msg))) {
                    mMessageSet.add(msg);
                    MessageProxy.register(msg, getHandler());
                }
            }
        }
    }

    /**
     * 反注册消息
     * @param msgs 已注册的消息数组
     */
    protected final void unregisterMessages(int... msgs) {
        if (msgs != null) {
            for (int msg : msgs) {
                mMessageSet.remove(Integer.valueOf(msg));
                MessageProxy.unregister(msg, getHandler());
            }
        }
    }

    /**
     * 反注册消息
     * @param msgs 已注册的消息集合
     */
    private final void unregisterMessages(Set<Integer> msgs) {
        if (msgs != null && msgs.size() > 0) {
            Integer[] array = msgs.toArray(new Integer[msgs.size()]);
            for (Integer msg : array) {
                mMessageSet.remove(msg);
                MessageProxy.unregister(msg, getHandler());
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在Android 4.4以上启用Translucent Status Bar
     */
    @TargetApi(19)
    protected final boolean useTranslucentStatusBar() {
        if (VersionHelper.hasKitKat()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 在Android 4.4以上启用Translucent Navigation Bar
     */
    @TargetApi(19)
    protected final void useTranslucentNavigationBar() {
        if (VersionHelper.hasKitKat()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 在Android 4.4以上启用黏性沉浸模式
     */
    @TargetApi(19)
    protected final boolean useStickyImmersiveMode() {
        if (VersionHelper.hasKitKat()) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * UI控件初始化之前回调
     */
    protected void onPreInitView() {
        // 空方法，子类自行覆盖重写该方法
    }

    /**
     * UI控件初始化时回调，findViewById，setOnXXXXListener等方法都推荐写到该方法中
     */
    protected void onInitView() {
        // 空方法，子类自行覆盖重写该方法
    }

    /**
     * onInitView之后回调，数据相关的操作推荐在此回调中执行。
     */
    protected void onInitData() {
        // 空方法，子类自行覆盖重写该方法
    }

    public Context getContext() {
        return mContext;
    }

    public final Handler getHandler() {
        return mHandler;
    }


    /**
     * 获取当前Activity名字
     * @return 当前Activity的名字
     */
    public String getClassName() {
        if (TextUtils.isEmpty(mClassName)) {
            mClassName = getClass().getSimpleName();
        }
        return mClassName;
    }

    /**
     * 当前Activity是否用户可见
     * @return true：用户可见，false：用户不可见
     */
    protected final boolean isVisible() {
        return mIsVisible;
    }

    /**
     * 弹出Toast（允许在非UI线程操作）
     * @param resId 字符串资源id
     */
    public final void showToast(int resId) {
        ProjectApplication.showToast(resId);
    }
    public final void showToast(CharSequence text) {
        ProjectApplication.showToast(text);
    }
    /**
     * 弹出Toast（允许在非UI线程操作）
     * @param aString 描述的文字信息
     */
    public final void showToast(String aString) {
        ProjectApplication.showToast(aString);
    }

    /**
     * 判断是否有网络连接
     */
    public final boolean isNetworkConnected() {
        return NetworkHelper.isConnected(this);
    }

    public final class Header {
        public RelativeLayout leftLayout, titleLayout, rightLayout;
        public ImageButton leftImageBtn, rightImageBtn;
        public TextView leftText, titleText, rightText;
        public TextView lineLeft, lineRight;
        public ImageView centerIV;
    }

    protected Header getHeader() {
        return header;
    }

    private void initHeader() {
        try {
            if (header == null) {
                header = new Header();
                header.centerIV = (ImageView)findViewById(R.id.qiandw_system_header_center_imageview);
                header.leftLayout = (RelativeLayout) findViewById(R.id.qiandw_system_header_left_layout);
                header.titleLayout = (RelativeLayout) findViewById(R.id.qiandw_system_header_center_layout);
                header.rightLayout = (RelativeLayout) findViewById(R.id.qiandw_system_header_right_layout);
                header.leftImageBtn = (ImageButton) findViewById(R.id.qiandw_system_header_left_imagebtn);
                header.rightImageBtn = (ImageButton) findViewById(R.id.qiandw_system_header_right_imagebtn);
                header.leftText = (TextView) findViewById(R.id.qiandw_system_header_left_text);
                header.titleText = (TextView) findViewById(R.id.qiandw_system_header_center_text);
                header.rightText = (TextView) findViewById(R.id.qiandw_system_header_right_text);
                header.lineLeft = (TextView) findViewById(R.id.qiandw_all_new_refresh_left);
                header.lineRight = (TextView) findViewById(R.id.qiandw_all_new_refresh_right);
                header.leftLayout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ProjectBaseUI.this.finish();
                    }
                });
                header.leftImageBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        ProjectBaseUI.this.finish();
                    }
                });
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    protected void showLoading() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (loading == null && !ProjectBaseUI.this.isFinishing()) {
                    loading = new ProjectLoadDialog(ProjectBaseUI.this);
                    loading.setCanceledOnTouchOutside(false);
                }
                loading.show();
            }
        });
    }

    protected void dismissLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshEnd();
                if (loading != null && loading.isShowing()  && !ProjectBaseUI.this.isFinishing()) {
                    loading.dismiss();
                }
            }
        });
    }

    protected void refreshEnd() {
        if (getHeader().lineLeft != null) {
            int oldWidthL = getHeader().lineLeft.getLayoutParams().width;
            if (oldWidthL > 0) {
                mHandler.removeMessages(CONS_REFRESH);
                Message msg = new Message();
                msg.what = CONS_REFRESH;
                msg.arg1 = 1;
                mHandler.sendMessageDelayed(msg, 5);
            } else {
                LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                getHeader().lineLeft.setLayoutParams(leftParams);
                getHeader().lineRight.setLayoutParams(rightParams);
                getHeader().lineLeft.setVisibility(View.GONE);
                getHeader().lineRight.setVisibility(View.GONE);
            }
        }
    }
}
