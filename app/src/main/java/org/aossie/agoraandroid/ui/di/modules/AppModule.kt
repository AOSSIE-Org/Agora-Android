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
import org.aossie.agoraandroid.data.Repository.ElectionsRepositoryImpl
import org.aossie.agoraandroid.data.Repository.UserRepositoryImpl
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.network.api.Api
import org.aossie.agoraandroid.data.network.api.FCMApi
import org.aossie.agoraandroid.data.network.interceptors.AuthorizationInterceptor
import org.aossie.agoraandroid.data.network.interceptors.HeaderInterceptor
import org.aossie.agoraandroid.data.network.interceptors.NetworkInterceptor
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.domain.useCases.authentication.forgotPassword.SendForgotPasswordLinkUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.FaceBookLogInUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.LogInUseCases
import org.aossie.agoraandroid.domain.useCases.authentication.login.RefreshAccessTokenUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.SaveUserUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.UserLogInUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.signUp.SignUpUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication.ResendOTPUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication.TwoFactorAuthUseCases
import org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication.VerifyOTPUseCase
import org.aossie.agoraandroid.domain.useCases.createElection.CreateElectionUseCase
import org.aossie.agoraandroid.domain.useCases.displayElection.DisplayElectionsUseCases
import org.aossie.agoraandroid.domain.useCases.displayElection.GetActiveElectionsUseCase
import org.aossie.agoraandroid.domain.useCases.displayElection.GetFinishedElectionsUseCase
import org.aossie.agoraandroid.domain.useCases.displayElection.GetPendingElectionsUseCase
import org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView.ElectionsUseCases
import org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView.GetElectionsUseCase
import org.aossie.agoraandroid.domain.useCases.homeFragment.DeleteUserUseCase
import org.aossie.agoraandroid.domain.useCases.homeFragment.FetchAndSaveElectionUseCase
import org.aossie.agoraandroid.domain.useCases.homeFragment.GetActiveElectionsCountUseCase
import org.aossie.agoraandroid.domain.useCases.homeFragment.GetFinishedElectionsCountUseCase
import org.aossie.agoraandroid.domain.useCases.homeFragment.GetPendingElectionsCountUseCase
import org.aossie.agoraandroid.domain.useCases.homeFragment.GetTotalElectionsCountUseCase
import org.aossie.agoraandroid.domain.useCases.homeFragment.HomeFragmentUseCases
import org.aossie.agoraandroid.domain.useCases.homeFragment.LogOutUseCase
import org.aossie.agoraandroid.domain.useCases.profile.ChangeAvatarUseCase
import org.aossie.agoraandroid.domain.useCases.profile.ChangePasswordUseCase
import org.aossie.agoraandroid.domain.useCases.profile.GetUserDataUseCase
import org.aossie.agoraandroid.domain.useCases.profile.ProfileUseCases
import org.aossie.agoraandroid.domain.useCases.profile.ToggleTwoFactorAuthUseCase
import org.aossie.agoraandroid.domain.useCases.profile.UpdateUserUseCase
import org.aossie.agoraandroid.domain.useCases.electionDetails.DeleteElectionUseCase
import org.aossie.agoraandroid.domain.useCases.electionDetails.ElectionDetailsUseCases
import org.aossie.agoraandroid.domain.useCases.electionDetails.GetBallotsUseCase
import org.aossie.agoraandroid.domain.useCases.electionDetails.GetElectionByIdUseCase
import org.aossie.agoraandroid.domain.useCases.electionDetails.GetResultUseCase
import org.aossie.agoraandroid.domain.useCases.electionDetails.GetVotersUseCase
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
    return ElectionsRepositoryImpl(api, appDatabase, preferenceProvider)
  }

  @Provides
  @Singleton
  fun providesElectionsRepositoryImpl(
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
  fun provideLogInUseCases(
    faceBookLogInUseCase: FaceBookLogInUseCase,
    getUserUseCase: GetUserUseCase,
    refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    saveUserUseCase: SaveUserUseCase,
    userLogInUseCase: UserLogInUseCase
  ): LogInUseCases {
    return LogInUseCases(
      faceBookLogInUseCase,
      getUserUseCase,
      refreshAccessTokenUseCase,
      saveUserUseCase,
      userLogInUseCase
    )
  }

  @Provides
  @Singleton
  fun provideSignUpUseCase(repository: UserRepository): SignUpUseCase {
    return SignUpUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideTwoFactorAuthUseCases(
    resendOTPUseCase: ResendOTPUseCase,
    verifyOTPUseCase: VerifyOTPUseCase,
    saveUserUseCase: SaveUserUseCase,
    getUserUseCase: GetUserUseCase
  ): TwoFactorAuthUseCases {
    return TwoFactorAuthUseCases(
      resendOTPUseCase,
      verifyOTPUseCase,
      saveUserUseCase,
      getUserUseCase
    )
  }

  @Provides
  @Singleton
  fun provideSendForgotPasswordLinkUseCase(repository: UserRepository): SendForgotPasswordLinkUseCase {
    return SendForgotPasswordLinkUseCase(repository)
  }

  @Provides
  @Singleton
  fun provideProfileUseCases(
    saveUserUseCase: SaveUserUseCase,
    changePasswordUseCase: ChangePasswordUseCase,
    changeAvatarUseCase: ChangeAvatarUseCase,
    getUserUseCase: GetUserUseCase,
    toggleTwoFactorAuthUseCase: ToggleTwoFactorAuthUseCase,
    updateUserUseCase: UpdateUserUseCase,
    getUserDataUseCase: GetUserDataUseCase
  ): ProfileUseCases {
    return ProfileUseCases(
      saveUserUseCase,
      changePasswordUseCase,
      changeAvatarUseCase,
      getUserUseCase,
      toggleTwoFactorAuthUseCase,
      updateUserUseCase,
      getUserDataUseCase
    )
  }

  @Provides
  @Singleton
  fun provideCreateElectionUseCase(electionsRepository: ElectionsRepository): CreateElectionUseCase {
    return CreateElectionUseCase(electionsRepository)
  }

  @Provides
  @Singleton
  fun provideHomeFragmentUseCases(
    fetchAndSaveElectionUseCase: FetchAndSaveElectionUseCase,
    getActiveElectionsCountUseCase: GetActiveElectionsCountUseCase,
    getFinishedElectionsCountUseCase: GetFinishedElectionsCountUseCase,
    getPendingElectionsCountUseCase: GetPendingElectionsCountUseCase,
    getTotalElectionsCountUseCase: GetTotalElectionsCountUseCase,
    deleteUserUseCase: DeleteUserUseCase,
    logOutUseCase: LogOutUseCase
  ): HomeFragmentUseCases {
    return HomeFragmentUseCases(
      fetchAndSaveElectionUseCase,
      getActiveElectionsCountUseCase,
      getFinishedElectionsCountUseCase,
      getPendingElectionsCountUseCase,
      getTotalElectionsCountUseCase,
      deleteUserUseCase,
      logOutUseCase
    )
  }

  @Provides
  @Singleton
  fun provideDisplayElectionsUseCases(
    getActiveElectionsUseCase: GetActiveElectionsUseCase,
    getFinishedElectionsUseCase: GetFinishedElectionsUseCase,
    getPendingElectionsUseCase: GetPendingElectionsUseCase
  ): DisplayElectionsUseCases {
    return DisplayElectionsUseCases(
      getActiveElectionsUseCase,
      getFinishedElectionsUseCase,
      getPendingElectionsUseCase
    )
  }

  @Provides
  @Singleton
  fun provideElectionsUseCases(
    fetchAndSaveElectionUseCase: FetchAndSaveElectionUseCase,
    getElectionsUseCase: GetElectionsUseCase
  ): ElectionsUseCases {
    return ElectionsUseCases(
      fetchAndSaveElectionUseCase, getElectionsUseCase
    )
  }

  @Provides
  @Singleton
  fun providesElectionDetailsUseCases(
    deleteElectionUseCase: DeleteElectionUseCase,
    getBallotsUseCase: GetBallotsUseCase,
    getElectionByIdUseCase: GetElectionByIdUseCase,
    getResultUseCase: GetResultUseCase,
    getVotersUseCase: GetVotersUseCase
  ): ElectionDetailsUseCases {
    return ElectionDetailsUseCases(
      deleteElectionUseCase,
      getBallotsUseCase,
      getElectionByIdUseCase,
      getResultUseCase,
      getVotersUseCase
    )
  }
}
