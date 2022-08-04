package org.aossie.agoraandroid.data.di.modules

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.aossie.agoraandroid.BuildConfig
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.remote.apiservice.Api
import org.aossie.agoraandroid.data.remote.apiservice.FCMApi
import org.aossie.agoraandroid.data.network.interceptors.AuthorizationInterceptor
import org.aossie.agoraandroid.data.network.interceptors.HeaderInterceptor
import org.aossie.agoraandroid.data.network.interceptors.NetworkInterceptor
import org.aossie.agoraandroid.data.repository.ElectionsRepositoryImpl
import org.aossie.agoraandroid.data.repository.UserRepositoryImpl
import org.aossie.agoraandroid.common.utilities.AppConstants
import org.aossie.agoraandroid.common.utilities.InternetManager
import org.aossie.agoraandroid.common.utilities.SecurityUtil
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.domain.useCases.auth_useCases.forgot_passowrd.SendForgotPasswordLinkUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.FaceBookLogInUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.GetUserDataUseCase_
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.GetUserUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.RefreshAccessTokenUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.SaveUserUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.UserLogInUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.signup.SignUpUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.twoFactorAuthentication.ResendOTPUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.twoFactorAuthentication.VeriFyOTPUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.ChangeAvatarUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.ChangePasswordUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.GetUserDataUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.ToggleTwoFactorAuthenticationUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.UpdateUserUseCase
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
  fun providesNetworkInterceptor(context: Context, internetManager: InternetManager): NetworkInterceptor {
    return NetworkInterceptor(
      context,
      internetManager
    )
  }

  @Provides
  @Singleton
  fun providesHeaderInterceptor(preferenceProvider: PreferenceProvider, context: Context): HeaderInterceptor {
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
  fun providesUserRepository(
    api: Api,
    appDatabase: AppDatabase,
    preferenceProvider: PreferenceProvider
  ): UserRepository {
    return UserRepositoryImpl(api, appDatabase, preferenceProvider)
  }

  @Provides
  @Singleton
  fun provideUserRepository(
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
  ): ElectionsRepositoryImpl {
    return ElectionsRepositoryImpl(api, appDatabase, preferenceProvider)
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
  fun provideFaceBookLogInUseCase(repository: UserRepository,getUserDataUseCase_: GetUserDataUseCase_,prefs: PreferenceProvider): FaceBookLogInUseCase {
    return FaceBookLogInUseCase(repository,getUserDataUseCase_,prefs)
  }

  @Provides
  @Singleton
  fun provideUserLogInUseCase(repository: UserRepository,saveUserUseCase: SaveUserUseCase,prefs: PreferenceProvider): UserLogInUseCase {
    return UserLogInUseCase(repository,saveUserUseCase,prefs)
  }

  @Provides
  @Singleton
  fun provideSaveUserUseCase(repository: UserRepository): SaveUserUseCase {
    return SaveUserUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideRefreshAccessTokenUseCase(repository: UserRepository,saveUserUseCase: SaveUserUseCase): RefreshAccessTokenUseCase {
    return RefreshAccessTokenUseCase(repository,saveUserUseCase)
  }

  @Provides
  @Singleton
  fun provideGetUserUseCase(repository: UserRepository): GetUserUseCase {
    return GetUserUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideGetUserDataUseCase_(saveUserUseCase: SaveUserUseCase,prefs: PreferenceProvider): GetUserDataUseCase_ {
    return GetUserDataUseCase_(saveUserUseCase ,prefs)
  }

  @Provides
  @Singleton
  fun provideSignUpUseCase(repository: UserRepository): SignUpUseCase {
    return SignUpUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideSendForgotPasswordLinkUseCase(repository: UserRepository) : SendForgotPasswordLinkUseCase {
    return SendForgotPasswordLinkUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideResendOTPUseCase(repository: UserRepository) : ResendOTPUseCase {
    return ResendOTPUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideVeriFyOTPUseCase(repository: UserRepository) : VeriFyOTPUseCase {
    return VeriFyOTPUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideChangePasswordUseCase(repository: UserRepository) : ChangePasswordUseCase {
    return ChangePasswordUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideChangeAvatarUseCase(repository: UserRepository) : ChangeAvatarUseCase {
    return ChangeAvatarUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideGetUserDataUseCase(repository: UserRepository) : GetUserDataUseCase {
    return GetUserDataUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideToggleTwoFactorAuthenticationUseCase(repository: UserRepository) : ToggleTwoFactorAuthenticationUseCase {
    return ToggleTwoFactorAuthenticationUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideUpdateUserUseCase(repository: UserRepository) : UpdateUserUseCase {
    return UpdateUserUseCase(repository)
  }

}
