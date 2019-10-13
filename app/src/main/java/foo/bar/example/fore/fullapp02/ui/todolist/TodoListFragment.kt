package foo.bar.example.fore.fullapp02.ui.todolist

import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.view.SyncViewXFragment
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListExporter
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import org.koin.android.ext.android.inject


/**
 * For the todolist example we manage fore observers at the **Fragment** level
 * but we sync at the view level [SyncViewXFragment]
 */
class TodoListFragment : SyncViewXFragment() {

    private val todoListModel: TodoListModel by inject()
    private val todoListExporter: TodoListExporter by inject()

    override fun getResourceIdForSyncableView(): Int {
        return R.layout.fragment_todolist
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(
            todoListExporter,
            todoListModel
        )
    }

    companion object {
        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }
}
