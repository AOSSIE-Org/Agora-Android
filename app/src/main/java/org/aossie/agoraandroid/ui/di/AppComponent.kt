package org.aossie.agoraandroid.ui.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteActivity
import org.aossie.agoraandroid.ui.activities.main.MainActivity
import org.aossie.agoraandroid.ui.di.modules.AppModule
import org.aossie.agoraandroid.ui.di.modules.FragmentModule
import org.aossie.agoraandroid.ui.di.modules.ViewModelModule
import org.aossie.agoraandroid.utilities.InternetManager
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, FragmentModule::class])
interface AppComponent {
  // Factory to create instances of the AppComponent
  @Component.Factory
  interface Factory {
    // With @BindsInstance, the Context passed in will be available in the graph
    fun create(@BindsInstance context: Context): AppComponent
  }

  // TODO inject activities, fragments, starting points of graph in here

  fun inject(activity: MainActivity)
  fun inject(activity: CastVoteActivity)
  fun getInternetManager(): InternetManager
}
