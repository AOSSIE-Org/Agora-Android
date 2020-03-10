package org.aossie.agoraandroid.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [])
interface AppComponent {
  // Factory to create instances of the AppComponent
  @Component.Factory
  interface Factory {
    // With @BindsInstance, the Context passed in will be available in the graph
    fun create(@BindsInstance context: Context): AppComponent
  }

  //TODO inject activities, fragments, starting points of graph in here

}