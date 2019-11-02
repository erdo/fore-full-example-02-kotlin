package foo.bar.example.fore.fullapp02

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Try not to fill this class with lots of code, if possible move it to a model somewhere
 *
 * Copyright © 2019 early.co. All rights reserved.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        inst = this

        startKoin {
            // Use Koin Android Logger
            androidLogger()
            // declare Android context
            androidContext(this@App)
            // declare modules to use
            modules(appModule)
        }

    }

//    fun injectTestAppModule(testAppModule: AppModule) {
//       // appComponent = DaggerAppComponent.builder().appModule(testAppModule).build()
//    }

    companion object {

        lateinit var inst: App private set

        fun init() {

            // run any initialisation code here

        }
    }
}
