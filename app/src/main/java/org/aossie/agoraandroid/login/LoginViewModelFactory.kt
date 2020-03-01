package org.aossie.agoraandroid.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.aossie.agoraandroid.networking.Networking
import org.aossie.agoraandroid.utilities.CacheManager

class LoginViewModelFactory(private val networking: Networking, private val cacheManager: CacheManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(networking,cacheManager) as T
    }
}