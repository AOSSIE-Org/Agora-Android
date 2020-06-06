package org.aossie.agoraandroid.di.modules

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import org.aossie.agoraandroid.di.utils.MainFragmentFactory

@Module
object FragmentModule{

    @JvmStatic
    @Provides
    fun providesMainFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return MainFragmentFactory(
            viewModelFactory
        )
    }


}