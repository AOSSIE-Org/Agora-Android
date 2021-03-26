package org.aossie.agoraandroid.di.modules

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.aossie.agoraandroid.BuildConfig
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.interceptors.AuthorizationInterceptor
import org.aossie.agoraandroid.data.network.interceptors.NetworkInterceptor
import org.aossie.agoraandroid.ui.fragments.createelection.ElectionDetailsSharedPrefs
import org.aossie.agoraandroid.utilities.AppConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

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
  @Singleton
  fun providesNetworkInterceptor(context: Context): NetworkInterceptor {
    return NetworkInterceptor(
        context
    )
  }

  @Provides
  @Singleton
  fun providesAppDatabase(context: Context): AppDatabase{
    return AppDatabase(context)
  }

  @Provides
  @Singleton
  fun providesAuthorizationInterceptor(preferenceProvider: PreferenceProvider, appDatabase: AppDatabase, @Named("apiWithoutAuthorization") api: Api): AuthorizationInterceptor {
    return AuthorizationInterceptor(
        preferenceProvider, appDatabase, api
    )
  }

  @Provides
  @Singleton
  fun providesApi(retrofit: Retrofit): Api =
    retrofit.create(Api::class.java)

  @Provides
  @Singleton
  @Named("apiWithoutAuthorization")
  fun providesApiWithoutAuthorization(@Named("retrofitWithoutAuthorization") retrofit: Retrofit): Api =
    retrofit.create(Api::class.java)

  @Provides
  @Singleton
  fun provideOkHttpClient(context: Context,networkInterceptor: NetworkInterceptor, authorizationInterceptor: AuthorizationInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .apply {
          addInterceptor(networkInterceptor)
          if (BuildConfig.DEBUG) {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                  level = AppConstants.HTTP_INTERCEPTOR_LEVEL
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
  @Named("okHttpWithoutAuthorization")
  fun provideOkHttpClientWithoutAuthorization(context: Context,networkInterceptor: NetworkInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .apply {
          addInterceptor(networkInterceptor)
          if (BuildConfig.DEBUG) {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                  level = AppConstants.HTTP_INTERCEPTOR_LEVEL
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
  @Named("retrofitWithoutAuthorization")
  fun provideRetrofitWithoutAuthorization(@Named("okHttpWithoutAuthorization") okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(AppConstants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
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