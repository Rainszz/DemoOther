package com.yhl.demoother.adapter.widget.interfaces;


import com.yhl.demoother.adapter.ViewHolder;

public interface OnItemChildClickListener<T> {
    void onItemChildClick(ViewHolder viewHolder, T data, int position);
}
