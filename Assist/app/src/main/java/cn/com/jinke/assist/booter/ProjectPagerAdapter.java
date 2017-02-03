package cn.com.jinke.assist.booter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

/**
 * @author zhaojian
 * @time 下午5:26:13
 * @date 2014-10-28
 * @class_name QDWPagerAdapter.java
 */
public abstract class ProjectPagerAdapter<T> extends PagerAdapter
{

	protected ArrayList<T> allItem = new ArrayList<T>();
	protected LayoutInflater inflater;
	protected Context mContext;
	protected int mChildCount = 0;

	public void setData(ArrayList<T> list)
	{
		allItem = list;
	}

	public ArrayList<T> getData()
	{
		return allItem;
	}

	public ProjectPagerAdapter(Context context)
	{
		mContext = context;
		inflater = (LayoutInflater) ProjectApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ProjectPagerAdapter(Context context, ArrayList<T> list)
	{
		mContext = context;
		inflater = (LayoutInflater) ProjectApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		allItem = list;
	}

	@Override
	public void destroyItem(View container, int position, Object object)
	{
		((ViewPager) container).removeView((View) object);
		if (allItem.size() != 0 && position < allItem.size())
		{
			destoryView(container, position, allItem.get(position));
		}

	}

	@Override
	public Object instantiateItem(View container, int position)
	{
		View view = getView(container, position, allItem.get(position));
		((ViewPager) container).addView(view);
		return view;
	}

	@Override
	public int getItemPosition(Object object)
	{
		if (mChildCount > 0)
		{
			mChildCount--;
			return POSITION_NONE;
		}
		return POSITION_NONE;

	}

	@Override
	public int getCount()
	{
		return allItem.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == arg1;
	}

	abstract public View getView(View container, int position, T t);

	abstract public void destoryView(View container, int position, T t);

}
