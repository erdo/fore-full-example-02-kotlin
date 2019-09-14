package foo.bar.example.fore.fullapp02.ui.todolist

import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.fragment.SyncableSupportFragment
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import org.koin.android.ext.android.inject


/**
 * For the todolist example we manage fore observers at the **Fragment** level
 * using: [SyncableSupportFragment]
 */
class TodoListFragment : SyncableSupportFragment() {

    private val todoListModel: TodoListModel by inject()

    override fun getResourceIdForSyncableView(): Int {
        return R.layout.fragment_todolist
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(
            todoListModel
        )
    }

    companion object {
        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }
}
