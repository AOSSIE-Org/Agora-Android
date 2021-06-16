package org.aossie.agoraandroid.utilities

import java.io.IOException

class NoInternetException(message: String) : IOException(message)
class ApiException(message: String) : IOException(message)
class SessionExpirationException : IOException()
