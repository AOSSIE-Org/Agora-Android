package org.aossie.agoraandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.main.MainActivity;
import org.aossie.agoraandroid.utilities.SharedPrefs;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final SharedPrefs sharedPrefs = new SharedPrefs(SplashScreenActivity.this);
        // Rotation And Fade Out Animation

        final ImageView rotatingAgora = findViewById(R.id.image_agora);
        final Animation rotateAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        rotatingAgora.startAnimation(rotateAnimation);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rotatingAgora.startAnimation(fadeOutAnimation);
                String userName = sharedPrefs.getUserName();
                if (userName == null) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();

                } else {
                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                    finish();


                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
