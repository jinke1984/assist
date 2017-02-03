package cn.com.jinke.assist.customview;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.manager.adapter.DialogAdapter;
import cn.com.jinke.assist.utils.ActivityHelper;


/**
 * 带有列表的对话框
 * @author apple
 *
 */

public class AppListDialog extends Dialog implements AdapterView.OnItemClickListener{

	private Activity mActivity;
	private AdapterView.OnItemClickListener mListener;
	private ListView mListView;
	private TextView mTitleTV;
	
	public AppListDialog(Activity aActivity, String aString, ArrayList<String> aItems, boolean isCancel) {
		super(aActivity, R.style.DimDialogStyle);
		setContentView(R.layout.dialog_app_list);
		mActivity = aActivity;
		setCanceledOnTouchOutside(isCancel);
		setCancelable(isCancel);
		mTitleTV = (TextView)findViewById(R.id.dialog_list_title);
		mListView = (ListView)findViewById(R.id.dialog_list_listview);
		mTitleTV.setText(aString);
		DialogAdapter adapter = new DialogAdapter(mActivity);
		mListView.setAdapter(adapter);
		adapter.setData(aItems);
		adapter.notifyDataSetChanged();
		mListView.setOnItemClickListener(this);
	}
	
	public void setOnItemClickListener( AdapterView.OnItemClickListener listener){
		this.mListener = listener;
	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		if (ActivityHelper.isActivityRunning(mActivity)) {
			super.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if(mListener != null){
			mListener.onItemClick(parent, view, position, id);
		}
		dismiss();
	}
	
	

}
