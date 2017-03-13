package cn.com.jinke.assist.function.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseAdapter;
import cn.com.jinke.assist.function.model.Persion;

/**
 * Created by jinke on 2017/3/13.
 */

public class SearchAdapter extends ProjectBaseAdapter<Persion> {

    private Activity mContext;

    public SearchAdapter(Activity context){
        super(context);
        mContext = context;
    }

    private final class Holder{
        TextView name_textview;
        TextView sex_textview;
        TextView idCard_textview;
        TextView phone_textview;
        TextView address_textview;
    }

    @Override
    public View getViewEx(int position, View v, Persion persion) {
        Holder holder = null;
        if(v == null || v.getTag() == null){
            holder = new Holder();
            v = inflater.inflate(R.layout.item_search, null);
            holder.name_textview = (TextView)v.findViewById(R.id.name_tv);
            holder.sex_textview = (TextView)v.findViewById(R.id.sex_tv);
            holder.idCard_textview = (TextView)v.findViewById(R.id.idCard_tv);
            holder.phone_textview = (TextView)v.findViewById(R.id.phone_tv);
            holder.address_textview = (TextView)v.findViewById(R.id.address_tv);
            v.setTag(holder);
        }
        holder = (Holder) v.getTag();
        BindData(holder, persion);
        return v;
    }

    private void BindData(Holder holder, Persion persion){
        holder.name_textview.setText(persion.getName());
        holder.sex_textview.setText(persion.getGender());
        holder.idCard_textview.setText(persion.getIdcard());
        holder.phone_textview.setText(persion.getPhone());
        holder.address_textview.setText(persion.getAddress());
    }
}
