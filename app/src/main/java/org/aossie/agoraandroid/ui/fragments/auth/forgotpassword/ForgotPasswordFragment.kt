package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_forgot_password.edit_text_user_name
import kotlinx.android.synthetic.main.fragment_forgot_password.view.button_send_link
import kotlinx.android.synthetic.main.fragment_forgot_password.view.edit_text_user_name
import net.steamcrafted.loadtoast.LoadToast

import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.utilities.HideKeyboard

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : Fragment() {

  private var forgotPasswordViewModel: ForgotPasswordViewModel? = null
  private var loadToast: LoadToast? = null

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)

    loadToast = LoadToast(context)
    forgotPasswordViewModel = ForgotPasswordViewModel(activity!!.application, context!!)
    rootView.button_send_link.setOnClickListener {
      val userName = rootView.edit_text_user_name.editText
          ?.text
          .toString()
          .trim { it <= ' ' }
      if (userName.isEmpty()) {
        rootView.edit_text_user_name.setError("Please Enter User Name")
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        loadToast!!.setText("Processing")
        loadToast!!.show()
        forgotPasswordViewModel!!.sendForgotPassLink(userName)
      }
    }
    forgotPasswordViewModel!!.isValidUsername.observe(
        viewLifecycleOwner, Observer<Boolean> { aBoolean -> handleValidUsername(aBoolean) })
    forgotPasswordViewModel!!.error.observe(
        viewLifecycleOwner, Observer<String?> { s ->
      loadToast!!.error()
      Toast.makeText(context, s, Toast.LENGTH_SHORT)
          .show()
    })

    return rootView
  }

  private fun handleValidUsername(aBoolean: Boolean) {
    loadToast!!.hide()
    if (!aBoolean) {
      edit_text_user_name.error = getString(string.invalid_username)
    } else {
      edit_text_user_name.error = null
      Toast.makeText(
          context,
          string.link_sent_please_check_your_email,
          Toast.LENGTH_SHORT
      )
          .show()
    }
  }

}
