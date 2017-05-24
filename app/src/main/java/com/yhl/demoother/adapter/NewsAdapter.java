package com.yhl.demoother.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yhl.demoother.R;
import com.yhl.demoother.bean.News;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class NewsAdapter extends BaseAdapter {
    private viewHolder holder;
    private List<News> data;

    public NewsAdapter(List<News> data,Activity activity) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, null);
            holder = new viewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext())
                .load(data.get(position).getThumbnail_pic_s())
                .placeholder(R.mipmap.errora)
                .error(R.mipmap.errora)
                .into(holder.img_news);

        holder.tv_title.setText(data.get(position).getTitle());
        return convertView;
    }

    class viewHolder {
        ImageView img_news;
        TextView tv_title;

        public viewHolder(View convertView) {
            img_news = (ImageView) convertView.findViewById(R.id.img_news);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);

        }
    }
    //下拉刷新
    public void setNewData(List<News> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    //加载更多
    public void setMoreData(List<News> newData) {
        data.addAll(newData);
        notifyDataSetChanged();
    }
}
