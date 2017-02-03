package cn.com.jinke.assist.booter;

import android.app.Dialog;
import android.content.Context;

import cn.com.jinke.assist.R;


/**
 * @author zhaojian
 * @time 下午4:26:02
 * @date 2014-12-8
 * @class_name QDWLoadDialog.java
 */
public class ProjectLoadDialog extends Dialog
{

	public ProjectLoadDialog(Context context)
	{
		super(context, R.style.prompt_dialog_style);
		setContentView(R.layout.qiandw_loading_dialog);
	}

}
