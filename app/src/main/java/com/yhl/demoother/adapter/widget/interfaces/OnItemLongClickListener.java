package com.yhl.demoother.adapter.widget.interfaces;


import com.yhl.demoother.adapter.ViewHolder;

public interface OnItemLongClickListener<T> {

    void onItemLongClick(ViewHolder viewHolder, T data, int position);

}
