package org.aossie.agoraandroid.main.signUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.databinding.ActivitySignUpBinding;
import org.aossie.agoraandroid.main.presenter.Presenter;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding activitySignUpBinding;
    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = new SignUpViewModel(this);
        activitySignUpBinding.setSignupview(signUpViewModel);

        activitySignUpBinding.setPresenter(new Presenter() {
            @Override
            public void signUpData() {
                final String userName = activitySignUpBinding.signUpUserName.getEditText().getText().toString();
                final String firstName = activitySignUpBinding.signUpFirstName.getEditText().getText().toString();
                final String lastName = activitySignUpBinding.signUpLastName.getEditText().getText().toString();
                final String userEmail = activitySignUpBinding.signUpEmail.getEditText().getText().toString();
                final String userPass = activitySignUpBinding.signUpPassword.getEditText().getText().toString();

                if (userName.isEmpty())
                    activitySignUpBinding.signUpUserName.setError("Please enter User Name");
                else activitySignUpBinding.signUpUserName.setError(null);

                if (firstName.isEmpty()) {
                    activitySignUpBinding.signUpFirstName.setError("Please enter First Name");
                } else activitySignUpBinding.signUpFirstName.setError(null);

                if (lastName.isEmpty()) {
                    activitySignUpBinding.signUpLastName.setError("Please enter Last Name");
                } else activitySignUpBinding.signUpLastName.setError(null);

                if (userEmail.isEmpty()) {
                    activitySignUpBinding.signUpEmail.setError("PLease enter Email Address");
                } else activitySignUpBinding.signUpEmail.setError(null);

                if (userPass.isEmpty()) {
                    activitySignUpBinding.signUpPassword.setError("Please enter password");
                } else {
                    activitySignUpBinding.signUpPassword.setError(null);
                    signUpViewModel.signUpRequest(userName, userPass, userEmail, firstName, lastName);
                }
            }
        });

    }

}

