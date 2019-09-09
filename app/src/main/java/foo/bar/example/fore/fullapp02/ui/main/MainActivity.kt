package foo.bar.example.fore.fullapp02.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.activity.SyncableAppCompatActivity
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.ui.login.LoginActivity


/**
 * For the tab content in this example we manage fore observers at the **Fragment** level,
 * However we still want the bottom navigation bar to be reactive, so we ask it to observe
 * a few models
 * here so that it can keep its badges in sync - SyncableAppCompatActivity does most
 * of the leg work for us
 */
class MainActivity : SyncableAppCompatActivity() {

    private lateinit var authentication: Authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authentication = App.inst.appComponent.authentication

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

//    private fun setupNavigationController(savedInstanceState: Bundle?) {
//
//        val navHostFragment = (navigationHostFragment as NavHostFragment)
//        val navController = navHostFragment.navController
//        val appBarConfiguration = AppBarConfiguration(
//            topLevelDestinationIds = setOf(
//                R.id.navigation_fruit,
//                R.id.navigation_basket,
//                R.id.navigation_todo
//            )
//        )
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navigationView.setupWithNavController(navController)
//    }
//
//    override fun onSupportNavigateUp() = findNavController(R.id.navigationHostFragment).navigateUp()


    /**
     * SyncableAppCompatActivity methods
     */

    override fun getResourceIdForSyncableView(): Int {
        return R.layout.activity_main
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(
            App.inst.appComponent.fruitCollectorModel,
            App.inst.appComponent.todoListModel
            // note that we are not observing the basket model as it has been implemented
            // as a locally scoped model that doesn't exist when we are not on the basket view
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

    companion object {

        fun start(context: Context) {
            val intent = build(context)
            context.startActivity(intent)
        }

        fun build(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
