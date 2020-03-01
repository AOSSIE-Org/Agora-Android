package org.aossie.agoraandroid.utilities

import android.os.Bundle

sealed class State {

    object Normal : State()
    object Loading : State()
    object Success : State()
    object Error : State()

    val errors: Bundle = Bundle()
}