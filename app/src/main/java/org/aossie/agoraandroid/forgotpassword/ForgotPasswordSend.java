package org.aossie.agoraandroid.forgotpassword;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.google.android.material.textfield.TextInputLayout;
import net.steamcrafted.loadtoast.LoadToast;
import org.aossie.agoraandroid.R;

public class ForgotPasswordSend extends AppCompatActivity {

  private ForgotPasswordViewModel forgotPasswordViewModel;
  private TextInputLayout mUserNameEditText;
  private LoadToast loadToast;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password_send);
    loadToast = new LoadToast(this);
    forgotPasswordViewModel = new ForgotPasswordViewModel(getApplication(), this);
    Button sendLinkButton = findViewById(R.id.button_send_link);
    mUserNameEditText = findViewById(R.id.edit_text_user_name);
    sendLinkButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String userName = mUserNameEditText.getEditText().getText().toString().trim();
        if (userName.isEmpty()) {
          mUserNameEditText.setError("Please Enter User Name");
        } else {
          loadToast.setText("Processing");
          loadToast.show();
          forgotPasswordViewModel.sendForgotPassLink(userName);
        }
      }
    });
    forgotPasswordViewModel.isValidUsername.observe(this, new Observer<Boolean>() {
      @Override public void onChanged(Boolean aBoolean) {
        handleValidUsername(aBoolean);
      }
    });
    forgotPasswordViewModel.error.observe(this, new Observer<String>() {
      @Override public void onChanged(String s) {
        loadToast.error();
        Toast.makeText(ForgotPasswordSend.this, s, Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void handleValidUsername(Boolean aBoolean) {
    loadToast.hide();
    if(!aBoolean){
      mUserNameEditText.setError(getApplicationContext().getString(R.string.invalid_username));
    }else {
      mUserNameEditText.setError(null);
      Toast.makeText(
          ForgotPasswordSend.this,
          R.string.link_sent_please_check_your_email,
          Toast.LENGTH_SHORT).show();
    }
  }
}
