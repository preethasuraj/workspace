package com.preethasuraj.watchlist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point for Hilt. Annotating it with [HiltAndroidApp] triggers
 * generation of the application-level dependency container that every other
 * Hilt component is rooted in.
 */
@HiltAndroidApp
class WatchListApplication : Application()
