package foo.bar.example.fore.fullapp02.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import co.early.fore.kt.core.logging.Logger
import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.activity.SyncActivityX
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.ui.common.uiutils.SyncerCheckChanged
import foo.bar.example.fore.fullapp02.ui.common.uiutils.SyncerTextWatcher
import foo.bar.example.fore.fullapp02.ui.common.uiutils.ViewUtils
import foo.bar.example.fore.fullapp02.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject


/**
 * For the login example we manage fore observers at the **Activity** level
 * using: [SyncXActivity]
 */
class LoginActivity : SyncActivityX() {

    //models we need
    private val authentication: Authentication by inject()
    private val logger: Logger by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.hide()

        setContentView(layoutInflater.inflate(R.layout.activity_login, null))

        setupClickListeners()
    }

    private fun setupClickListeners() {

        login_login_button.setOnClickListener {

            logger.i(LOG_TAG, "loginButton clicked")

            val emailStr = login_email_edittext.text.toString()
            val passwordStr = login_password_edittext.text.toString()

            authentication.login(
                emailStr, passwordStr,
                success = {
                    MainActivity.start(this)
                    ViewUtils.getActivityFromContext(this)?.finish()
                },
                failureWithPayload = { failureMessage ->
                    Toast.makeText(
                        this,
                        "Login Failed, reason:" + failureMessage,
                        Toast.LENGTH_LONG
                    ).show()
                })
        }

        login_email_edittext.addTextChangedListener(SyncerTextWatcher(this))
        login_password_edittext.addTextChangedListener(SyncerTextWatcher(this))
        login_password_edittext.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {//just helps with small screens that can't see the login button properly
                login_container_scrollview.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
        login_showpass_switch.setOnCheckedChangeListener(SyncerCheckChanged(this))

    }

    override fun syncView() {

        logger.i(LOG_TAG, "syncView()")

        val emailValid = authentication.isEmail(login_email_edittext.text.toString())
        val passwordValid = authentication.isPassword(login_password_edittext.text.toString())
        val enableLoginButton = emailValid && passwordValid && !authentication.hasSessionToken()

        login_emailnotvalid_text.visibility = if (emailValid) View.INVISIBLE else View.VISIBLE
        login_login_button.isEnabled = enableLoginButton
        login_login_button.visibility =
            if (!authentication.isBusy) View.VISIBLE else View.INVISIBLE
        login_loading_progressbar.visibility =
            if (authentication.isBusy) View.VISIBLE else View.INVISIBLE
        login_password_edittext.inputType =
            if (login_showpass_switch.isChecked) InputType.TYPE_CLASS_TEXT else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        login_password_edittext.setSelection(login_password_edittext.text.length)
        login_email_edittext.isEnabled = !authentication.isBusy
        login_password_edittext.isEnabled = !authentication.isBusy
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(authentication)
    }

    companion object {

        var LOG_TAG = LoginActivity::class.java.simpleName

        fun start(context: Context) {
            val intent = build(context)
            context.startActivity(intent)
        }

        fun build(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
