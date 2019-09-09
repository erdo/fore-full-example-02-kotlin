package foo.bar.example.fore.fullapp02.feature.login

import co.early.fore.core.Affirm
import co.early.fore.core.WorkMode
import co.early.fore.core.callbacks.FailureCallbackWithPayload
import co.early.fore.core.callbacks.SuccessCallback
import co.early.fore.core.logging.Logger
import co.early.fore.core.observer.ObservableImp
import co.early.fore.retrofit.CallProcessor
import foo.bar.example.fore.fullapp02.api.authentication.AuthenticationService
import foo.bar.example.fore.fullapp02.api.authentication.SessionRequestPojo
import foo.bar.example.fore.fullapp02.api.authentication.SessionResponsePojo
import foo.bar.example.fore.fullapp02.message.UserMessage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * All the unit testable logic and data for Authentication,
 * connects to the network via CallProcessor (which is mockable for unit tests)
 *
 * (this class knows nothing about views, contexts, nor anything to do with the android)
 */
@Singleton
class Authentication @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val callProcessor: CallProcessor<UserMessage>,
    private val workMode: WorkMode,
    private val logger: Logger
) : ObservableImp(workMode) {

    var sessionToken = ""
        private set
    var isBusy = false
        private set


    fun login(
        username: String,
        password: String,
        successCallback: SuccessCallback,
        failureCallbackWithPayload: FailureCallbackWithPayload<UserMessage>
    ) {

        logger.i(TAG, "login()")

        Affirm.notNull(username)
        Affirm.notNull(password)
        Affirm.notNull(failureCallbackWithPayload)

        if (isBusy) {
            failureCallbackWithPayload.fail(UserMessage.ERROR_BUSY)
            return
        }

        isBusy = true
        notifyObservers()

        // this is the network call, CallProcessor handles a lot of the complication
        // and lets us mock network calls during tests
        callProcessor.processCall<SessionResponsePojo>(authenticationService.getSessionToken(SessionRequestPojo(username, password),
            "3s"
        ),
            workMode,
            { successResponse ->
                sessionToken = successResponse.sessionToken
                successCallback.success()
                complete()
            }
        ) { failureMessage ->
            failureCallbackWithPayload.fail(failureMessage)
            complete()
        }

    }

    fun logout() {

        callProcessor.processCall(authenticationService.invalidateSessionToken("3s"), workMode,
            { successResponse ->
                //do nothing
            }
        ) { failureMessage ->
            //do nothing - but in reality, maybe warn user that they weren't properly logged out
        }

        sessionToken = ""
        notifyObservers()
    }


    private fun complete() {

        logger.i(TAG, "complete()")

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

    companion object {

        private val TAG = Authentication::class.java.simpleName
    }

}
