package com.yhl.demoother.activity;

import android.animation.Animator;
import android.content.Intent;
import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.yhl.demoother.MainActivity;
import com.yhl.demoother.R;

import java.util.Timer;
import java.util.TimerTask;


public class WeclomeActivity extends AppCompatActivity implements Animator.AnimatorListener {
    private Animation toLargeAnimation;
    private Animation toSmallAnimation;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weclome);

        imageView = (ImageView) findViewById(R.id.imageView);
        toLargeAnimation = AnimationUtils.loadAnimation(this, R.anim.to_welcome);
        toSmallAnimation = AnimationUtils.loadAnimation(this, R.anim.to_welcome_small);
        imageView.startAnimation(toLargeAnimation);

        final Intent it = new Intent(this, MainActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
                finish();
            }
        };
        timer.schedule(task, 1000 * 3); //设置3秒后显示MainActivity
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (animation.hashCode() == toLargeAnimation.hashCode())
            imageView.startAnimation(toSmallAnimation);
        else
            imageView.startAnimation(toLargeAnimation);
    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
