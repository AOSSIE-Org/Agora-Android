package org.aossie.agoraandroid.ui.di.modules

import android.content.Context
import androidx.viewbinding.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepositoryImpl
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.network.api.Api
import org.aossie.agoraandroid.data.network.api.FCMApi
import org.aossie.agoraandroid.data.network.interceptors.AuthorizationInterceptor
import org.aossie.agoraandroid.data.network.interceptors.HeaderInterceptor
import org.aossie.agoraandroid.data.network.interceptors.NetworkInterceptor
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.domain.use_cases.authentication.login.FaceBookLogInUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.RefreshAccessTokenUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.SaveUserUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.UserLogInUseCase
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.InternetManager
import org.aossie.agoraandroid.utilities.SecurityUtil
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

  // TODO provide App level dependencies in here using @Provides

  @Provides
  @Singleton
  fun providesPreferenceProvider(context: Context, securityUtil: SecurityUtil): PreferenceProvider {
    return PreferenceProvider(context, securityUtil)
  }

  @Provides
  @Singleton
  fun provideInternetManager(context: Context): InternetManager {
    return InternetManager(context)
  }

  @Provides
  @Singleton
  fun providesNetworkInterceptor(
    context: Context,
    internetManager: InternetManager
  ): NetworkInterceptor {
    return NetworkInterceptor(
      context,
      internetManager
    )
  }

  @Provides
  @Singleton
  fun providesHeaderInterceptor(
    preferenceProvider: PreferenceProvider,
    context: Context
  ): HeaderInterceptor {
    return HeaderInterceptor(preferenceProvider, context.resources.getString(R.string.serverKey))
  }

  @Provides
  @Singleton
  fun providesAppDatabase(context: Context): AppDatabase {
    return AppDatabase(context)
  }

  @Provides
  @Singleton
  fun providesAuthorizationInterceptor(
    preferenceProvider: PreferenceProvider,
    appDatabase: AppDatabase,
    @Named("apiWithoutAuth") api: Api
  ): AuthorizationInterceptor {
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
  @Named("apiWithoutAuth")
  fun providesApiWithoutAuth(@Named("retrofitWithoutAuth") retrofit: Retrofit): Api =
    retrofit.create(Api::class.java)

  @Provides
  @Singleton
  fun provideOkHttpClient(
    context: Context,
    networkInterceptor: NetworkInterceptor,
    authorizationInterceptor: AuthorizationInterceptor,
    headerInterceptor: HeaderInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .apply {
        addInterceptor(networkInterceptor)
        addInterceptor(headerInterceptor)
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
  fun provideOkHttpClientWithoutAuth(
    context: Context,
    networkInterceptor: NetworkInterceptor,
    headerInterceptor: HeaderInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .apply {
        addInterceptor(networkInterceptor)
        addInterceptor(headerInterceptor)
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
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  @Named("retrofitWithoutAuth")
  fun provideRetrofitWithoutAuth(@Named("okHttpWithoutAuth") okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(AppConstants.BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  fun provideUserRepository(
    api: Api,
    appDatabase: AppDatabase,
    preferenceProvider: PreferenceProvider
  ): UserRepository {
    return UserRepositoryImpl(api, appDatabase, preferenceProvider)
  }

  @Provides
  @Singleton
  fun provideUserRepositoryImpl(
    api: Api,
    appDatabase: AppDatabase,
    preferenceProvider: PreferenceProvider
  ): UserRepositoryImpl {
    return UserRepositoryImpl(api, appDatabase, preferenceProvider)
  }

  @Provides
  @Singleton
  fun providesElectionsRepository(
    api: Api,
    appDatabase: AppDatabase,
    preferenceProvider: PreferenceProvider
  ): ElectionsRepository {
    return ElectionsRepository(api, appDatabase, preferenceProvider)
  }

  @Provides
  @Singleton
  fun providesSecurityUtil(
    context: Context
  ): SecurityUtil {
    return SecurityUtil(context.resources.getString(R.string.secretKey))
  }

  @Provides
  @Singleton
  @Named("okHttpForFCM")
  fun provideOkHttpClientForFCM(
    context: Context,
    networkInterceptor: NetworkInterceptor,
    headerInterceptor: HeaderInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .apply {
        addInterceptor(networkInterceptor)
        addInterceptor(headerInterceptor)
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
  @Named("retrofitForFCM")
  fun provideRetrofitForFCM(@Named("okHttpForFCM") okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(AppConstants.FCM_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  fun providesFCM(@Named("retrofitForFCM") retrofit: Retrofit): FCMApi =
    retrofit.create(FCMApi::class.java)

  @Provides
  @Singleton
  fun provideFaceBookLogInUseCase(repository: UserRepository): FaceBookLogInUseCase {
    return FaceBookLogInUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideUserLogInUseCase(repository: UserRepository): UserLogInUseCase {
    return UserLogInUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideSaveUserUseCase(repository: UserRepository): SaveUserUseCase {
    return SaveUserUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideRefreshAccessTokenUseCase(repository: UserRepository): RefreshAccessTokenUseCase {
    return RefreshAccessTokenUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideGetUserUseCase(repository: UserRepository): GetUserUseCase {
    return GetUserUseCase(repository)
  }

}
