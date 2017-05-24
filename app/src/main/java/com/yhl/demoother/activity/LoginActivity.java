package com.yhl.demoother.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yhl.demoother.R;
import com.yhl.demoother.bean.Account;
import com.yhl.demoother.common.BaseActivity;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.manager.PreferencesManager;
import com.yhl.demoother.utils.LogUtils;
import com.yhl.demoother.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

public class LoginActivity extends BaseActivity {
    private EditText et_username, et_password;
    private TextView forget_password, tv_regist;
    private Button btn_login;
    private ImageButton btn_qq, btn_sina;
    private int rightVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRightVisibility(View.GONE);
        setTitle(getString(R.string.login));
        findViews();
    }


    private void findViews() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_qq = (ImageButton) findViewById(R.id.btn_qq);
        btn_sina = (ImageButton) findViewById(R.id.btn_sina);
    }

    public void onforget(View view) {
        switch (view.getId()) {
            //忘记密码
            case R.id.forget_password:
                break;
            //注册新用户
            case R.id.tv_regist:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_qq:
                loginByQQ();
                break;
            case R.id.btn_sina:
                loginBySina();
                break;
        }
    }


    private void loginBySina() {
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //输出所有授权信息
                PlatformDb data = platform.getDb();
                BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("weibo", data.getToken(), String.valueOf(data.getExpiresIn()), data.getUserId());
                loginWithAuth(authInfo, data);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        weibo.showUser(null);//授权并获取用户信息
    }

    private void loginByQQ() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //输出所有权信息
                PlatformDb data = platform.getDb();
                BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("qq", data.getToken(), String.valueOf(data.getExpiresIn()), data.getUserId());
                loginWithAuth(authInfo, data);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        qq.showUser(null);//授权并获取用户信息
    }

    public void loginWithAuth(final BmobUser.BmobThirdUserAuth authInfo, final PlatformDb data) {
        BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                LogUtils.i(authInfo.getSnsType() + "登陆成功返回:" + userAuth);
                Account user = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
                //更新登录的账户信息
                updateUserInfo(user, data, authInfo);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "第三方登录失败" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo(Account user, PlatformDb data, final BmobUser.BmobThirdUserAuth authInfo) {
        Account newUser = new Account();
        newUser.setPhoto(data.getUserIcon());
        newUser.setSex("男".equals(data.getUserGender()) ? true : false);
        newUser.setUsername(data.getUserName());
        Account bmobUser = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
        newUser.update(LoginActivity.this, bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "更新用户信息成功..", Toast.LENGTH_SHORT).show();
                //保存登录信息到本地
                saveUserInfo(Common.LOGIN_TYPE_THIRD, authInfo);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "更新用户信息成功.." + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        String name = et_username.getText().toString();
        String pwd = et_password.getText().toString();
        // 非空验证

        BmobUser bu2 = new BmobUser();
        bu2.setUsername(name);
        bu2.setPassword(pwd);
        if (name.equals("") || pwd.equals("")) {
            Toast.makeText(this, "账号名密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            bu2.login(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    ToastUtils.shortToast(LoginActivity.this, "登录成功！");
                    saveUserInfo(Common.LOGIN_TYPE_NORMAL, null);
                    //关闭登录页面
                    LoginActivity.this.finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(LoginActivity.this, "登陆失败用户名密码不正确", Toast.LENGTH_SHORT).show();
                    clearInput();
                }
            });
        }
    }

    private void saveUserInfo(int loginType, BmobUser.BmobThirdUserAuth authInfo) {
        /*
         * TODO 把用户的登录信息保存到本地：sp\sqlite：（登录状态，登录类别，登录账户信息）
         * 注意:为了保证数据安全，一般对数据进行加密
         * 通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
         * 如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
         * */
        Account user = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
        PreferencesManager preferences = PreferencesManager.getInstance(LoginActivity.this);
        preferences.put(Common.IS_LOGIN, true);
        preferences.put(Common.LOGINTYPE, loginType);
        preferences.put(Common.USER_NAME, user.getUsername());
        preferences.put(Common.USER_PHOTO, user.getPhoto());
        preferences.put(Common.USER_PWD, et_password.getText().toString());
        if (authInfo != null) {
            preferences.put(authInfo);
        }
        LoginActivity.this.finish();
    }

    private void clearInput() {
        et_username.setText("");
        et_password.setText("");
    }

}
