package org.aossie.agoraandroid.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.aossie.agoraandroid.di.utils.ViewModelFactory
import org.aossie.agoraandroid.di.utils.ViewModelKey
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel
import org.aossie.agoraandroid.ui.fragments.auth.forgotpassword.ForgotPasswordViewModel
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.ui.fragments.auth.signup.SignUpViewModel
import org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication.TwoFactorAuthViewModel
import org.aossie.agoraandroid.ui.fragments.createelection.CreateElectionViewModel
import org.aossie.agoraandroid.ui.fragments.displayelections.DisplayElectionViewModel
import org.aossie.agoraandroid.ui.fragments.elections.ElectionViewModel
import org.aossie.agoraandroid.ui.fragments.invitevoters.InviteVotersViewModel
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel

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

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    internal abstract fun bindsSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DisplayElectionViewModel::class)
    internal abstract fun bindsDisplayElectionViewModel(viewModel: DisplayElectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InviteVotersViewModel::class)
    internal abstract fun bindsInviteVotersViewModel(viewModel: InviteVotersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateElectionViewModel::class)
    internal abstract fun bindsCreateElectionViewModel(viewModel: CreateElectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindsProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TwoFactorAuthViewModel::class)
    internal abstract fun bindsTwoFactorAuthViewModel(viewModel: TwoFactorAuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CastVoteViewModel::class)
    internal abstract fun bindsCastVoteViewModel(viewModel: CastVoteViewModel): ViewModel
}