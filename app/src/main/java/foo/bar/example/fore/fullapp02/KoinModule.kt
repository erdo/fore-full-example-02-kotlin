package foo.bar.example.fore.fullapp02

import co.early.fore.core.WorkMode
import co.early.fore.core.logging.AndroidLogger
import co.early.fore.core.logging.Logger
import co.early.fore.core.time.SystemTimeWrapper
import co.early.fore.retrofit.CallProcessor
import co.early.fore.retrofit.InterceptorLogging
import foo.bar.example.fore.fullapp02.api.CustomGlobalErrorHandler
import foo.bar.example.fore.fullapp02.api.CustomGlobalRequestInterceptor
import foo.bar.example.fore.fullapp02.api.CustomRetrofitBuilder
import foo.bar.example.fore.fullapp02.api.authentication.AuthenticationService
import foo.bar.example.fore.fullapp02.api.fruits.FruitService
import foo.bar.example.fore.fullapp02.feature.basket.BasketModel
import foo.bar.example.fore.fullapp02.feature.csv.ReaderWriter
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.feature.permission.Permission
import foo.bar.example.fore.fullapp02.feature.permission.SystemPermissionWrapper
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListExporter
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.io.File

val appModule = module(override = true) {

    /**
     * Common
     */

    single { androidContext() as App }

    single { WorkMode.ASYNCHRONOUS }

    single<Logger> { AndroidLogger("forefoo_") }

    single {
        CustomRetrofitBuilder.create(
            CustomGlobalRequestInterceptor(get()),
            InterceptorLogging(get())
        )//logging interceptor should be the last one
    }

    single { CallProcessor(CustomGlobalErrorHandler(get()), get()) }

    single { SystemTimeWrapper() }

    single { SystemPermissionWrapper() }

    single { Permission(get(), get(), get(), get()) }


    /**
     * Features
     */

    single { Authentication(get(), get(), get(), get()) }
    single { FruitCollectorModel(get(), get(), get(), get(), get()) }
    single { TodoListModel(get(), get()) }
    single { TodoListExporter(get(), get(), get(), get(), get()) }
    single {
        ReaderWriter(
            get(),
            File((get() as App).filesDir.path) // on the device see data/data/[apppackage]/files
        )
    } //testing path Environment.getDataDirectory()


    /**
     * Services
     */

    single { (get() as Retrofit).create(AuthenticationService::class.java) }
    single { (get() as Retrofit).create(FruitService::class.java) }


    /**
     * ViewModels
     */

    viewModel { BasketModel(get(), WorkMode.SYNCHRONOUS) }
}




