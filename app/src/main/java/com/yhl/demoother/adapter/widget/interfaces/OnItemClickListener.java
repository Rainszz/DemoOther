package com.yhl.demoother.adapter.widget.interfaces;


import com.yhl.demoother.adapter.ViewHolder;

/**
 * Created by jayli on 2017/5/5 0005.
 */

public interface OnItemClickListener<T> {
    void onItemClick(ViewHolder viewHolder, T data, int position);
}
