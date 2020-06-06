package org.aossie.agoraandroid.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import org.aossie.agoraandroid.data.db.PreferenceProvider

@Module
class AppModule {

  //TODO provide App level dependencies in here using @Provides

  @Provides
  fun providesPreferenceProvider(context: Context): PreferenceProvider{
    return PreferenceProvider(context)
  }
}