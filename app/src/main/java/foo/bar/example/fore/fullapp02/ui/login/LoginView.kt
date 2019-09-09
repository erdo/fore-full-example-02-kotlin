package foo.bar.example.fore.fullapp02.ui.login

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ScrollView
import android.widget.Toast
import co.early.fore.core.callbacks.FailureCallbackWithPayload
import co.early.fore.core.callbacks.SuccessCallback
import co.early.fore.core.logging.Logger
import co.early.fore.core.ui.SyncableView
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.ui.common.uiutils.SyncerCheckChanged
import foo.bar.example.fore.fullapp02.ui.common.uiutils.SyncerTextWatcher
import foo.bar.example.fore.fullapp02.ui.common.uiutils.ViewUtils
import foo.bar.example.fore.fullapp02.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.view.*

/**
 * For the login example we manage fore observers at the **Activity** level
 * [LoginActivity]
 */
class LoginView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ScrollView(context, attrs, defStyleAttr), SyncableView {

    private lateinit var authentication: Authentication
    private lateinit var logger: Logger


    public override fun onFinishInflate() {// grab a reference to all the view elements, setup buttons listeners
        super.onFinishInflate()

        setupModelReferences()

        setupClickListeners()
    }

    private fun setupModelReferences() {
        authentication = App.inst.appComponent.authentication
        logger = App.inst.appComponent.logger
    }

    private fun setupClickListeners() {

        login_login_button.setOnClickListener {

            logger.i(TAG, "loginButton clicked")

            val emailStr = login_email_edittext.text.toString()
            val passwordStr = login_password_edittext.text.toString()

            authentication.login(
                emailStr, passwordStr,
                successCallback = SuccessCallback {
                    MainActivity.start(this@LoginView.context)
                    ViewUtils.getActivityFromContext(this@LoginView.context)?.finish()
                },
                failureCallbackWithPayload = FailureCallbackWithPayload { failureMessage ->
                    Toast.makeText(
                        this@LoginView.context,
                        "Login Failed, reason:" + failureMessage,
                        Toast.LENGTH_LONG
                    ).show()
                })
        }

        login_email_edittext.addTextChangedListener(SyncerTextWatcher(this))
        login_password_edittext.addTextChangedListener(SyncerTextWatcher(this))
        login_password_edittext.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {//just helps with small screens that can't see the login button properly
                this@LoginView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
        login_showpass_switch.setOnCheckedChangeListener(SyncerCheckChanged(this))

    }

    override fun syncView() {

        logger.i(TAG, "syncView()")

        if (!isInEditMode) {

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
    }

    companion object {
        var TAG = LoginView::class.java.simpleName
    }


    /**
     * For the login view we are managing fore observers at the **Activity**
     * level, so the below is not required
     */
    //    @Override
    //    protected void onAttachedToWindow() {
    //        super.onAttachedToWindow();
    //        authentication.addObserver(observer);
    //        syncView();  // <- don't forget this
    //    }
    //
    //
    //    @Override
    //    protected void onDetachedFromWindow() {
    //        super.onDetachedFromWindow();
    //        authentication.removeObserver(observer);
    //    }
}
