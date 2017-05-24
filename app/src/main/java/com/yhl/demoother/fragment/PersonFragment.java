package com.yhl.demoother.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.yhl.demoother.R;
import com.yhl.demoother.activity.CollectionActivity;
import com.yhl.demoother.activity.LoginActivity;
import com.yhl.demoother.activity.PerfectActivity;
import com.yhl.demoother.activity.SettingActivity;
import com.yhl.demoother.common.BaseFragment;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.manager.PreferencesManager;
import com.yhl.demoother.utils.ImageLoader;
import com.yhl.demoother.utils.LoginUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by S01 on 2017/5/2.
 */

public class PersonFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.img_photo)
    RoundedImageView imgPhoto;
    @BindView(R.id.tv_log)
    TextView tvLog;
    @BindView(R.id.tv_sett)
    TextView tvSett;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_uname)
    TextView tvUname;
    @BindView(R.id.tv_coll)
    TextView tvColl;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.freagment_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();

        return view;
    }

    protected void initData() {
        if (PreferencesManager.getInstance(getActivity()).get(Common.IS_LOGIN, false)) {
            imgPhoto.setVisibility(View.VISIBLE);
            tvUname.setVisibility(View.VISIBLE);
            tvLog.setVisibility(View.GONE);
            loadUserInfo();
        } else {
            imgPhoto.setVisibility(View.GONE);
            tvUname.setVisibility(View.GONE);
            tvLog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView() {

    }

    //已经登录的话重新获取用户信息
    private void loadUserInfo() {
        String userPhoto = PreferencesManager.getInstance(getActivity()).get(Common.USER_PHOTO);
        String userName = PreferencesManager.getInstance(getActivity()).get(Common.USER_NAME);
        tvUname.setText(userName);
        ImageLoader.getInstance().displayImageTarget(imgPhoto, userPhoto);
    }


    @OnClick({R.id.img_photo, R.id.tv_log, R.id.tv_sett, R.id.tv_about, R.id.tv_coll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                //TODO 用户头像
                startActivity(new Intent(getActivity(), PerfectActivity.class));
                break;
            //TODO 登陆
            case R.id.tv_log:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            //TODO 设置
            case R.id.tv_sett:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            //TODO 收藏
            case R.id.tv_coll:
                //检查是否登陆
                LoginUtils.checkLogin(true);
                startActivity(new Intent(getActivity(), CollectionActivity.class));
                break;
            //TODO 关于
            case R.id.tv_about:
                ShowAbout();
                break;
        }
    }

    private void ShowAbout() {
        AlertDialog.Builder builer = new AlertDialog.Builder(getActivity())
                .setTitle("关于我们")
                .setMessage("开发人:Kissmzz\n地址:https://github.com/Kissmzz/DemoOther")
                .setPositiveButton("确定", null);
        builer.create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
