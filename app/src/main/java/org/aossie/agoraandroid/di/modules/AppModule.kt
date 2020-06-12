package org.aossie.agoraandroid.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.NetworkInterceptor

@Module
class AppModule {

  //TODO provide App level dependencies in here using @Provides

  @Provides
  fun providesPreferenceProvider(context: Context): PreferenceProvider{
    return PreferenceProvider(context)
  }

  @Provides
  fun providesNetworkInterceptor(context: Context): NetworkInterceptor{
    return NetworkInterceptor(context)
  }

  @Provides
  fun providesAppDatabase(context: Context): AppDatabase{
    return AppDatabase(context)
  }

  @Provides
  fun providesApi(networkInterceptor: NetworkInterceptor): Api{
    return Api(networkInterceptor)
  }

  @Provides
  fun providesUserRepository(
    api: Api,
    appDatabase: AppDatabase,
    preferenceProvider: PreferenceProvider
  ): UserRepository{
    return UserRepository(api, appDatabase, preferenceProvider)
  }

  @Provides
  fun providesElectionsRepository(
    api: Api,
    appDatabase: AppDatabase,
    preferenceProvider: PreferenceProvider
  ): ElectionsRepository{
    return ElectionsRepository(api, appDatabase, preferenceProvider)
  }
}