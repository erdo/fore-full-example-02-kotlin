package foo.bar.example.fore.fullapp02.ui.login

import android.content.Context
import android.content.Intent
import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.activity.SyncableAppCompatActivity
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.R


/**
 * For the login example we manage fore observers at the **Activity** level
 * using: [SyncableAppCompatActivity]
 */
class LoginActivity : SyncableAppCompatActivity() {

    override fun getResourceIdForSyncableView(): Int {
        return R.layout.activity_login
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(App.inst.appComponent.authentication)
    }

    companion object {

        fun start(context: Context) {
            val intent = build(context)
            context.startActivity(intent)
        }

        fun build(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
