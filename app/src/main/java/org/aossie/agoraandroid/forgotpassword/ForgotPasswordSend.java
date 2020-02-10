package org.aossie.agoraandroid.forgotpassword;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.aossie.agoraandroid.R;

public class ForgotPasswordSend extends AppCompatActivity {

    private ForgotPasswordViewModel forgotPasswordViewModel;
    private TextInputLayout mUserNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_send);
        forgotPasswordViewModel = new ForgotPasswordViewModel(getApplication(), this);
        Button sendLinkButton = findViewById(R.id.button_send_link);
        mUserNameEditText = findViewById(R.id.edit_text_user_name);
        sendLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEditText.getEditText().getText().toString().trim();
                if (userName.isEmpty()) {
                    mUserNameEditText.setError(getResources().getString(R.string.user_name_not_entered_error));
                } else {
                    forgotPasswordViewModel.sendForgotPassLink(userName);
                }
            }
        });

    }
}
