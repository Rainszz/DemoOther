package com.yhl.demoother;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.yhl.demoother.bean.Account;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.manager.PreferencesManager;
import com.yhl.demoother.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.OkHttpClient;


public class BaseApplication extends Application {
    private static BaseApplication instance = null;


    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = (BaseApplication) getApplicationContext();
        initOKHttp();
        initBmob();
        initShareSdk();
        initData();

    }

    private void initData() {
//TODO 初始化数据
        //TODO 登录状态
        //TODO 已经登录的话自动完成登录，（重新请求个人信息数据，并获取sessionid，保存在cookie中，）
        if(PreferencesManager.getInstance(getApplicationContext()).get(Common.IS_LOGIN,false)){
            String userPhoto = PreferencesManager.getInstance(getApplicationContext()).get(Common.USER_PHOTO);
            String userName = PreferencesManager.getInstance(getApplicationContext()).get(Common.USER_NAME);
            String userPwd = PreferencesManager.getInstance(getApplicationContext()).get(Common.USER_PWD);
            BmobUser.BmobThirdUserAuth authInfo = (BmobUser.BmobThirdUserAuth) PreferencesManager.getInstance(getApplicationContext()).get(BmobUser.BmobThirdUserAuth.class);
            int loginType = PreferencesManager.getInstance(getApplicationContext()).get(Common.LOGINTYPE,0);
            switch (loginType){
                case Common.LOGIN_TYPE_NORMAL:
                    loginByUser(userName,userPwd);
                    break;
                case Common.LOGIN_TYPE_THIRD:
                    loginByThird(authInfo);
                    break;
                default:
                    ToastUtils.shortToast(getApplicationContext(),getString(R.string.auto_login_failed));
                    break;
            }
        }
    }
    private void loginByThird(BmobUser.BmobThirdUserAuth authInfo) {
        BmobUser.loginWithAuthData(getApplicationContext(), authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.shortToast(getApplicationContext(), getString(R.string.auto_login_third_failed) + msg);
            }
        });
    }

    private void loginByUser(String userName, final String userPwd) {
        //使用BmobSDK提供的登录功能
        Account user = new Account();
        user.setUsername(userName);
        user.setPassword(userPwd);
        user.login(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.shortToast(getApplicationContext(), s);
            }
        });
    }

    public static synchronized BaseApplication getInstance(){
        return instance;
    }

    //Bmob  连接Application
    private void initBmob() {
        Bmob.initialize(this, "f6b7054d087cb06126efcb4c3a0b5eac");
    }

    private void initShareSdk() {
        ShareSDK.initSDK(this);
    }

    private void initOKHttp() {
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("HTTP"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base); MultiDex.install(this);
    }
}
