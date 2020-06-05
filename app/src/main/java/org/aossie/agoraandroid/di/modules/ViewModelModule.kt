package org.aossie.agoraandroid.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import org.aossie.agoraandroid.di.utils.ViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}