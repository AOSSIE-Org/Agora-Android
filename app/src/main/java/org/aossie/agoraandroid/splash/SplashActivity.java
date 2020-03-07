package org.aossie.agoraandroid.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.homelogin.HomeLoginActivity;
import org.aossie.agoraandroid.utilities.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    final SharedPrefs sharedPrefs = new SharedPrefs(SplashActivity.this);
    // Rotation And Fade Out Animation

    final ImageView rotatingAgora = findViewById(R.id.main_logo_iv);
    final Animation rotateAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
    final Animation fadeOutAnimation =
        AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

    rotatingAgora.startAnimation(rotateAnimation);
    rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {
        rotatingAgora.startAnimation(fadeOutAnimation);
        String userName = sharedPrefs.getUserName();
        String password = sharedPrefs.getPass();
        if (userName == null || password == null) {
          startActivity(new Intent(SplashActivity.this, HomeLoginActivity.class));
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
