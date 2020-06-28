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
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginFragment
import org.aossie.agoraandroid.ui.fragments.elections.ElectionsFragment
import org.aossie.agoraandroid.ui.fragments.home.HomeFragment
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
  abstract fun bindFragmentFactory(factory: MainFragmentFactory): FragmentFactory

}