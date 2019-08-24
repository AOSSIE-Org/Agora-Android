package org.aossie.agoraandroid.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.aossie.agoraandroid.R;
public class ForgotPasswordSend extends AppCompatActivity {

    private ForgotPasswordViewModel forgotPasswordViewModel;
    private TextInputLayout mUserNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_send);
        forgotPasswordViewModel = new ForgotPasswordViewModel(getApplication(),this);
        Button sendLinkButton = findViewById(R.id.button_send_link);
        mUserNameEditText = findViewById(R.id.edit_text_user_name);
        sendLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEditText.getEditText().getText().toString().trim();
                if (userName.isEmpty()) {
                    mUserNameEditText.setError("Please Enter User Name");
                } else {
                    forgotPasswordViewModel.sendForgotPassLink(userName);
                }
            }
        });

    }
}
