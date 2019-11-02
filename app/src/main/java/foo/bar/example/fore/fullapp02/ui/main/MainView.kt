package foo.bar.example.fore.fullapp02.ui.main

import android.content.Context
import android.util.AttributeSet
import co.early.fore.core.ui.SyncableView
import co.early.fore.lifecycle.LifecycleSyncer
import co.early.fore.lifecycle.view.SyncConstraintLayout
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import foo.bar.example.fore.fullapp02.ui.common.uiutils.inject
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * To update the badges on the main navigation view,
 * we manage fore observers at the **View** level
 */
class MainView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    SyncConstraintLayout
        (context, attrs, defStyleAttr), SyncableView {

    private val todoListModel: TodoListModel by inject()
    private val fruitCollectorModel: FruitCollectorModel by inject()


    internal fun setSelectedItemId(resId: Int) {
        navigationView.selectedItemId = resId
    }

    override fun getThingsToObserve(): LifecycleSyncer.Observables {
        return LifecycleSyncer.Observables(
            fruitCollectorModel,
            todoListModel
            // note that we are not observing the basket model as it has been implemented
            // as a locally scoped view model that doesn't exist when we are not on the basket view
        )
    }

    override fun syncView() {
        if (!isInEditMode) {
            navigationView.syncView()
        }
    }
}
