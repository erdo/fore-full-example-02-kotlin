package foo.bar.example.fore.fullapp02.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.view.SyncViewXActivity
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.feature.permission.Permission
import foo.bar.example.fore.fullapp02.feature.permission.PermissionReceiver
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import foo.bar.example.fore.fullapp02.ui.login.LoginActivity
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * For the tab content in this example we manage fore observers at the **Fragment** level,
 * However we still want the bottom navigation bar to be reactive, so we ask it to observe
 * a few models
 * here so that it can keep its badges in sync - SyncViewXActivity does most
 * of the leg work for us
 */

class MainActivity : SyncViewXActivity(), PermissionReceiver by permission {

    private val authentication: Authentication by inject()
    private val todoListModel: TodoListModel by inject()
    private val fruitCollectorModel: FruitCollectorModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavigation(savedInstanceState)
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
     * Navigation methods
     */

    private fun setupNavigation(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {//set initial state of navigation
            (syncableView as MainView).setSelectedItemId(R.id.navigation_fruit)
        }
    }

    /**
     * SyncableAppCompatActivity methods
     */

    override fun getResourceIdForSyncableView(): Int {
        return R.layout.activity_main
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(
            fruitCollectorModel,
            todoListModel
            // note that we are not observing the basket model as it has been implemented
            // as a locally scoped view model that doesn't exist when we are not on the basket view
        )
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
