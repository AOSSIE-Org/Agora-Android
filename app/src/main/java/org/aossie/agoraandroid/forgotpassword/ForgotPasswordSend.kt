package org.aossie.agoraandroid.forgotPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.utilities.CheckNetwork

class ForgotPasswordSend : Fragment() {
    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var rootView: View
    private var usernameEditText: TextInputLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        usernameEditText = rootView.findViewById(R.id.edit_text_user_name)
        rootView.button_send_link.setOnClickListener { sendVerificationLink() }
        return rootView
    }

    private fun sendVerificationLink() {
        val userName = usernameEditText!!.editText!!.text.toString().trim { it <= ' ' }
        if (userName.isEmpty()) {
            usernameEditText?.error = "Please Enter User Name"
        } else {
            if (!CheckNetwork.isNetworkConnected(requireContext())) {
                Toast.makeText(requireContext(), "No Internet Connection!!", Toast.LENGTH_LONG).show()
            } else viewModel.sendForgotPassLink(userName)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
    }
}
