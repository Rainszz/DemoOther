package com.yhl.demoother.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.yhl.demoother.R;
import com.yhl.demoother.adapter.widget.MultiBaseAdapter;
import com.yhl.demoother.bean.Collection;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.utils.ImageLoader;
import com.yhl.demoother.utils.LogUtils;
import com.yhl.demoother.utils.LruCacheUtils;

import java.util.List;


public class CollectionAdapter extends MultiBaseAdapter<Collection> {


    public CollectionAdapter(Context context, List<Collection> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, Collection data, int viewType) {
        LogUtils.e("convert:"+viewType);
        switch (viewType){
            case Common.COLLECTION_TYPE_NEWS:
                loadNews(holder,data);
               break;
            case Common.COLLECTION_TYPE_JOKE:
                loadJoke(holder,data);
                break;
            case Common.COLLECTION_TYPE_PIC:
                loadPic(holder,data);
                break;
        }
    }

    private void loadPic(ViewHolder holder, Collection data) {
        holder.setText(R.id.tv_title,data.getTitle());
        String imgUrl = data.getPicUrl();
        //判断缓存中是否已经缓存过该图片，有则直接拿Bitmap，没有则直接调用Glide加载并缓存Bitmap
        Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(imgUrl);
        if (bitmap != null) {
            holder.setImageBitmap(R.id.img_pic,bitmap);
        } else {
            holder.setImageUrl(R.id.img_pic, new ViewHolder.ImageLoder(imgUrl) {
                @Override
                public void loadImage(ImageView imageView, String path) {
                    //封装的图片缓存加载类
                    ImageLoader.getInstance().displayImageTarget(imageView,path);
                }
            });
        }
    }

    private void loadJoke(ViewHolder holder, Collection data) {
        holder.setText(R.id.tv_joke,data.getTitle());
    }

    private void loadNews(ViewHolder holder, Collection data) {
        holder.setText(R.id.tv_title,data.getTitle());
        if(!TextUtils.isEmpty(data.getPicUrl())){
            String imgUrl = data.getPicUrl();
            //判断缓存中是否已经缓存过该图片，有则直接拿Bitmap，没有则直接调用Glide加载并缓存Bitmap
            Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(imgUrl);
            if (bitmap != null) {
                holder.setImageBitmap(R.id.img_news,bitmap);
            } else {
                holder.setImageUrl(R.id.img_news, new ViewHolder.ImageLoder(imgUrl) {
                    @Override
                    public void loadImage(ImageView imageView, String path) {
                        //封装的图片缓存加载类
                        ImageLoader.getInstance().displayImageTarget(imageView,path);
                    }
                });
            }
        }else{
            holder.setVisbilty(R.id.layout_news, View.GONE);
        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        LogUtils.e("getItemLayoutId:"+viewType);
        switch (viewType){
            case Common.COLLECTION_TYPE_NEWS:
                return R.layout.item_news;
            case Common.COLLECTION_TYPE_JOKE:
                return R.layout.item_joke;
            case Common.COLLECTION_TYPE_PIC:
                return R.layout.item_pic;
        }
        return 0;
    }

    @Override
    protected int getViewType(int position, Collection data) {
        LogUtils.e("getViewType:"+data.getType()+"|"+mDatas.get(position).getType());
        return mDatas.get(position).getType();
    }
}
