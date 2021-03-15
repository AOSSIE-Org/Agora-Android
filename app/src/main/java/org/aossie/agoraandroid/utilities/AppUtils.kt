package org.aossie.agoraandroid.utilities

import java.util.regex.Pattern

fun String.isUrl() =
  Pattern.compile(AppConstants.URL_REGEX).matcher(this).matches()