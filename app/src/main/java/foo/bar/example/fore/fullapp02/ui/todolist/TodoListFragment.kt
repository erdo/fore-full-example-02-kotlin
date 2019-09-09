package foo.bar.example.fore.fullapp02.ui.todolist

import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.fragment.SyncableSupportFragment
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.R


/**
 * For the todolist example we manage fore observers at the **Fragment** level
 * using: [SyncableSupportFragment]
 */
class TodoListFragment : SyncableSupportFragment() {

    override fun getResourceIdForSyncableView(): Int {
        return R.layout.fragment_todolist
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(
            App.inst.appComponent.todoListModel
        )
    }

    companion object {
        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }
}
