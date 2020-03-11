package org.aossie.agoraandroid.utilities

import android.content.Context
import androidx.annotation.StringRes

class Resource {

  fun Context.getString(@StringRes resId: Int) = getString(resId)

}