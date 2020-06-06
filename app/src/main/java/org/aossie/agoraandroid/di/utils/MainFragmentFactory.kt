package org.aossie.agoraandroid.di.utils

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class MainFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

}