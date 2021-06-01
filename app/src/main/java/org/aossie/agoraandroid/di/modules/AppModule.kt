package org.aossie.agoraandroid.di.modules

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.aossie.agoraandroid.BuildConfig
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.interceptors.AuthorizationInterceptor
import org.aossie.agoraandroid.di.utils.InternetManager
import org.aossie.agoraandroid.ui.fragments.createelection.ElectionDetailsSharedPrefs
import org.aossie.agoraandroid.utilities.AppConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class AppModule {

  // TODO provide App level dependencies in here using @Provides

  @Provides
  @Singleton
  fun providesPreferenceProvider(context: Context): PreferenceProvider {
    return PreferenceProvider(context)
  }

  @Provides
  @Singleton
  fun providesElectionDetailsSharedPrefs(context: Context): ElectionDetailsSharedPrefs {
    return ElectionDetailsSharedPrefs(context)
  }

  @Provides
  @Singleton
  fun providesAppDatabase(context: Context): AppDatabase {
    return AppDatabase(context)
  }

  @Provides
  @Singleton
  fun providesAuthorizationInterceptor(context: Context, preferenceProvider: PreferenceProvider, appDatabase: AppDatabase, @Named("apiWithoutAuth") api: Api, manager: InternetManager): AuthorizationInterceptor {
    return AuthorizationInterceptor(
      context, preferenceProvider, appDatabase, manager, api
    )
  }

  @Provides
  @Singleton
  fun providesApi(retrofit: Retrofit): Api =
    retrofit.create(Api::class.java)

  @Provides
  @Singleton
  @Named("apiWithoutAuth")
  fun providesApiWithoutAuth(@Named("retrofitWithoutAuth") retrofit: Retrofit): Api =
    retrofit.create(Api::class.java)

  @Provides
  @Singleton
  fun provideOkHttpClient(context: Context, authorizationInterceptor: AuthorizationInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
      .apply {
        if (BuildConfig.DEBUG) {
          addInterceptor(
            HttpLoggingInterceptor().apply {
              level = Level.BASIC
            }
          )
          addInterceptor(
            ChuckerInterceptor.Builder(context)
              .build()
          )
        }
        addInterceptor(authorizationInterceptor)
      }
      .build()
  }

  @Provides
  @Singleton
  @Named("okHttpWithoutAuth")
  fun provideOkHttpClientWithoutAuth(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
      .apply {
        if (BuildConfig.DEBUG) {
          addInterceptor(
            HttpLoggingInterceptor().apply {
              level = Level.BASIC
            }
          )
          addInterceptor(
            ChuckerInterceptor.Builder(context)
              .build()
          )
        }
      }
      .build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(AppConstants.BASE_URL)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  @Named("retrofitWithoutAuth")
  fun provideRetrofitWithoutAuth(@Named("okHttpWithoutAuth") okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(AppConstants.BASE_URL)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  fun provideInternetManager(context: Context) = InternetManager(context)

  @Provides
  @Singleton
  fun providesUserRepository(
    api: Api,
    appDatabase: AppDatabase,
    preferenceProvider: PreferenceProvider,
    manager: InternetManager
  ): UserRepository {
    return UserRepository(api, appDatabase, preferenceProvider, manager)
  }

  @Provides
  @Singleton
  fun providesElectionsRepository(
    api: Api,
    appDatabase: AppDatabase,
    preferenceProvider: PreferenceProvider,
    manager: InternetManager
  ): ElectionsRepository {
    return ElectionsRepository(api, appDatabase, preferenceProvider, manager)
  }
}
