package cn.com.jinke.assist.customview;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.utils.ActivityHelper;


/**
 * 具有一个按钮的自定义的对话框
 * @author jinke
 *
 */
public class AppOneDialog extends Dialog implements View.OnClickListener{
	
	private Activity mActivity;
	private View.OnClickListener mListener;
	private TextView mContent;
	private TextView mTitleTV;
	private TextView mOKBtn = null;
	
	/**
	 * 
	 * @param activity   上下文
	 * @param content    对话框显示的内容
	 * @param aString    对话框title显示的内容
	 * @param isCancel   是否点击消失
	 * @param aOK 按钮的描述文字
	 */
	public AppOneDialog(Activity activity, String content, String aString, boolean isCancel, String aOK){
		super(activity, R.style.DimDialogStyle);
		setContentView(R.layout.dialog_app_one);
		mActivity = activity;
		setCanceledOnTouchOutside(isCancel);
		setCancelable(isCancel);
		mTitleTV = (TextView)findViewById(R.id.dialog_one_tv);
		mContent = (TextView) findViewById(R.id.one_content);
		mContent.setText(content);
		mTitleTV.setText(aString);
		mOKBtn = (TextView)findViewById(R.id.one_ok);
		if(!TextUtils.isEmpty(aOK)){
			mOKBtn.setText(aOK);
		}
		mOKBtn.setOnClickListener(this);
	}
	
	public void setOnClickListener(View.OnClickListener listener) {
		this.mListener = listener;
	}
	
	@Override
	public void dismiss() {
		if (ActivityHelper.isActivityRunning(mActivity)) {
			super.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		if (mListener != null) {
			mListener.onClick(v);
		}
		dismiss();
	}
}
