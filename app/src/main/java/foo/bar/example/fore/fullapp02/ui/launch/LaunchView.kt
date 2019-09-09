package foo.bar.example.fore.fullapp02.ui.launch

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.RelativeLayout
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.ui.common.uiutils.ViewUtils
import foo.bar.example.fore.fullapp02.ui.login.LoginActivity
import foo.bar.example.fore.fullapp02.ui.main.MainActivity

/**
 * This view has nothing to sync, if the user is logged in, they are sent to the
 * [MainActivity], if not they are sent to the [LoginActivity]
 */
class LaunchView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var authentication: Authentication


    public override fun onFinishInflate() {
        super.onFinishInflate()

        // grab a reference to any models we need here
        authentication = App.inst.appComponent.authentication

        //redirect based on logged in status
        Handler().postDelayed({
            if (authentication.hasSessionToken()) {
                MainActivity.start(context)
            } else {
                LoginActivity.start(context)
            }
            ViewUtils.getActivityFromContext(context)?.finish()
        }, 1000)
    }

}
