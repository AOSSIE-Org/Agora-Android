package org.aossie.agoraandroid.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.aossie.agoraandroid.di.utils.ViewModelFactory
import org.aossie.agoraandroid.di.utils.ViewModelKey
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindsLoginViewModel(viewModel: LoginViewModel): ViewModel

}