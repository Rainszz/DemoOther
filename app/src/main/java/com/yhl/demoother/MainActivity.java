package com.yhl.demoother;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yhl.demoother.fragment.JokeFragment;
import com.yhl.demoother.fragment.NewsFragment;
import com.yhl.demoother.fragment.PersonFragment;
import com.yhl.demoother.fragment.PicFragment;
import com.yhl.demoother.utils.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;
    @BindView(R.id.container)
    LinearLayout container;

    private NewsFragment newsFragment;
    private JokeFragment jokeFragment;
    private PersonFragment personFragment;
    private PicFragment picFragment;


    private FragmentManager fragmentmanager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        //禁用标题栏动画
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //设置默认fragment
        fragmentmanager = getSupportFragmentManager();
        fragmentTransaction = fragmentmanager.beginTransaction();
        newsFragment = new NewsFragment();
        fragmentTransaction.add(R.id.content, newsFragment);
        fragmentTransaction.commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    newsFragment = new NewsFragment();
                    replace(newsFragment);
                    return true;
                case R.id.navigation_joke:
                    jokeFragment = new JokeFragment();
                    replace(jokeFragment);
                    return true;
                case R.id.navigation_pic:
                    picFragment = new PicFragment();
                    replace(picFragment);
                    return true;
                case R.id.navigation_person:
                    personFragment = new PersonFragment();
                    replace(personFragment);
                    return true;
            }
            return false;
        }

        private void replace(Fragment fragment) {
            fragmentTransaction = fragmentmanager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.commit();
        }

    };

}
