package org.aossie.agoraandroid.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.login.LoginViewModel;
import org.aossie.agoraandroid.signup.SignUpActivity;
import org.aossie.agoraandroid.login.LoginActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginViewModel loginViewModel;
    private Button signIn,signUp, facebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (Button) findViewById(R.id.signin_btn);
        signUp = (Button) findViewById(R.id.signup_btn);

        loginViewModel = new LoginViewModel(getApplication(), this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        facebookLogin = findViewById(R.id.fb_login_btn);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends"));
            }
        });

       // signIn.setOnClickListener();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logInIntent);
//            Toast.makeText(MainActivity.this, "Button Clicked" , Toast.LENGTH_SHORT).show();
            }

        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Toast.makeText(MainActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
            } else {
                String facebookAccessToken = currentAccessToken.getToken();
                loginViewModel.facebookLogInRequest(facebookAccessToken);
            }
        }
    };

}
