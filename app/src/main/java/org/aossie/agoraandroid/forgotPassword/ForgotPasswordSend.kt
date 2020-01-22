package org.aossie.agoraandroid.forgotPassword

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.utilities.CheckNetwork

class ForgotPasswordSend : AppCompatActivity() {
    private var forgotPasswordViewModel: ForgotPasswordViewModel? = null
    private var mUserNameEditText: TextInputLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_send)
        forgotPasswordViewModel = ForgotPasswordViewModel(application, this)
        val sendLinkButton = findViewById<Button>(R.id.button_send_link)
        mUserNameEditText = findViewById(R.id.edit_text_user_name)
        sendLinkButton.setOnClickListener {
            val userName = mUserNameEditText.getEditText()!!.text.toString().trim { it <= ' ' }
            if (userName.isEmpty()) {
                mUserNameEditText.setError("Please Enter User Name")
            } else {
                if (!CheckNetwork.isNetworkConnected(baseContext)) {
                    Toast.makeText(baseContext, "No Internet Connection!!", Toast.LENGTH_LONG).show()
                } else forgotPasswordViewModel!!.sendForgotPassLink(userName)
            }
        }
    }
}