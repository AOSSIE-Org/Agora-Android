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

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final SharedPrefs sharedPrefs = new SharedPrefs(SplashScreen.this);
        // Rotation And Fade Out Animation

        final ImageView rotatingAgora = findViewById(R.id.image_agora);
        final Animation animation1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation animation2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        rotatingAgora.startAnimation(animation1);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rotatingAgora.startAnimation(animation2);
                String userName = sharedPrefs.getUserName();
                if (userName.equals("0")) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();

                } else {
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    finish();


                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
