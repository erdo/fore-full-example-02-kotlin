package foo.bar.example.fore.fullapp02.feature.login

import arrow.core.Either
import co.early.fore.kt.core.logging.Logger
import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.callbacks.FailureWithPayload
import co.early.fore.kt.core.callbacks.Success
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.retrofit.CallProcessor
import foo.bar.example.fore.fullapp02.api.authentication.AuthenticationService
import foo.bar.example.fore.fullapp02.api.authentication.SessionRequestPojo
import foo.bar.example.fore.fullapp02.message.UserMessage
/**
 * All the unit testable logic and data for Authentication,
 * connects to the network via CallProcessor (which is mockable for unit tests)
 *
 * (this class knows nothing about views, contexts, nor anything to do with the android)
 */
class Authentication(
    private val authenticationService: AuthenticationService,
    private val callProcessor: CallProcessor<UserMessage>,
    private val logger: Logger
) : Observable by ObservableImp() {

    var sessionToken = ""
        private set
    var isBusy = false
        private set


    fun login(
        username: String,
        password: String,
        success: Success,
        failureWithPayload: FailureWithPayload<UserMessage>
    ) {

        logger.i("login()")


        if (isBusy) {
            failureWithPayload(UserMessage.ERROR_BUSY)
            return
        }

        isBusy = true
        notifyObservers()


        launchMain {

            // this is the network call, CallProcessor handles a lot of the complication
            // and lets us mock network calls during tests
            val result = callProcessor.processCallAwait {
                authenticationService.getSessionToken(
                    SessionRequestPojo(username, password),
                    "3s"
                )
            }

            when (result) {
                is Either.Left -> {
                    failureWithPayload(result.a)
                    complete()
                }
                is Either.Right -> {
                    sessionToken = result.b.sessionToken
                    success()
                    complete()
                }
            }
        }

    }

    fun logout() {

        launchMain {

            // this is the network call, CallProcessor handles a lot of the complication
            // and lets us mock network calls during tests
            val result = callProcessor.processCallAwait {
                authenticationService.invalidateSessionToken("3s")
            }

            when (result) {
                is Either.Left -> {} //do nothing
                is Either.Right -> {} //do nothing - but in reality, maybe warn user that they weren't properly logged out

            }
        }
        sessionToken = ""
        notifyObservers()
    }

    private fun complete() {

        logger.i("complete()")

        isBusy = false
        notifyObservers()
    }

    fun hasSessionToken(): Boolean {
        return sessionToken.length != 0
    }

    fun isEmail(emailCandidate: String?): Boolean {
        return if (emailCandidate == null) false else emailCandidate.contains("@") && emailCandidate.contains(
            ""
        ) && emailCandidate.length > 4
    }

    fun isPassword(passwordCandidate: String?): Boolean {
        return if (passwordCandidate == null) false else passwordCandidate.length > 0
    }
}
