package org.aossie.agoraandroid.utilities

import android.R.string
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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

fun TextView.hide() {
  visibility = TextView.GONE
}

fun TextView.show() {
  visibility = TextView.VISIBLE
}

fun View.snackbar(message: String) {
  Snackbar
      .make(this, message, Snackbar.LENGTH_INDEFINITE)
      .also { snackbar ->
        snackbar.setAction(AppConstants.ok, View.OnClickListener {
          snackbar.dismiss()
        }).show()
      }
}

fun View.errorDialog(message: String){
  AlertDialog.Builder(context)
      .setTitle("Alert ! ! !")
      .setMessage(message)
      .setCancelable(false)
      .setPositiveButton(string.ok) { dialog, which ->
        dialog.cancel()
      }.create().show()
}

fun View.shortSnackbar(message: String){
  Snackbar
      .make(this, message, Snackbar.LENGTH_SHORT)
      .show()
}