package foo.bar.example.fore.fullapp02

import android.app.Application
import co.early.fore.core.WorkMode
import co.early.fore.core.logging.AndroidLogger
import co.early.fore.core.logging.Logger
import co.early.fore.core.time.SystemTimeWrapper
import co.early.fore.retrofit.CallProcessor
import co.early.fore.retrofit.InterceptorLogging
import dagger.Module
import dagger.Provides
import foo.bar.example.fore.fullapp02.api.CustomGlobalErrorHandler
import foo.bar.example.fore.fullapp02.api.CustomGlobalRequestInterceptor
import foo.bar.example.fore.fullapp02.api.CustomRetrofitBuilder
import foo.bar.example.fore.fullapp02.api.authentication.AuthenticationService
import foo.bar.example.fore.fullapp02.api.fruits.FruitService
import foo.bar.example.fore.fullapp02.message.UserMessage
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class AppModule(private val app: Application) {


    /**
     * Common
     */

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideWorkMode(): WorkMode {
        return WorkMode.ASYNCHRONOUS
    }

    @Provides
    @Singleton
    fun provideLogger(androidLogger: AndroidLogger): Logger {
        return androidLogger
    }

    @Provides
    @Singleton
    fun provideAndroidLogger(): AndroidLogger {
        val androidLogger = AndroidLogger("forefull02_")
        androidLogger.i(LOG_TAG, "created logger")
        return androidLogger
    }

    @Provides
    @Singleton
    fun provideRetrofit(logger: Logger): Retrofit {
        logger.i(LOG_TAG, "provideRetrofit()")
        return CustomRetrofitBuilder.create(
            CustomGlobalRequestInterceptor(logger),
            InterceptorLogging(logger)
        )//logging interceptor should be the last one
    }

    @Provides
    @Singleton
    fun provideCallProcessor(logger: Logger): CallProcessor<UserMessage> {
        logger.i(LOG_TAG, "provideCallProcessor()")
        return CallProcessor(CustomGlobalErrorHandler(logger), logger)
    }

    @Provides
    @Singleton
    fun provideSystemTimeWrapper(): SystemTimeWrapper {
        return SystemTimeWrapper()
    }

    /**
     * Services
     */

    @Provides
    @Singleton
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    @Singleton
    fun provideFruitService(retrofit: Retrofit): FruitService {
        return retrofit.create(FruitService::class.java)
    }


    companion object {
        val LOG_TAG = AppModule::class.java.simpleName
    }
}
