package com.yhl.demoother.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yhl.demoother.R;
import com.yhl.demoother.bean.JokeBean;


import java.util.List;

/**
 * Created by S01 on 2017/5/6.
 */

public class JokeAdapter extends BaseAdapter {
    private List<JokeBean.ResultBean.Joke> data;
    private viewHolder holder;
    public JokeAdapter(List<JokeBean.ResultBean.Joke>data){
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke, null);
            holder = new viewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
    }

        holder.tv_joke.setText(data.get(position).getContent());

        return convertView;
    }
    class viewHolder{
        TextView tv_joke;

        public viewHolder(View view) {
            this.tv_joke = (TextView) view.findViewById(R.id.tv_joke);
        }
    }
    //下拉刷新
    public void  setNewData(List<JokeBean.ResultBean.Joke> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public void setMoreData(List<JokeBean.ResultBean.Joke> newData){
        data.addAll(newData);
        notifyDataSetChanged();
    }
}
