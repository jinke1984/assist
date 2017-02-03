package cn.com.jinke.assist.manager;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.View;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.jinke.assist.R;

/**
 * @Author: lufengwen
 * @Date: 2016年5月4日 上午2:12:29
 * @Description: 下拉刷新控件助手类
 */
public class PullToRefreshHelper {
    public static <T extends View> void initHeader(PullToRefreshBase<T> refreshView) {
        if (refreshView == null) {
            return;
        }
        ILoadingLayout layout = refreshView.getLoadingLayoutProxy(true, false);
        if (layout == null) {
            return;
        }
        
        Resources res = refreshView.getResources();
        layout.setPullLabel(res.getText(R.string.ptr_start_pull_label));
        layout.setRefreshingLabel(res.getText(R.string.ptr_start_refreshing_label));
        layout.setReleaseLabel(res.getText(R.string.ptr_start_release_label));
    }
    
    public static <T extends View> void initNullHeader(PullToRefreshBase<T> refreshView) {
        if (refreshView == null) {
            return;
        }
        ILoadingLayout layout = refreshView.getLoadingLayoutProxy(true, false);
        if (layout == null) {
            return;
        }
        
        layout.setPullLabel("");
        layout.setRefreshingLabel("");
        layout.setReleaseLabel("");
        
        layout.setLoadingDrawable(null);
        layout.setIndeterminateDrawable(null);
    }
    
    public static <T extends View> void initFooter(PullToRefreshBase<T> refreshView) {
        if (refreshView == null) {
            return;
        }
        ILoadingLayout layout = refreshView.getLoadingLayoutProxy(false, true);
        if (layout == null) {
            return;
        }
        
        Resources res = refreshView.getResources();
        layout.setPullLabel(res.getText(R.string.ptr_end_pull_label));
        layout.setRefreshingLabel(res.getText(R.string.ptr_end_refreshing_label));
        layout.setReleaseLabel(res.getText(R.string.ptr_end_release_label));
        //layout.setLoadingDrawable(null);
    }
    
    @SuppressWarnings("deprecation")
    @SuppressLint("SimpleDateFormat")
    public static String getLastUpdated(Resources res, Date date, boolean isRefresh) {
        if (isRefresh) {
            date = new Date();
        }
        if (date != null) {
            Date now = new Date();
            if (now.getDay() == date.getDay()) {
                String tips = res.getString(R.string.common_today);
                String time = new SimpleDateFormat("HH:mm").format(new Date());
                return tips + time;
            }
            else {
                return new SimpleDateFormat("MM-dd HH:mm").format(new Date());
            }
        }
        else {
            return null;
        }
    }
}