package foo.bar.example.fore.fullapp02.ui.main


import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import co.early.fore.core.ui.SyncableView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import foo.bar.example.fore.fullapp02.ui.basket.BasketFragment
import foo.bar.example.fore.fullapp02.ui.common.uiutils.ViewUtils
import foo.bar.example.fore.fullapp02.ui.common.uiutils.inject
import foo.bar.example.fore.fullapp02.ui.common.widget.BottomNavigationViewWithBadges
import foo.bar.example.fore.fullapp02.ui.fruitcollector.FruitCollectorFragment
import foo.bar.example.fore.fullapp02.ui.todolist.TodoListFragment
import org.koin.android.ext.android.inject

/**
 *
 */
class GlobalBottomNavigationView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) :
    BottomNavigationViewWithBadges(context, attrs, defStyleAttr), SyncableView {

    private val fruitCollectorModel: FruitCollectorModel by inject()
    private val todoListModel: TodoListModel by inject()

    private val fruitCollectorFragment = FruitCollectorFragment.newInstance()
    private val basketFragment = BasketFragment.newInstance()
    private val todoListFragment = TodoListFragment.newInstance()

    private val itemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_fruit -> {
                this@GlobalBottomNavigationView.setFragment(fruitCollectorFragment, FruitCollectorFragment::class.java.simpleName)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_basket -> {
                this@GlobalBottomNavigationView.setFragment(basketFragment, BasketFragment::class.java.simpleName)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_todo -> {
                this@GlobalBottomNavigationView.setFragment(todoListFragment, TodoListFragment::class.java.simpleName)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        setOnNavigationItemSelectedListener(itemSelectedListener)
    }

    private fun setFragment(fragment: Fragment, fragmentTag: String) {
       (ViewUtils.getActivityFromContext(context) as MainActivity).setFragment(fragment, fragmentTag)
    }

    override fun syncView() {
        setBadge(0, fruitCollectorModel.totalFruitCount)
        setBadge(1, 0)
        setBadge(2, todoListModel.numberOfItemsStillToDo())
    }
}
