package org.aossie.agoraandroid.adapters

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_invite_voters.text_input_voter_email
import kotlinx.android.synthetic.main.activity_invite_voters.text_input_voter_name

class TextWatcherAdapter(private val context: Context) : TextWatcher{


  override fun afterTextChanged(p0: Editable?) {

    val activity:Activity = context as Activity

    if (!activity.text_input_voter_name.editText!!.text.isNullOrEmpty()) {
      activity.text_input_voter_name.error=null
    }
    if (!activity.text_input_voter_email.editText!!.text.isNullOrEmpty()) {
      activity.text_input_voter_email.error=null
    }

  }

  override fun beforeTextChanged(
    p0: CharSequence?,
    p1: Int,
    p2: Int,
    p3: Int) {


  }

  override fun onTextChanged(
    p0: CharSequence?,
    p1: Int,
    p2: Int,
    p3: Int
  )
    {

      }


 }