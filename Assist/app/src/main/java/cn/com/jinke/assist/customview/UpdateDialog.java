package cn.com.jinke.assist.customview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.manager.VersionManager;
import cn.com.jinke.assist.utils.MessageProxy;


/**
 * @Author: lufengwen
 * @Date: 2016年5月4日 下午2:09:17
 * @Description: 更新对话框
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {

	private Builder mBuilder;

	private TextView mMessage;
	private TextView mProgressText;
	private TextView mDownload;
	private View mClose;
	private View mLayoutDownload;
	private ProgressBar mProgressBar;
	private TextView mPause;

	private final static int[] mMsg = new int[] { VersionManager.UPDATE_DOWNLOAD_FILE_SIZE, VersionManager.UPDATE_CLOSE_DIALOG };

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case VersionManager.UPDATE_DOWNLOAD_FILE_SIZE:
				setProgress(msg.arg1);
				break;

			case VersionManager.UPDATE_CLOSE_DIALOG:
				dismiss();
				break;
			}
		};
	};

	public UpdateDialog(Context context, Builder builder) {
		super(context, R.style.DimDialogStyle);
		this.mBuilder = builder;
		initView();
	}

	public UpdateDialog(Context context, int theme, Builder builder) {
		super(context, theme);
		this.mBuilder = builder;
		initView();
	}

	private void initView() {
		setCancelable(mBuilder._mIsCancelable);
		View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_update_dialog, null, false);
		setContentView(view);
		
		mMessage = (TextView) findViewById(R.id.message);
		mProgressText = (TextView) findViewById(R.id.progress_text);
		mDownload = (TextView) findViewById(R.id.download);
		mLayoutDownload = (View) findViewById(R.id.layout_donwload);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mPause = (TextView) findViewById(R.id.pause);
		mClose = findViewById(R.id.close);
		
		if (mBuilder != null) {
			if (!TextUtils.isEmpty(mBuilder._mMessage)) {
				mMessage.setText(mBuilder._mMessage);
			}
		}
		
		if (mBuilder._mIsCancelable) {
			mClose.setVisibility(View.VISIBLE);
			setCanceledOnTouchOutside(true);
		}
		else {
			mClose.setVisibility(View.GONE);
			setCanceledOnTouchOutside(false);
		}

		mClose.setOnClickListener(this);
		findViewById(R.id.download).setOnClickListener(this);
		findViewById(R.id.pause).setOnClickListener(this);
		
		MessageProxy.register(mMsg, mHandler);

	}

	public void showDownloadView() {
		mDownload.setVisibility(View.GONE);
		mLayoutDownload.setVisibility(View.VISIBLE);
		mPause.setText("暂停下载");
		setProgress(0);
	}

	public void setProgress(int percent) {
		mProgressBar.setProgress(percent);
		mProgressText.setText(Html.fromHtml("已下载  <font color=\'#ff0000\'>" + percent + "%</font>"));
	}

	@Override
	public void dismiss() {
		super.dismiss();
		MessageProxy.unregister(VersionManager.UPDATE_DOWNLOAD_FILE_SIZE, mHandler);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.close) {
			dismiss();
			if (mBuilder._mCancelListener != null) {
				mBuilder._mCancelListener.onClick(v);
			}
		}
		else if (id == R.id.download) {
			showDownloadView();
			if (mBuilder._mDownloadListener != null) {
				mBuilder._mDownloadListener.onClick(v);
			}
		}
		else if (id == R.id.pause) {
			VersionManager.isPause = !VersionManager.isPause;
			if (!VersionManager.isPause) {
				mPause.setText("暂停下载");
				// mPause.setBackgroundResource(R.drawable.update_pause_btn_bg);
			}
			else {
				mPause.setText("继续下载");
				// mPause.setBackgroundResource(R.drawable.update_play_btn_bg);
			}
			if (mBuilder._mPauseListener != null) {
				mBuilder._mPauseListener.onClick(v);
			}
		}
	}

	public static class Builder {
		private Context _mContext;
		private String _mMessage;
		private View.OnClickListener _mCancelListener;
		private View.OnClickListener _mDownloadListener;
		private View.OnClickListener _mPauseListener;
		private boolean _mIsCancelable;

		public Builder(Context context) {
			this._mContext = context;
		}

		public Builder setMessage(String message) {
			this._mMessage = message;
			return this;
		}

		public Builder setCancelListener(View.OnClickListener listener) {
			this._mCancelListener = listener;
			return this;
		}

		public Builder setDownloadListener(View.OnClickListener listener) {
			this._mDownloadListener = listener;
			return this;
		}

		public Builder setPauseListener(View.OnClickListener listener) {
			this._mPauseListener = listener;
			return this;
		}
		
		public Builder setCancelable(boolean isCancelable) {
			this._mIsCancelable = isCancelable;
			return this;
		}

		public UpdateDialog create() {
			UpdateDialog dialog = new UpdateDialog(_mContext, this);
			return dialog;
		}

		public UpdateDialog create(int theme) {
			UpdateDialog dialog = new UpdateDialog(_mContext, theme, this);
			return dialog;
		}
	}

}
