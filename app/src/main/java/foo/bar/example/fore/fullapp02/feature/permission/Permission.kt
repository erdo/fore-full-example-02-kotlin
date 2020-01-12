package foo.bar.example.fore.fullapp02.feature.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import co.early.fore.core.logging.Logger
import co.early.fore.core.time.SystemTimeWrapper
import co.early.fore.kt.core.callbacks.Failure
import co.early.fore.kt.core.callbacks.Success
import foo.bar.example.fore.fullapp02.App
import org.koin.android.ext.android.inject
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import java.util.*


/**
 * <p>
 * Attempting to separate the permissions check from the UI code as much as possible. It's all a
 * bit shit, but I think it's an improvement on Android's default work none the less.
 * <p>
 * Firstly in order to use this class, you need your Activity (maybe a base activity) to add
 * a delegate to an instance of this class:
 *
 * <p>
 * <code>
 * class YourActivity: PermissionReceiver by permission {
 * </code>
 *
 * <p>
 * That permission object is going to have to be injected before the activity gets instantiated.
 * The way to do that with koin is to add the inject to a companion object like this:
 *
 * <p>
 * <code>
 * companion object : KoinComponent {
 *  private val permission: Permission by inject()
 * }
 * </code>
 *
 * <p>
 * Secondly, in the Activity's onDestroy() method, you should call permissionClearUp() in case
 * your users rotate the device before they get the android permissions request pop up.
 *
 * <p>
 * <code>
 * override fun onDestroy() {
 *    super.onDestroy()
 *    permissionClearUp()
 * }
 * </code>
 *
 * <p>
 * Once you've done that, you can use the same permission instance (from your activity or by
 * injecting it into a fragment or a view) to call the permissionsGranted() and permissionRequest()
 * functions as you wish.
 *
 * <p>
 * <code>
 *  mybutton.setOnClickListener { _ ->
 *    permission.permissionRequest(
 *      activity, //the one that delegates to permission
 *      {}, //success, do the thing
 *      {}, //failure, don't do the thing, explain to the user
 *      {}, //immediate failure, "Never Ask Again" was checked, explain and show app settings
 *      Permission.Type.WRITE_EXTERNAL_STORAGE //all the permissions required
 *    )
 *  }
 * </code>
 *
 * <p>
 * Android gives us no sane way of telling if the user has selected the "Never Ask Again"
 * check box on the permission pop-up. This is why we infer it based on the time it takes for the
 * permission to be denied.
 *
 * <p>
 * Don't forget you also need to add the "dangerous" permissions to the manifest, it's not enough
 * to request them via this class
 *
 * <p>
 *
 * Copyright © 2019 early.co. All rights reserved.
 */
class Permission(
    private val systemPermissionWrapper: SystemPermissionWrapper,
    private val systemTimeWrapper: SystemTimeWrapper,
    private val logger: Logger,
    private val app: App
) : PermissionReceiver {

    private var pendingRequest: PendingRequest? = null


    /**
     * Check to see if we have these permissions.
     *
     * Consider just calling [.permissionRequest]
     * which will check this method as part of its process.
     *
     * @param permissionTypes any number of permissions you wish to check
     * @return true if ALL permissions are granted, false if any or all of them are not granted
     */
    override fun permissionsGranted(vararg permissionTypes: Type): Boolean {

        logger.i(LOG_TAG, "permissionsGranted()")

        var allGranted = true

        for (permissionType in permissionTypes) {
            if (systemPermissionWrapper.checkSelfPermission(
                    app, permissionType.androidMagicString
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                logger.w(LOG_TAG, "$SPACER(NO)  $permissionType")
                allGranted = false
            } else {
                logger.i(LOG_TAG, "$SPACER(YES) $permissionType")
            }
        }

        return allGranted
    }

    /**
     * Checks (and requests if necessary) the permissions specified by the caller
     *
     * @param activity       must be an activity that conforms to the requirements detailed in the
     *                       docs for this class (i.e. delegating PermissionReceiver and overriding
     *                       onDestroy())
     * @param success        will be called immediately if ALL permissions are already granted, or
     *                       will be called at some point in the future if ALL permissions are
     *                       granted by the user
     * @param failure        called if the user declines to grant ANY of the permissions
     * @param instantFailure called if the user declines to grant ANY of the permissions and we
     *                       suspect that the user has selected the "Never Ask Again" checkbox
     * @param permissionTypes permission(s) that are required
     */
    override fun permissionRequest(
        activity: Activity,
        success: Success,
        failure: Failure,
        instantFailure: Failure,
        vararg permissionTypes: Type
    ) {

        logger.i(LOG_TAG, "permissionRequest() for " + permissionTypes.size + " permissions")

        //check if the activity implements the correct interface
        require(activity is PermissionReceiver) {
            "activity must implement PermissionReceiver, adding a delegate " +
                    "to a Permissions instance is the easiest method, see docs"
        }

        //check if we already have all of these permissions anyway
        if (permissionsGranted(*permissionTypes)) {
            success()
            return
        }

        permissionTypes.map {
            logger.i(LOG_TAG, SPACER + "permission pending: " + it)
        }

        pendingRequest = PendingRequest(
            systemTimeWrapper.currentTimeMillis(),
            success,
            failure,
            instantFailure,
            arrayListOf(*permissionTypes)
        )

        pendingRequest?.let {
            systemPermissionWrapper.requestPermissions(
                activity,
                it.permissionsRequested,
                REQUEST_CODE
            )
        }
    }

    /**
     * Activity.onRequestPermissionsResult() is delegated to here,
     * it let's us decouple the UI from the permissions request system
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        logger.i(
            LOG_TAG,
            "reportPermissionResult() for " + permissions.size + " permissions" +
                    " reqCode:" + requestCode
        )

        pendingRequest?.run {
            if (requestCode == REQUEST_CODE) {
                when {
                    allPermissionsGranted(grantResults) -> success()
                    systemTimeWrapper.currentTimeMillis() < timeStampMs
                            + AUTOMATICALLY_DENIED_WINDOW_MS -> instantFailure()
                    else -> failure()
                }
            }
        }

        pendingRequest = null
    }

    override fun permissionClearUp() {
        pendingRequest = null
    }

    override fun permissionShowAppSettingsScreen(context: Context) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.getPackageName(), null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun allPermissionsGranted(grantResults: IntArray): Boolean {

        var allGranted = true

        for (result in grantResults) {
            logger.i(
                LOG_TAG,
                SPACER + "grant result:" + (result == PackageManager.PERMISSION_GRANTED)
            )
            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
            }
        }

        if (!allGranted) {
            logger.w(LOG_TAG, "Some permissions were not granted")
        }

        return allGranted
    }

    private data class PendingRequest(
        val timeStampMs: Long,
        val success: Success,
        val failure: Failure,
        val instantFailure: Failure,
        val permissionsRequested: ArrayList<Type>
    )

    enum class Type constructor(val androidMagicString: String) {

        WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        //...add more permissions as we need them, don't forget to also put them in the manifest
        ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    //helps us access koin's inject() from here
    private inline fun <reified T : Any> inject(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null
    ) = App.inst.inject<T>(qualifier, parameters)


    companion object {

        private val LOG_TAG = Permission::class.java.simpleName
        private const val SPACER = "-- "
        private const val REQUEST_CODE = 4242
        // If the permission gets denied this quick, then it implies that the user has selected the
        // "Never Ask Again" check box on the permissions request
        private const val AUTOMATICALLY_DENIED_WINDOW_MS: Long = 500
    }

}
