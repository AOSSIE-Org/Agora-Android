package org.aossie.agoraandroid.utilities

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.hideActionBar() = (activity as AppCompatActivity).supportActionBar!!.hide()
fun Fragment.showActionBar() = (activity as AppCompatActivity).supportActionBar!!.show()

fun ProgressBar.show() {
  visibility = ProgressBar.VISIBLE
}

fun ProgressBar.hide() {
  visibility = ProgressBar.GONE
}

fun View.snackbar(message: String) {
  Snackbar
      .make(this, message, Snackbar.LENGTH_INDEFINITE)
      .also { snackbar ->
        snackbar.setAction("OK", View.OnClickListener {
          snackbar.dismiss()
        }).show()
      }
}

fun View.shortSnackbar(message: String){
  Snackbar
      .make(this, message, Snackbar.LENGTH_SHORT)
      .show()
}