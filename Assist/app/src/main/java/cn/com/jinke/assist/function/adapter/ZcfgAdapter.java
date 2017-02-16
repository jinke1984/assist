package cn.com.jinke.assist.function.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseAdapter;
import cn.com.jinke.assist.function.model.Zcfg;
import cn.com.jinke.assist.utils.StringUtils;

/**
 * Created by jinke on 2017/2/13.
 */

public class ZcfgAdapter extends ProjectBaseAdapter<Zcfg> {

    private Context mContext;

    public ZcfgAdapter(Activity context){
        super(context);
        mContext = context;
    }

    private final class Holder{
        TextView xxmc_tv;
        TextView zz_tv;
        TextView ly_tv;
        TextView fbsj_tv;
    }

    @Override
    public View getViewEx(int position, View v, Zcfg zcfg) {
        Holder holder = null;
        if(v == null || v.getTag() == null){
            holder = new Holder();
            v = inflater.inflate(R.layout.item_zcfg, null);
            holder.fbsj_tv = (TextView)v.findViewById(R.id.fbsj_tv);
            holder.ly_tv = (TextView)v.findViewById(R.id.ly_tv);
            holder.zz_tv = (TextView)v.findViewById(R.id.zz_tv);
            holder.xxmc_tv = (TextView)v.findViewById(R.id.xxmc_tv);
            v.setTag(holder);
        }
        holder = (Holder) v.getTag();
        bindData(holder, zcfg);
        return v;
    }

    private void bindData(Holder aHolder, Zcfg aZcfg){
        aHolder.fbsj_tv.setText(mContext.getString(R.string.fbsj)+ StringUtils.getNullString(mContext, aZcfg.getPubtime()));
        aHolder.ly_tv.setText(mContext.getString(R.string.ly)+ StringUtils.getNullString(mContext, aZcfg.getSource()));
        aHolder.zz_tv.setText(mContext.getString(R.string.zz)+ StringUtils.getNullString(mContext, aZcfg.getAuthor()));
        aHolder.xxmc_tv.setText(mContext.getString(R.string.xxmc)+ StringUtils.getNullString(mContext, aZcfg.getInfoname()));

    }
}
