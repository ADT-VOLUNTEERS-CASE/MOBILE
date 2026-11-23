package org.adt.presentation.di

import android.app.Application
import coil.imageLoader
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {
    internal val versionedCoverImageLoader by lazy {
        imageLoader.newBuilder()
            .respectCacheHeaders(false)
            .build()
    }
}
