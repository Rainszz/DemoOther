package com.yhl.demoother.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yhl.demoother.R;
import com.yhl.demoother.common.BaseActivity;
import com.yhl.demoother.common.Common;
import com.yhl.demoother.manager.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_sett)
    TextView tvSett;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settig);
        ButterKnife.bind(this);

        setRightVisibility(View.GONE);
        setTitle("设置");
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_sett:
                loginout();
                break;
        }
    }

    private void loginout() {
        BmobUser.logOut(SettingActivity.this);   //清除缓存用户对象
        PreferencesManager.getInstance(SettingActivity.this).put(Common.IS_LOGIN, false);
        SettingActivity.this.finish();
    }
}
