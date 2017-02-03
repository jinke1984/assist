package cn.com.jinke.assist.function.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseAdapter;
import cn.com.jinke.assist.function.model.Gzfw;

/**
 * Created by jinke on 2017/1/20.
 */

public class GzfwAdapter extends ProjectBaseAdapter<Gzfw> {

    private Context mContext;

    public GzfwAdapter(Activity context){
        super(context);
        mContext = context;
    }

    private final class Holder{
        TextView jyqk_textview;
        TextView fwdx_textview;
        TextView gzqk_textview;
        TextView jbqk_textview;
        TextView gzry_textview;
        TextView dcsj_textview;
    }

    @Override
    public View getViewEx(int position, View v, Gzfw gzfw) {
        Holder holder = null;
        if(v == null || v.getTag() == null){
            holder = new Holder();
            v = inflater.inflate(R.layout.item_gzfw, null);
            holder.jyqk_textview = (TextView)v.findViewById(R.id.jyqk_tv);
            holder.fwdx_textview = (TextView)v.findViewById(R.id.fwdh_tv);
            holder.gzqk_textview = (TextView)v.findViewById(R.id.gzqk_tv);
            holder.jbqk_textview = (TextView)v.findViewById(R.id.jbqk_tv);
            holder.gzry_textview = (TextView)v.findViewById(R.id.gzry_tv);
            holder.dcsj_textview = (TextView)v.findViewById(R.id.dcsj_tv);
            v.setTag(holder);
        }
        holder = (Holder) v.getTag();
        bindData(holder, gzfw);
        return v;
    }

    private void bindData(Holder aHolder, Gzfw aGzfw){
        aHolder.jyqk_textview.setText(mContext.getString(R.string.jyqk)+aGzfw.getJobcircs());
        aHolder.fwdx_textview.setText(mContext.getString(R.string.fwdx)+aGzfw.getPhoneno());
        aHolder.gzqk_textview.setText(mContext.getString(R.string.gzqk)+aGzfw.getSrvcircs());
        aHolder.jbqk_textview.setText(mContext.getString(R.string.jyyz)+aGzfw.getHelpcircs());
        aHolder.gzry_textview.setText(mContext.getString(R.string.gzry)+aGzfw.getStaff());
        aHolder.dcsj_textview.setText(mContext.getString(R.string.dcsj)+aGzfw.getExaminetime());

    }
}
