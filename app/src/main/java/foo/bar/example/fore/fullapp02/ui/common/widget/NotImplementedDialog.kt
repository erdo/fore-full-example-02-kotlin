package foo.bar.example.fore.fullapp02.ui.common.widget

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import foo.bar.example.fore.fullapp02.R

/**
 *
 * From within a view:
 *
 * (context as MainActivity).supportFragmentManager?.let {
 *    NotImplementedDialog.newInstance().show(it, NotImplementedDialog::class.java.simpleName)
 * }
 *
 *
 * Copyright © 2019 early.co. All rights reserved.
 */
class NotImplementedDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = buildDialog()
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        return dialog
    }

    private fun buildDialog(): Dialog {

        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.common_dialog_title)
        builder.setMessage(R.string.common_not_implemented_msg)
        builder.setPositiveButton(R.string.common_ok) { dialog, _ -> dialog.dismiss() }
        return builder.create()
    }

    companion object {
        fun newInstance(): NotImplementedDialog {
            return NotImplementedDialog()
        }
    }

}
