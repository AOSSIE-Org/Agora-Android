package org.aossie.agoraandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.aossie.agoraandroid.databinding.ActivitySplashBinding;
import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.main.MainActivity;
import org.aossie.agoraandroid.utilities.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding activitySplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        final SharedPrefs sharedPrefs = new SharedPrefs(SplashActivity.this);
        // Rotation And Fade Out Animation

        final Animation rotateAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        activitySplashBinding.mainLogoIv.startAnimation(rotateAnimation);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                activitySplashBinding.mainLogoIv.startAnimation(fadeOutAnimation);
                String userName = sharedPrefs.getUserName();
                String password=sharedPrefs.getPass();
                if (userName == null || password==null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();

                } else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
