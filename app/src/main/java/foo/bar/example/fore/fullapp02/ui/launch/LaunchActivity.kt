package foo.bar.example.fore.fullapp02.ui.launch

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.ui.common.uiutils.ViewUtils
import foo.bar.example.fore.fullapp02.ui.login.LoginActivity
import foo.bar.example.fore.fullapp02.ui.main.MainActivity
import org.koin.android.ext.android.inject

/**
 * Nothing to sync here, all this does is show a spinner while we check for the log in status of the
 * user, this work is done in [LaunchView]
 */
class LaunchActivity : AppCompatActivity() {

    //models we need
    private val authentication: Authentication by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.hide()

        setContentView(layoutInflater.inflate(R.layout.activity_launch, null))

        //redirect based on logged in status
        Handler().postDelayed({
            if (authentication.hasSessionToken()) {
                MainActivity.start(this)
            } else {
                LoginActivity.start(this)
            }
            ViewUtils.getActivityFromContext(this)?.finish()
        }, 1000)
    }

}
