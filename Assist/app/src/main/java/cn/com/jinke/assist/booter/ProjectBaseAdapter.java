package cn.com.jinke.assist.booter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.jinke.assist.utils.JsonConstans;
import cn.com.jinke.assist.utils.MsgKey;


/**
 * Created by jinke on 16/8/1.
 */
public abstract class ProjectBaseAdapter<T> extends BaseAdapter implements JsonConstans, MsgKey {

    private List<T> allModel = new ArrayList<T>();
    protected LayoutInflater inflater;
    protected Activity mContext;


    public ProjectBaseAdapter(Activity context){
        mContext = context;
        inflater = (LayoutInflater) ProjectApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ProjectBaseAdapter(Activity context, List<T> list){
        mContext = context;
        allModel = list;
        inflater = (LayoutInflater) ProjectApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ProjectBaseAdapter<T> setData(List<T> list) {
        allModel = list;
        return this;
    }

    public List<T> getData() {
        return allModel;
    }

    @Override
    public int getCount() {
        return allModel == null ? 0 : allModel.size();
    }

    @Override
    public T getItem(int position) {
        return allModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewEx(position, convertView, getItem(position));
    }

    abstract public View getViewEx(int position, View v, T t);
}
