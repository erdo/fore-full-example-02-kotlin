package foo.bar.example.fore.fullapp02.ui.main

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import co.early.fore.core.observer.Observer
import co.early.fore.core.ui.SyncableView
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
) : ConstraintLayout (context!!, attrs, defStyleAttr), SyncableView {

    private val todoListModel: TodoListModel by inject()
    private val fruitCollectorModel: FruitCollectorModel by inject()

    //single observer reference
    var observer = Observer { syncView() }

    internal fun setSelectedItemId(resId: Int) {
        navigationView.selectedItemId = resId
    }

    //reactive ui stuff below

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        fruitCollectorModel.addObserver(observer)
        todoListModel.addObserver(observer)
        syncView() //  <- don't forget this
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        fruitCollectorModel.removeObserver(observer)
        todoListModel.removeObserver(observer)
    }

    override fun syncView() {
        if (!isInEditMode) {
            navigationView.syncView()
        }
    }
}
