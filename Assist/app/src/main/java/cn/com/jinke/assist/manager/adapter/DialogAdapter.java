package cn.com.jinke.assist.manager.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseAdapter;


public class DialogAdapter extends ProjectBaseAdapter<String> {

	public DialogAdapter(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getViewEx(int position, View v, String t) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (v == null || v.getTag() == null){
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.item_dialog, null);
			holder.textview = (TextView)v.findViewById(R.id.item_textview);
			v.setTag(holder);
		}
		holder = (ViewHolder)v.getTag();
		holder.textview.setText(t);
		return v;
	}
	
	private final class ViewHolder{
		TextView textview;
	}

}
