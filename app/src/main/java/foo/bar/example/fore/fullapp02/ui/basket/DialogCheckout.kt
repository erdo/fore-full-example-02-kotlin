package foo.bar.example.fore.fullapp02.ui.basket

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import foo.bar.example.fore.fullapp02.R

/**
 *
 */
class DialogCheckout : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = buildDialog()
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        return dialog
    }

    private fun buildDialog(): Dialog {

        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.common_dialog_title)
        builder.setMessage(R.string.basket_checkout_msg)
        builder.setPositiveButton(R.string.common_ok) { dialog, _ -> dialog.dismiss() }
        return builder.create()
    }

    companion object {

        fun newInstance(): DialogCheckout {
            return DialogCheckout()
        }
    }

}
