package foo.bar.example.fore.fullapp02.feature.permission

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat

/**
 *
 * This enables us to set a mock the system permissions for testing.
 *
 * Copyright © 2019 early.co. All rights reserved.
 */
class SystemPermissionWrapper {

    fun checkSelfPermission(context: Context, permission: String): Int {
        return ActivityCompat.checkSelfPermission(context, permission)
    }

    fun requestPermissions(
        activity: Activity,
        permission: ArrayList<Permission.Type>, requestCode: Int
    ) {

        ActivityCompat.requestPermissions(
            activity,
            permission.map {
                it.androidMagicString
            }.toTypedArray(),
            requestCode
        )
    }
}
