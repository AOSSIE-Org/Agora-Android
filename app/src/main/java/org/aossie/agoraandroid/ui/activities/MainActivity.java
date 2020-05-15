package org.aossie.agoraandroid.ui.activities;

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

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final SharedPrefs sharedPrefs = new SharedPrefs(MainActivity.this);
        String userName = sharedPrefs.getUserName();
        String password = sharedPrefs.getPass();
        if (userName == null || password == null) {
          startActivity(new Intent(MainActivity.this, HomeLoginActivity.class));
          finish();
        } else {
          startActivity(new Intent(MainActivity.this, HomeActivity.class));
          finish();
        }
  }
}
