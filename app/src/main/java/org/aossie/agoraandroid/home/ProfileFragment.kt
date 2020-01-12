package org.aossie.agoraandroid.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

  private val mFirstNameTextInputLayout: TextInputLayout? = null
  private val mLastNameTextInputLayout: TextInputLayout? = null
  private val mUserNameTextInputLayout: TextInputLayout? = null
  private val mEmailIdTextInputLayout: TextInputLayout? = null
  private val mNewPass: TextInputLayout? = null
  private val mConfirmPass: TextInputLayout? = null
  private val loadToast: LoadToast? = null

  lateinit var binding: FragmentProfileBinding
  lateinit var viewModel: ProfileViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

    binding.viewModel = viewModel

    binding.changePasswordBtn.setOnClickListener {
      viewModel.changePassword(
          binding.newPasswordTil.editText!!.text.toString(),
          binding.confirmPasswordTil.editText!!.text.toString()
      )
    }

    //        TextView userName = view.findViewById(R.id.use);
//        TextView emailId = view.findViewById(R.id.email_id_tv);
//        final String token = sharedPrefs.getToken();
//        Button mChangePassButton = view.findViewById(R.id.button_change_password);
//        mNewPass = view.findViewById(R.id.textInputLayout_new_password);
//        mConfirmPass = view.findViewById(R.id.textInputLayout_confirm_password);
//        mChangePassButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String newPass = mNewPass.getEditText().getText().toString().trim();
//                String confirmPass = mConfirmPass.getEditText().getText().toString().trim();
//                if (newPass.equals(confirmPass)) {
//                    if (newPass.equals(sharedPrefs.getPass())) {
//                        mNewPass.setError("New Password should not be same as old one");
//                        mConfirmPass.getEditText().setText("");
//                        mConfirmPass.setErrorEnabled(false);
//                    } else
//                        doChangePasswordRequest(confirmPass, token);
//                }
//                else {
//                    mConfirmPass.setError("Password Does Not Matches");
//                    mNewPass.setErrorEnabled(false);
//                }
//            }
//        });
//        String username = sharedPrefs.getUserName();
//        String email = sharedPrefs.getEmail();
//
//        userName.setText(username);
//        emailId.setText(email);

    viewModel.error.observe(this, Observer {
      when(it){
        1-> binding.firstNameTil.error = "not valid"
      }
    })
    return binding.getRoot()
  }


}