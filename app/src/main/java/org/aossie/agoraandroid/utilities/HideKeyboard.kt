package org.aossie.agoraandroid.utilities

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object HideKeyboard {
  fun hideKeyboardInActivity(appCompatActivity: AppCompatActivity) {
    val view = appCompatActivity.currentFocus
    val imm = appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
  }

  fun hideKeyboardInFrag(fragment: Fragment) {
    val view = fragment.activity!!.currentFocus
    val imm =
      fragment.activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
  }
}