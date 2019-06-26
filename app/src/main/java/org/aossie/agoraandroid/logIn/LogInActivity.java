package org.aossie.agoraandroid.logIn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputLayout;
import org.aossie.agoraandroid.R;

@SuppressWarnings("ConstantConditions")
public class LogInActivity extends AppCompatActivity {
    private TextInputLayout mLoginUserName, mLoginPassword;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        loginViewModel = new LoginViewModel(getApplication(),this);


        mLoginPassword=findViewById(R.id.logInPassword);
        mLoginUserName=findViewById(R.id.logInUserName);
        Button mFinalLoginButton=findViewById(R.id.finalLogInButton);

        mFinalLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = mLoginUserName.getEditText().getText().toString();
                final String userPass = mLoginPassword.getEditText().getText().toString();

                if (userName.isEmpty())
                    mLoginUserName.setError("Please enter User Name");
                else mLoginUserName.setError(null);

                if (userPass.isEmpty()) {
                    mLoginPassword.setError("Please enter password");
                } else {
                    mLoginPassword.setError(null);
                    loginViewModel.logInRequest(userName, userPass);
                }
            }


        });

    }

}