package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

/**
 * @Author: lufengwen
 * @Date: 2015年6月12日 上午8:53:57
 * @Description: 加载显示
 */
public class CenterProgressLayoutLayout extends LoadingLayout {

    public CenterProgressLayoutLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        
        mHeaderImage.setVisibility(View.GONE);
        mHeaderProgress.setVisibility(View.GONE);
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.ptr_default_flip_white;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        
    }

    @Override
    protected void pullToRefreshImpl() {
        //mHeaderCenterProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void refreshingImpl() {
        mHeaderCenterProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void releaseToRefreshImpl() {
        //mHeaderCenterProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void resetImpl() {
        mHeaderCenterProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void setIndeterminateDrawable(Drawable drawable) {
        
    }
}

