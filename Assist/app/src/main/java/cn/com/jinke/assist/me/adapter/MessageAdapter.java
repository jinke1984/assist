package cn.com.jinke.assist.me.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseAdapter;
import cn.com.jinke.assist.function.model.Zcfg;

/**
 * Created by jinke on 2017/3/15.
 */

public class MessageAdapter extends ProjectBaseAdapter<Zcfg> {

    public MessageAdapter(Activity context){
        super(context);
    }

    private final class Holder{
        TextView filename_textview;
    }

    @Override
    public View getViewEx(int position, View v, Zcfg zcfg) {
        Holder holder = null;
        if(v == null || v.getTag() == null){
            holder = new Holder();
            v = inflater.inflate(R.layout.item_main, null);
            holder.filename_textview = (TextView)v.findViewById(R.id.content_tv);
            v.setTag(holder);
        }
        holder = (Holder) v.getTag();
        bindData(holder, zcfg);
        return v;
    }

    private void bindData(Holder aHolder, Zcfg aZcfg){
        aHolder.filename_textview.setText(aZcfg.getInfoname());
    }
}
