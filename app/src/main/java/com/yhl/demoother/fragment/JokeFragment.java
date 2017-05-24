package com.yhl.demoother.fragment;

import android.content.DialogInterface;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yhl.demoother.BaseApplication;
import com.yhl.demoother.R;
import com.yhl.demoother.adapter.JokeAdapter;
import com.yhl.demoother.bean.Account;
import com.yhl.demoother.bean.Collection;
import com.yhl.demoother.bean.JokeBean;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.common.SeverConfig;
import com.yhl.demoother.utils.LoginUtils;
import com.yhl.demoother.utils.TimerUtils;
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

public class JokeFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.tv_tag)
    PullToRefreshListView tvTag;
    private View view;
    public static final int TYPE_REFRESH = 0X01;
    public static final int TYPE_LOADMORE = 0X02;
    private static int page = 1;
    private List<JokeBean.ResultBean.Joke> data = new ArrayList<>();
    private JokeAdapter jokeadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.freagment_joke, container, false);
        unbinder = ButterKnife.bind(this, view);

        initData();
        initView();
        return view;
    }

    private void initView() {
        //设置列表刷新加载
        tvTag.setMode(PullToRefreshBase.Mode.BOTH);
        //初始化适配器
        jokeadapter = new JokeAdapter(data);
        //绑定适配器
        tvTag.setAdapter(jokeadapter);
        //添加监听事件
        tvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), data.get(position - 1).getContent(), Toast.LENGTH_SHORT).show();
            }
        });
        tvTag.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.collection)
                        .setMessage("是否收藏？")
                        .setPositiveButton("收藏", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO 验证是否登录
                                LoginUtils.checkLogin(true);
                                Account account = BmobUser.getCurrentUser(BaseApplication.getInstance(),Account.class);
                                if(account != null){
                                    Collection collection = new Collection();
                                    collection.setuId(account.getObjectId());
                                    collection.setType(Common.COLLECTION_TYPE_JOKE);
                                    collection.setTitle(data.get(position-1).getContent());
                                    saveCollectionData(collection);
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();
                return false;
            }

        });
        tvTag.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新,先清空数据，在加载新数据,更新UI
                page = 1;
                getAsyncData(page, TYPE_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多：加载下一页数据,请求page++,新数据添加在后面
                getAsyncData(page++, TYPE_LOADMORE);
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


    //获取异步请求
    private void getAsyncData(int page, final int type) {
        OkHttpUtils
                .get()
                .url(SeverConfig.JOKE_URL)
                .addParams("sort", "desc")
                .addParams("page", String.valueOf(page))
                .addParams("pagesize", Common.PAGE_SIZE)
                .addParams("time", TimerUtils.getTime())
                .addParams("key", Common.API_JOKE_KEY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                        tvTag.onRefreshComplete();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tvTag.onRefreshComplete();
                        //解析数据
                        JokeBean jokeBean = JSON.parseObject(response, JokeBean.class);
                        switch (type) {
                            case TYPE_REFRESH:
                                jokeadapter.setNewData(jokeBean.getResult().getData());
                                break;
                            case TYPE_LOADMORE:
                                jokeadapter.setMoreData(jokeBean.getResult().getData());
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
