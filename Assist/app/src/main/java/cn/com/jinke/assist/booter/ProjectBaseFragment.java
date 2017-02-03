package cn.com.jinke.assist.booter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.utils.CodeConstants;
import cn.com.jinke.assist.utils.JsonConstans;
import cn.com.jinke.assist.utils.MessageProxy;
import cn.com.jinke.assist.utils.MsgKey;
import cn.com.jinke.assist.utils.NetworkHelper;


/**
 * Created by jinke on 16/8/9.
 */
public class ProjectBaseFragment extends Fragment implements JsonConstans, MsgKey, CodeConstants {

    private String mClassName;

    private Set<Integer> mMessageSet = new HashSet<>();
    private Handler mHandler = new InnerHandler(this);
    private boolean mIsExcludeStat;

    private Header header;
    private ProjectLoadDialog loading;

    private final int CONS_REFRESH = -20000;

    private static class InnerHandler extends Handler {
        WeakReference<ProjectBaseFragment> mFragment;

        InnerHandler(ProjectBaseFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 基类处理的消息放这里
            }
            ProjectBaseFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        parentView = createView(inflater, container, savedInstanceState);
//        initHeader();
//        initHeadView();
//        initData();
//        return parentView;
//    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        dismissLoading();
        getHandler().removeCallbacksAndMessages(null);
        unregisterMessages(mMessageSet);
        super.onDestroy();
    }

    /**
     * 设置是否排除当前页面的统计
     */
    protected void setExcludeStat(boolean isExclude) {
        mIsExcludeStat = isExclude;
    }

    protected boolean handleMessage(Message msg) {
        return true;
    }

    public final Handler getHandler() {
        return mHandler;
    }

    /**
     * ProjectBaseUI
     * @return ProjectBaseUI
     */
//    public final ProjectBaseUI getBaseActivity() {
//        return getActivity() instanceof ProjectBaseUI ? (ProjectBaseUI) getActivity() : null;
//    }

    public final CharSequence getTextEx(int resId) {
        return ProjectApplication.getContext().getText(resId);
    }

    public final String getStringEx(int resId) {
        return ProjectApplication.getContext().getString(resId);
    }

    /**
     * 注册消息
     *
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
     *
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
     *
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

    /**
     * 获取当前Fragment的名字
     *
     * @return 当前Fragment的名字
     */
    public String getClassName() {
        if (TextUtils.isEmpty(mClassName)) {
            mClassName = getClass().getSimpleName();
        }
        return mClassName;
    }


    /**
     * 弹出Toast（允许在非UI线程操作）
     * @param resId 字符串资源id
     */
    public final void showToast(int resId) {
        ProjectApplication.showToast(resId);
    }

    /**
     * 弹出Toast（允许在非UI线程操作）
     *
     * @param text 字符串
     */
    public final void showToast(CharSequence text) {
        ProjectApplication.showToast(text);
    }

    /**
     * 判断是否有网络连接
     * @return
     */
    public final boolean isNetworkConnected() {
        return NetworkHelper.isConnected(getActivity());
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

    protected void initHeader(View view){
        try{
            header = null;
            if (header == null){
                header = new Header();
                header.centerIV = (ImageView)view.findViewById(R.id.qiandw_system_header_center_imageview);
                header.leftLayout = (RelativeLayout) view.findViewById(R.id.qiandw_system_header_left_layout);
                header.titleLayout = (RelativeLayout) view.findViewById(R.id.qiandw_system_header_center_layout);
                header.rightLayout = (RelativeLayout) view.findViewById(R.id.qiandw_system_header_right_layout);
                header.leftImageBtn = (ImageButton) view.findViewById(R.id.qiandw_system_header_left_imagebtn);
                header.rightImageBtn = (ImageButton) view.findViewById(R.id.qiandw_system_header_right_imagebtn);
                header.leftText = (TextView) view.findViewById(R.id.qiandw_system_header_left_text);
                header.titleText = (TextView) view.findViewById(R.id.qiandw_system_header_center_text);
                header.rightText = (TextView) view.findViewById(R.id.qiandw_system_header_right_text);
                header.lineLeft = (TextView) view.findViewById(R.id.qiandw_all_new_refresh_left);
                header.lineRight = (TextView) view.findViewById(R.id.qiandw_all_new_refresh_right);
                // header.leftLayout.setOnClickListener(new OnClickListener() {
                //
                // @Override
                // public void onClick(View v) {
                // getActivity().finish();
                // }
                // });
            }
        }
        catch (Exception e){
            // TODO: handle exception
        }
    }

    protected void showLoading() {
        if(getActivity() != null){

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (loading == null && !getActivity().isFinishing()) {
                        loading = new ProjectLoadDialog(getActivity());
                    }
                    loading.show();
                }
            });
        }
    }

    protected void dismissLoading() {
        if(getActivity() != null){

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //refreshEnd();
                    if (loading != null && loading.isShowing() && !isHidden()) {
                        loading.dismiss();
                    }
                }
            });
        }
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
            }
            else {
                LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                getHeader().lineLeft.setLayoutParams(leftParams);
                getHeader().lineRight.setLayoutParams(rightParams);
                getHeader().lineLeft.setVisibility(View.GONE);
                getHeader().lineRight.setVisibility(View.GONE);
            }
        }
    }

//    abstract public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
//
//
//    //初始化控件
//    abstract public void initHeadView();
//
//    //初始化数据
//    abstract public void initData();
}
