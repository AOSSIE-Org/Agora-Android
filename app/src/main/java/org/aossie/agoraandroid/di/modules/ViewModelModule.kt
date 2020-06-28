package org.aossie.agoraandroid.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.aossie.agoraandroid.di.utils.ViewModelFactory
import org.aossie.agoraandroid.di.utils.ViewModelKey
import org.aossie.agoraandroid.ui.fragments.auth.forgotpassword.ForgotPasswordViewModel
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.ui.fragments.elections.ElectionViewModel
import org.aossie.agoraandroid.ui.fragments.moreOptions.HomeViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindsLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindsHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ElectionViewModel::class)
    internal abstract fun bindsElectionViewModel(viewModel: ElectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    internal abstract fun bindsForgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel
}