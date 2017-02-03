package cn.com.jinke.assist.booter;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;

import java.util.ArrayList;

/**
 * @author zhaojian
 * @time 下午5:26:08
 * @date 2014-10-28
 * @class_name QDWExpandAdapter.java
 */
public abstract class ProjectExpandAdapter<P, C> extends DataSetObserver implements ExpandableListAdapter
{

	protected ArrayList<P> parentData = new ArrayList<P>();
	protected ArrayList<ArrayList<C>> childData = new ArrayList<ArrayList<C>>();
	private DataSetObservable dataSetObservable = new DataSetObservable();
	protected LayoutInflater inflater;

	public ProjectExpandAdapter()
	{
		inflater = (LayoutInflater) ProjectApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setParent(ArrayList<P> parent)
	{
		this.parentData = parent;
	}

	public void setChild(ArrayList<ArrayList<C>> child)
	{
		this.childData = child;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer)
	{
		dataSetObservable.registerObserver(this);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer)
	{
		dataSetObservable.unregisterObserver(this);
	}

	public void notifyDataSetInvalidated()
	{
		dataSetObservable.notifyInvalidated();
	}

	public void notifyDataSetChanged()
	{
		dataSetObservable.notifyChanged();
	}

	@Override
	public int getGroupCount()
	{
		// TODO Auto-generated method stub
		return parentData.size();
	}

	public boolean hasChild(int position)
	{
		return getChildrenCount(position) > 0 ? true : false;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		// TODO Auto-generated method stub
		return childData.get(groupPosition).size();
	}

	@Override
	public P getGroup(int groupPosition)
	{
		// TODO Auto-generated method stub
		return parentData.get(groupPosition);
	}

	@Override
	public C getChild(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return childData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return getParentView(groupPosition, isExpanded, convertView, getGroup(groupPosition));
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return getChildViewEx(groupPosition, childPosition, isLastChild, convertView, getChild(groupPosition, childPosition));
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGroupExpanded(int groupPosition)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupCollapsed(int groupPosition)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public long getCombinedChildId(long groupId, long childId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	abstract public View getParentView(int groupPosition, boolean isExpanded, View convertView, P p);

	abstract public View getChildViewEx(int groupPosition, int childPosition, boolean isLastChild, View convertView, C c);

	abstract public void selectGroup(int groupP);

	abstract public void selectChild(int groupP, int childP);
}
