package com.yhl.demoother.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yhl.demoother.R;
import com.yhl.demoother.common.BaseActivity;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {
    private EditText et_Newname, et_Newpwd;
    private TextView btn_resg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setRightVisibility(View.GONE);
        setTitle("注册");
        findViews();
    }

    private void findViews() {
        et_Newname = (EditText) findViewById(R.id.et_Newname);
        et_Newpwd = (EditText) findViewById(R.id.et_Newpwd);
        btn_resg = (TextView) findViewById(R.id.btn_resg);
    }

    public void onRegist(View view) {
        switch (view.getId()) {
            case R.id.btn_resg:
                DDregist();
                break;
        }
    }

    private void DDregist() {
        //判断输入新用户
        String name = et_Newname.getText().toString();
        String pwd = et_Newpwd.getText().toString();

        BmobUser user = new BmobUser();
        user.setUsername(et_Newname.getText().toString());
        user.setPassword(et_Newpwd.getText().toString());

        if (name.equals("") || et_Newname.equals("")) {
            Toast.makeText(this, "帐号名或密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }else if (pwd.length() < 6) {
            Toast.makeText(this, "账号名密码不能少于6位", Toast.LENGTH_LONG).show();
            return;
        }else {
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                //关闭注册页面
                RegisterActivity.this.finish();
            }
            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegisterActivity.this,"账户密码已存在",Toast.LENGTH_SHORT).show();
            }
        });
        }

    }
}

