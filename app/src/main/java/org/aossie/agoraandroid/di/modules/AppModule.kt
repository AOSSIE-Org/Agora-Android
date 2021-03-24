package org.aossie.agoraandroid.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.interceptors.AuthorizationInterceptor
import org.aossie.agoraandroid.data.network.Client
import org.aossie.agoraandroid.data.network.interceptors.NetworkInterceptor
import org.aossie.agoraandroid.ui.fragments.createelection.ElectionDetailsSharedPrefs

@Module
class AppModule {

  //TODO provide App level dependencies in here using @Provides

  @Provides
  fun providesPreferenceProvider(context: Context): PreferenceProvider{
    return PreferenceProvider(context)
  }

  @Provides
  fun providesElectionDetailsSharedPrefs(context: Context): ElectionDetailsSharedPrefs{
    return ElectionDetailsSharedPrefs(context)
  }

  @Provides
  fun providesNetworkInterceptor(context: Context): NetworkInterceptor {
    return NetworkInterceptor(
        context
    )
  }

  @Provides
  fun providesAppDatabase(context: Context): AppDatabase{
    return AppDatabase(context)
  }

  @Provides
  fun providesClient(context: Context, networkInterceptor: NetworkInterceptor): Client{
    return Client(context, networkInterceptor)
  }

  @Provides
  fun providesAuthorizationInterceptor(preferenceProvider: PreferenceProvider, appDatabase: AppDatabase, client: Client): AuthorizationInterceptor {
    return AuthorizationInterceptor(
        preferenceProvider, appDatabase, client
    )
  }

  @Provides
  fun providesApi(context: Context, networkInterceptor: NetworkInterceptor, authorizationInterceptor: AuthorizationInterceptor): Api {
    return Api(
        context, networkInterceptor, authorizationInterceptor
    )
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