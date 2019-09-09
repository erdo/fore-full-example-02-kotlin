package foo.bar.example.fore.fullapp02.ui.common.uiutils

import android.widget.CompoundButton
import co.early.fore.core.ui.SyncableView

/**
 *
 */
class SyncerCheckChanged(private val syncableView: SyncableView) : CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        syncableView.syncView()
    }
}
