package cn.com.jinke.assist.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

/**
 * @Function
 * @author ZhangYun
 * @date 2013-6-7
 */
public abstract class ActivityBase extends Activity
{

	protected Context mContext;

	protected Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		mHandler = new Handler(new Callback()
		{
			@Override
			public boolean handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				return ActivityBase.this.handleMessage(msg);
			}
		});

		onPreIinitialized(savedInstanceState);
		onInitViews(savedInstanceState);
		onInitListeners(savedInstanceState);
		onPostIinitialized(savedInstanceState);
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void doResume()
	{
		onResume();
	}

	public void doPause()
	{
		onPause();
	}

	protected void onPreIinitialized(Bundle savedInstanceState)
	{
	}

	protected void onPostIinitialized(Bundle savedInstanceState)
	{
	}

	/**
	 * ��ʼ����ͼ
	 */
	protected abstract void onInitViews(Bundle savedInstanceState);

	/**
	 * ��ʼ��������
	 */
	protected abstract void onInitListeners(Bundle savedInstanceState);

	/**
	 * ������Ϣ
	 * 
	 * @param msg
	 * @return
	 */
	protected boolean handleMessage(Message msg)
	{
		return false;
	}

}
