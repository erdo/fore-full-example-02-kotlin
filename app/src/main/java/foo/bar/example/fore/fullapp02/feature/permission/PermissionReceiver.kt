package foo.bar.example.fore.fullapp02.feature.permission

import android.app.Activity
import android.content.Context
import co.early.fore.kt.core.callbacks.Failure
import co.early.fore.kt.core.callbacks.Success

/**
 *
 * Copyright © 2019 early.co. All rights reserved.
 */

interface PermissionReceiver {

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    )

    fun permissionsGranted(vararg permissionTypes: Permission.Type): Boolean

    fun permissionRequest(
        activity: Activity,
        success: Success,
        failure: Failure,
        instantFailure: Failure,
        vararg permissionTypes: Permission.Type
    )

    fun permissionClearUp()

    fun permissionShowAppSettingsScreen(context: Context)
}
