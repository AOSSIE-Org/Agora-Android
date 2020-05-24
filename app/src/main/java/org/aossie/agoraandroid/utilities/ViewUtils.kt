package org.aossie.agoraandroid.utilities

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.hideActionBar() = (activity as AppCompatActivity).supportActionBar!!.hide()
fun Fragment.showActionBar() = (activity as AppCompatActivity).supportActionBar!!.show()