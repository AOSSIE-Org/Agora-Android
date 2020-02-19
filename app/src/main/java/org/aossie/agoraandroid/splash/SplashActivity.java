package org.aossie.agoraandroid.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.homelogin.HomeLoginActivity;
import org.aossie.agoraandroid.utilities.SharedPrefs;
import org.aossie.agoraandroid.utilities.TokenUpdateWorker;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {
  private String userName,password;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    final SharedPrefs sharedPrefs = new SharedPrefs(SplashActivity.this);
    userName = sharedPrefs.getUserName();
    password = sharedPrefs.getPass();

    //WorkManager

    Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // you can add as many constraints as you want
            .build();

    final PeriodicWorkRequest workRequest =
            new PeriodicWorkRequest.Builder(TokenUpdateWorker.class,1, TimeUnit.HOURS)
                    .addTag("JobTag")
                    .setConstraints(constraints)
                    .build();
    if(userName != null&&password != null)
    WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("JobTag",ExistingPeriodicWorkPolicy.REPLACE,workRequest);

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
