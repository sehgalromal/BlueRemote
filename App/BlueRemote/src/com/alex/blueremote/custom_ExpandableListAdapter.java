package com.alex.blueremote;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class custom_ExpandableListAdapter extends BaseExpandableListAdapter {

	private List<? extends Map> mGroupData;
    private int mExpandedGroupLayout;
    private int mCollapsedGroupLayout;
    private String[] mGroupFrom;
    private int[] mGroupTo;
    
    private List<? extends List> mChildData;
    private int mChildLayout;
    private int mLastChildLayout;
    private String[] mChildFrom;
    private int[] mChildTo;
    
    private LayoutInflater mInflater;
    
    public custom_ExpandableListAdapter(
    		Context context,
            
    		List<? extends Map> groupData, 
            int groupLayout,
            String[] groupFrom, 
            int[] groupTo,
            
            List<? extends List> childData,
            int childLayout,
            String[] childFrom,
            int[] childTo) 
    {
        this(context, groupData, groupLayout, groupLayout, groupFrom, groupTo, childData,
                childLayout, childLayout, childFrom, childTo);
    }

    public custom_ExpandableListAdapter(Context context,
            List<? extends Map> groupData, int expandedGroupLayout,
            int collapsedGroupLayout, String[] groupFrom, int[] groupTo,
            List<? extends List> childData,
            int childLayout, String[] childFrom, int[] childTo) {
        this(context, groupData, expandedGroupLayout, collapsedGroupLayout,
                groupFrom, groupTo, childData, childLayout, childLayout,
                childFrom, childTo);
    }

    public custom_ExpandableListAdapter(Context context,
            List<? extends Map> groupData, int expandedGroupLayout,
            int collapsedGroupLayout, String[] groupFrom, int[] groupTo,
            List<? extends List> childData,
            int childLayout, int lastChildLayout, String[] childFrom,
            int[] childTo) {
        mGroupData = groupData;
        mExpandedGroupLayout = expandedGroupLayout;
        mCollapsedGroupLayout = collapsedGroupLayout;
        mGroupFrom = groupFrom;
        mGroupTo = groupTo;
        
        mChildData = childData;
        mChildLayout = childLayout;
        mLastChildLayout = lastChildLayout;
        mChildFrom = childFrom;
        mChildTo = childTo;
        
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public Object getChild(int groupPosition, int childPosition) {
        return mChildData.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = newChildView(isLastChild, parent);
        } else {
            v = convertView;
        }
        bindView(v, (Map<String, ?>) mChildData.get(groupPosition).get(childPosition), mChildFrom, mChildTo);
        return v;
    }

    public View newChildView(boolean isLastChild, ViewGroup parent) {
        return mInflater.inflate((isLastChild) ? mLastChildLayout : mChildLayout, parent, false);
    }
    
    private void bindView(View view, Map<String, ?> data, String[] from, int[] to) {
        int len = to.length;

        for (int i = 0; i < len; i++) {
            TextView v = (TextView)view.findViewById(to[i]);
            if (v != null) {
                v.setText(data.get(from[i]).toString());
            }
        }
    }

    public int getChildrenCount(int groupPosition) {
        return mChildData.get(groupPosition).size();
    }

    public Object getGroup(int groupPosition) {
        return mGroupData.get(groupPosition);
    }

    public int getGroupCount() {
        return mGroupData.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = newGroupView(isExpanded, parent);
        } else {
            v = convertView;
        }
        bindView(v, mGroupData.get(groupPosition), mGroupFrom, mGroupTo);
        return v;
    }

    public View newGroupView(boolean isExpanded, ViewGroup parent) {
        return mInflater.inflate((isExpanded) ? mExpandedGroupLayout : mCollapsedGroupLayout, parent, false);
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}
