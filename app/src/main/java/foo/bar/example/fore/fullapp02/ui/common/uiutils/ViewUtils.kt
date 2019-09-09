package foo.bar.example.fore.fullapp02.ui.common.uiutils

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity


object ViewUtils {

    fun getActivityFromContext(context: Context): AppCompatActivity? {

        val appCompatActivity: AppCompatActivity?

        if (context is AppCompatActivity) {//this maybe a context from a view hosted in a regular fragment for example
            appCompatActivity = context
        } else if (context is ContextWrapper) {//this maybe a context from a view hosted in a dialogfragment for example
            appCompatActivity = context.baseContext as AppCompatActivity
        } else {//some other kind of context
            appCompatActivity = null
        }

        return appCompatActivity
    }
}
