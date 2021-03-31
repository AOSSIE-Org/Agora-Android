package org.aossie.agoraandroid.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.aossie.agoraandroid.di.utils.FragmentKey
import org.aossie.agoraandroid.di.utils.MainFragmentFactory
import org.aossie.agoraandroid.ui.fragments.auth.forgotpassword.ForgotPasswordFragment
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginFragment
import org.aossie.agoraandroid.ui.fragments.auth.signup.SignUpFragment
import org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication.TwoFactorAuthFragment
import org.aossie.agoraandroid.ui.fragments.createelection.CreateElectionFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.ActiveElectionsFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.FinishedElectionsFragment
import org.aossie.agoraandroid.ui.fragments.displayelections.PendingElectionsFragment
import org.aossie.agoraandroid.ui.fragments.electionDetails.ResultFragment
import org.aossie.agoraandroid.ui.fragments.electionDetails.BallotFragment
import org.aossie.agoraandroid.ui.fragments.electionDetails.ElectionDetailsFragment
import org.aossie.agoraandroid.ui.fragments.electionDetails.VotersFragment
import org.aossie.agoraandroid.ui.fragments.elections.CalendarViewElectionFragment
import org.aossie.agoraandroid.ui.fragments.elections.ElectionsFragment
import org.aossie.agoraandroid.ui.fragments.home.HomeFragment
import org.aossie.agoraandroid.ui.fragments.invitevoters.InviteVotersFragment
import org.aossie.agoraandroid.ui.fragments.profile.ProfileFragment
import org.aossie.agoraandroid.ui.fragments.settings.SettingsFragment

@Module
abstract class FragmentModule {

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
  @FragmentKey(SettingsFragment::class)
  abstract fun bindMoreOptionsFragment(settingsFragment: SettingsFragment): Fragment

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
  @FragmentKey(
    ElectionDetailsFragment::class
  )
  abstract fun bindsElectionDetailsFragment(electionDetailsFragment: ElectionDetailsFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(
    VotersFragment::class
  )
  abstract fun bindsVotersFragment(votersFragment: VotersFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(
    BallotFragment::class
  )
  abstract fun bindsBallotFragment(ballotFragment: BallotFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(InviteVotersFragment::class)
  abstract fun bindsInviteVotersFragment(inviteVotersFragment: InviteVotersFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(ResultFragment::class)
  abstract fun bindsResultFragment(resultFragment: ResultFragment): Fragment

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
  @FragmentKey(ProfileFragment::class)
  abstract fun bindsProfileFragment(profileFragment: ProfileFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(CreateElectionFragment::class)
  abstract fun bindsCreateElectionFragment(createElectionFragment: CreateElectionFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(CalendarViewElectionFragment::class)
  abstract fun bindsCalendarViewElectionFragment(calendarViewElectionFragment: CalendarViewElectionFragment): Fragment

  @Binds
  @IntoMap
  @FragmentKey(TwoFactorAuthFragment::class)
  abstract fun bindsTwoFactorAuthenticationFragment(twoFactorAuthFragment: TwoFactorAuthFragment): Fragment

  @Binds
  abstract fun bindFragmentFactory(factory: MainFragmentFactory): FragmentFactory
}
