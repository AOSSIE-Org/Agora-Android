package org.aossie.agoraandroid.koin

import android.app.Application
import com.google.gson.GsonBuilder
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.login.LoginViewModel
import org.aossie.agoraandroid.networking.Networking
import org.aossie.agoraandroid.remote.APIService
import org.aossie.agoraandroid.utilities.CacheManager
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

const val BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/"

val modules = module {

    fun provideApiService(): APIService {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(APIService::class.java)
    }

    single { Networking(provideApiService()) }
    single { SharedPrefs(androidContext()) }
    single { CacheManager(get()) }

    factory { LoadToast(androidContext()) }

    viewModel { LoginViewModel(get(), get()) }

}

class KoinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin {
            // Android context
            androidContext(this@KoinApplication)
            // modules
            modules(modules)
        }
    }
}