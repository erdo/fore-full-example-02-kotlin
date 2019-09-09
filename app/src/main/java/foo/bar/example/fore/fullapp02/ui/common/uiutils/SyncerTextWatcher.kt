package foo.bar.example.fore.fullapp02.ui.common.uiutils

import android.text.Editable
import android.text.TextWatcher

import co.early.fore.core.ui.SyncableView

/**
 *
 */
class SyncerTextWatcher(private val syncableView: SyncableView) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        //no opp
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //no opp
    }

    override fun afterTextChanged(s: Editable) {
        syncableView.syncView()
    }
}
