package org.aossie.agoraandroid.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.aossie.agoraandroid.di.utils.FragmentKey
import org.aossie.agoraandroid.di.utils.MainFragmentFactory
import org.aossie.agoraandroid.ui.fragments.auth.forgotpassword.ForgotPasswordFragment
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginFragment
import org.aossie.agoraandroid.ui.fragments.auth.signup.SignUpFragment
import org.aossie.agoraandroid.ui.fragments.createelection.UploadElectionDetailsFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.ActiveElectionsFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.BallotFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.ElectionDetailsFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.FinishedElectionsFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.PendingElectionsFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.VotersFragment
import org.aossie.agoraandroid.ui.fragments.elections.ElectionsFragment
import org.aossie.agoraandroid.ui.fragments.home.HomeFragment
import org.aossie.agoraandroid.ui.fragments.invitevoters.InviteVoterListener
import org.aossie.agoraandroid.ui.fragments.invitevoters.InviteVotersFragment
import org.aossie.agoraandroid.ui.fragments.moreOptions.MoreOptionsFragment
import org.aossie.agoraandroid.ui.fragments.welcome.WelcomeFragment

@Module
abstract class FragmentModule{

  @Binds
  @IntoMap
  @FragmentKey(WelcomeFragment::class)
  abstract fun bindWelcomeFragment(mainFragment: WelcomeFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(LoginFragment::class)
  abstract fun bindLoginFragment(mainFragment: LoginFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(HomeFragment::class)
  abstract fun bindHomeFragment(homeFragment: HomeFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(MoreOptionsFragment::class)
  abstract fun bindMoreOptionsFragment(moreOptionsFragment: MoreOptionsFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(ElectionsFragment::class)
  abstract fun bindElectionsFragment(electionsFragment: ElectionsFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(ForgotPasswordFragment::class)
  abstract fun bindsForgotPasswordFragment(electionsFragment: ForgotPasswordFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(SignUpFragment::class)
  abstract fun bindsSignUpFragment(signUpFragment: SignUpFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(ElectionDetailsFragment::class)
  abstract fun bindsElectionDetailsFragment(electionDetailsFragment: ElectionDetailsFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(VotersFragment::class)
  abstract fun bindsVotersFragment(votersFragment: VotersFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(BallotFragment::class)
  abstract fun bindsBallotFragment(ballotFragment: BallotFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(InviteVotersFragment::class)
  abstract fun bindsInviteVotersFragment(inviteVotersFragment: InviteVotersFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(ActiveElectionsFragment::class)
  abstract fun bindsActiveElectionsFragment(activeElectionsFragment: ActiveElectionsFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(PendingElectionsFragment::class)
  abstract fun bindsPendingElectionsFragment(pendingElectionsFragment: PendingElectionsFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(FinishedElectionsFragment::class)
  abstract fun bindsFinishedElectionsFragment(finishedElectionsFragment: FinishedElectionsFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(UploadElectionDetailsFragment::class)
  abstract fun bindsUploadElectionDetailsFragment(uploadElectionDetailsFragment: UploadElectionDetailsFragment): Fragment

  @Binds
  abstract fun bindFragmentFactory(factory: MainFragmentFactory): FragmentFactory

}