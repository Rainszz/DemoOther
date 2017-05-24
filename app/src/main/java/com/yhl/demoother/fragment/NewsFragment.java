package com.yhl.demoother.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yhl.demoother.BaseApplication;
import com.yhl.demoother.R;
import com.yhl.demoother.adapter.NewsAdapter;
import com.yhl.demoother.activity.NewsDetailActivity;
import com.yhl.demoother.bean.Account;
import com.yhl.demoother.bean.Collection;
import com.yhl.demoother.bean.News;
import com.yhl.demoother.bean.Pic;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.common.SeverConfig;
import com.yhl.demoother.utils.LoginUtils;
import com.yhl.demoother.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;

/**
 * Created by S01 on 2017/5/2.
 */

public class NewsFragment extends Fragment {
    public static final int TYPE_REFRESH = 0X01;
    public static final int TYPE_LOADMORE = 0X02;
    private List<News> data = new ArrayList<>();
    private NewsAdapter newsAdapter;
    @BindView(R.id.lv_news)
    PullToRefreshListView lv_news;
    Unbinder unbinder;
    private View view;
    private static int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.freagment_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    private void initView() {
        //设置列表刷新加载
        lv_news.setMode(PullToRefreshBase.Mode.BOTH);
        //初始化适配器
        newsAdapter = new NewsAdapter(data, getActivity());
        //绑定适配器
        lv_news.setAdapter(newsAdapter);
        //添加监听事件
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("news", data.get(position - 1));
                intent.putExtras(bundle);
                NewsFragment.this.startActivity(intent);
            }
        });
        lv_news.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
                                    collection.setType(Common.COLLECTION_TYPE_NEWS);
                                    collection.setTitle(data.get(position).getTitle());
                                    collection.setUrl(data.get(position).getUrl());
                                    collection.setPicUrl(data.get(position).getThumbnail_pic_s());
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


        lv_news.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                page = 1;
                getAsyncData(page, TYPE_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                加载更多
//                getAsyncData(page++,TYPE_LOADMORE);

            }
        });

    }
    private void saveCollectionData(Collection collection) {
        collection.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtils.shortToast(getActivity(),"收藏成功!");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.shortToast(getActivity(),"收藏失败!");
            }
        });
    }

    private void initData() {
        getAsyncData(1, TYPE_REFRESH);
    }

    //获取异步请求的数值
    private void getAsyncData(int page, final int type) {
        OkHttpUtils
                .get()
                .url(SeverConfig.BASE_URL)
                .addParams("key", Common.API_NEWS_KEY)
                .addParams("type", "top")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                        lv_news.onRefreshComplete();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //停止刷新
                        lv_news.onRefreshComplete();
                        //解析数据
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("data");
                        switch (type) {
                            case TYPE_REFRESH:
                                newsAdapter.setNewData(JSONArray.parseArray(jsonArray.toJSONString(), News.class));
                                break;
                            case TYPE_LOADMORE:
//                                newsAdapter.setMoreData(JSONArray.parseArray(jsonObject.toJSONString(),News.class));
                                break;
                        }
                    }
                });

    }


    @Override

    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
