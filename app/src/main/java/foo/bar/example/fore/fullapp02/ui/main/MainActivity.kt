package foo.bar.example.fore.fullapp02.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.feature.permission.Permission
import foo.bar.example.fore.fullapp02.feature.permission.PermissionReceiver
import foo.bar.example.fore.fullapp02.ui.login.LoginActivity
import org.koin.core.KoinComponent
import org.koin.core.inject


class MainActivity : AppCompatActivity(R.layout.activity_main), PermissionReceiver by permission {

    private val authentication: Authentication by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {//set initial state of navigation
            findViewById<MainView>(R.id.container).setSelectedItemId(R.id.navigation_fruit)
        }
    }


    /**
     * this gets called by the global navigation
     */

    internal fun setFragment(fragment: Fragment, fragmentTag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.content_main,
            fragment,
            fragmentTag
        )
        fragmentTransaction.commitAllowingStateLoss()
    }


    /**
     * Handling logout menu
     */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (handleMenu(item)) true else super.onOptionsItemSelected(item)
    }

    private fun handleMenu(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_logout -> {
                authentication.logout()
                LoginActivity.start(this)
            }
            else -> return false
        }
        return true
    }


    /**
     * Prevents a malicious user starting this activity
     * when they are not logged in
     */
    override fun onResume() {
        super.onResume()
        if (!authentication.hasSessionToken()) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionClearUp()//because we could be waiting for outstanding permissions
    }

    companion object : KoinComponent {

        private val permission: Permission by inject()

        fun start(context: Context) {
            val intent = build(context)
            context.startActivity(intent)
        }

        fun build(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
