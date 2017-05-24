package com.yhl.demoother.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhl.demoother.BaseApplication;
import com.yhl.demoother.R;
import com.yhl.demoother.adapter.PicAdapter;
import com.yhl.demoother.bean.Account;
import com.yhl.demoother.bean.Collection;
import com.yhl.demoother.bean.Pic;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.common.SeverConfig;
import com.yhl.demoother.utils.LoginUtils;
import com.yhl.demoother.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;


public class PicFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private View view;
    private PicAdapter picAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Pic> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.freagment_pic, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sw_lot);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        picAdapter = new PicAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(picAdapter);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        OkHttp();
        onClick();
        super.onActivityCreated(savedInstanceState);
    }

    private void onClick() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttp();
                picAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.collection)
                        .setMessage("是否收藏？")
                        .setPositiveButton("收藏", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginUtils.checkLogin(true);
                                Account account = BmobUser.getCurrentUser(BaseApplication.getInstance(), Account.class);
                                if (account != null) {
                                    Collection collection = new Collection();
                                    collection.setuId(account.getObjectId());
                                    collection.setType(Common.COLLECTION_TYPE_PIC);
                                    collection.setTitle(mList.get(which).getTitle());
                                    collection.setPicUrl(mList.get(which).getPicUrl());
                                    collection.setUrl(mList.get(which).getUrl());
                                    saveCollectionData(collection);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });
    }

    private void saveCollectionData(Collection collection) {
        collection.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtils.shortToast(getActivity(), "收藏成功!");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.shortToast(getActivity(), "收藏失败!");
            }
        });
    }

    //获取异步请求
    private void OkHttp() {
        OkHttpUtils
                .get()
                .url(SeverConfig.PIC_URL)
                .addParams("key", Common.API_PIC_KEY)
                .addParams("num", "10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //解析数据
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("newslist");
                        mList.addAll(JSONArray.parseArray(jsonArray.toJSONString(), Pic.class));

                        picAdapter.notifyDataSetChanged();
                    }
                });
    }

}
