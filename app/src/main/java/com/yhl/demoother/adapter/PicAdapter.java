package com.yhl.demoother.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yhl.demoother.R;
import com.yhl.demoother.adapter.widget.interfaces.OnItemClickListener;
import com.yhl.demoother.bean.Pic;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Kissmzz on 2017/5/7.
 */

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Pic> mList;

    public PicAdapter(Context context, List<Pic> list) {
        mList = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = mInflater.inflate(R.layout.item_pic, parent, false);
        PicAdapter.ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(PicAdapter.ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(mList.get(position).getPicUrl())
                .placeholder(R.mipmap.errora)
                .into(holder.img_pic);

        holder.tv_title.setText(mList.get(position).getTitle());
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_pic;
        TextView tv_title;
        public ViewHolder(View itemView) {
            super(itemView);
            img_pic = (ImageView) itemView.findViewById(R.id.img_pic);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    //下拉刷新
    public void setNewData(List<Pic> newData) {
        mList.clear();
        mList.addAll(newData);
        notifyDataSetChanged();
    }

    public void setMoreData(List<Pic> newData) {
        mList.addAll(newData);
        notifyDataSetChanged();
    }
}