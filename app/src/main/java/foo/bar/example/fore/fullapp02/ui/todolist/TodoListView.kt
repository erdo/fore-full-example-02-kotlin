package foo.bar.example.fore.fullapp02.ui.todolist

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.early.fore.core.logging.Logger
import co.early.fore.core.ui.SyncableView
import co.early.fore.lifecycle.view.SyncViewXFragment
import foo.bar.example.fore.fullapp02.feature.permission.Permission
import foo.bar.example.fore.fullapp02.feature.todolist.TodoItem
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import foo.bar.example.fore.fullapp02.message.UserMessage
import foo.bar.example.fore.fullapp02.ui.common.uiutils.SyncerTextWatcher
import foo.bar.example.fore.fullapp02.ui.common.uiutils.inject
import kotlinx.android.synthetic.main.fragment_todolist.view.*


/**
 * For the todolist example we manage fore observers at the **Fragment** level
 * but we sync at the view level [SyncViewXFragment] [TodoListFragment]
 */
class TodoListView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr), SyncableView {

    //models that we need
    private val todoListModel: TodoListModel by inject()
    private val logger: Logger by inject()
    private val permission: Permission by inject()

    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var animationSet: AnimatorSet
    private lateinit var keyboard: InputMethodManager


    override fun onFinishInflate() {
        super.onFinishInflate()

        keyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setupClickListeners()

        setupAdapters()

        setupAnimations()
    }

    private fun setupClickListeners() {

        todo_add_button.setOnClickListener { this@TodoListView.showAddDropDown() }

        todo_hide_button.setOnClickListener { this@TodoListView.hideAddDropDown() }

        todo_create_button.setOnClickListener { this@TodoListView.addItem() }

        todo_clear_button.setOnClickListener { todoListModel.clear() }

        todo_showdone_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            todoListModel.setDisplayDoneItems(
                isChecked
            )
        }

        todo_export_button.setOnClickListener { v ->
            permission.permissionRequest(
                (context as Activity),
                {
                    Toast.makeText(
                        context,
                        "doing the stuff",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                {
                    Toast.makeText(
                        context,
                        UserMessage.ERROR_PERMISSION_DENIED.messageResId,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                { permission.permissionShowAppSettingsScreen(context) },
                Permission.Type.WRITE_EXTERNAL_STORAGE
            )
        }

        todo_description_edit.addTextChangedListener(SyncerTextWatcher(this))
        todo_description_edit.setOnEditorActionListener()
        { v, actionId, event ->
            if (actionId == IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                this@TodoListView.addItem()
                true
            } else {
                false
            }
        }
    }

    private fun setupAdapters() {

        todoListAdapter = TodoListAdapter(todoListModel, logger)

        val linearLayoutManager1 = LinearLayoutManager(context)
        linearLayoutManager1.orientation = RecyclerView.VERTICAL

        todo_list_recycleview.layoutManager = linearLayoutManager1
        todo_list_recycleview.adapter = todoListAdapter
    }

    private fun setupAnimations() {

        animationSet = AnimatorSet()
        animationSet.duration = 300
    }

    private fun addItem() {
        if (todo_create_button.isEnabled) {
            todoListModel.addItem(TodoItem(false, todo_description_edit.text.toString()))
            todo_description_edit.setText("")
        }
    }

    private fun showAddDropDown() {

        // NB you might notice that the next line conflicts with the advice followed elsewhere in
        // fore and explicitly mentioned here:
        // https://erdo.github.io/fore-project/05-code-review-checklist.html#adhoc-state-setting
        //
        // By setting the enabled state of the addButton here in an *adhoc* way (rather than using
        // the syncview() method together with a boolean state which is stored in a model and
        // accessed using a getter()), we are indeed sacrificing:
        //
        // 1) the ability to easily unit test this behaviour (e.g. how do we test that when the
        // "to do" editing drop down is displayed - we have correctly set the enabled state of the
        // addButton? well we would have to use espresso or similar, rather than junit.
        // 2) we will loose this enabled/disabled state completely when the view is rotated
        // 3) we risk introducing UI consistency bugs if the requirements for the addButton enabled
        // state get more complicated.
        // (for an in depth discussion of UI consistency bugs and why they happen, see here:
        // https://erdo.github.io/android-fore/03-reactive-uis.html#syncview)
        //
        // In this case I'm leaning towards thinking it's fine - partly because it's a state that's
        // very closely related to a temporary condition of the view (having the drop down visible),
        // and not related to the underlying TodoListModel. It also doesn't matter if we loose this
        // enabled/disabled state on rotation - we will also loose the temporary view condition on
        // which it is based (the visibility of the edit drop down) - and I think that also doesn't
        // matter from a UX perspective. So in full knowledge, we'll take a small risk here and
        // save ourselves some code (recognising that the more code you add, the more
        // cluttered and risky a codebase becomes anyway).
        //
        todo_add_button.isEnabled = false
        todo_description_edit.requestFocus()
        keyboard.showSoftInput(todo_description_edit, 0)
        animationSet.playTogether(
            ObjectAnimator.ofFloat(todo_newcontainer_linear, "translationY", -250f, 0f)
        )
        animationSet.start()
    }

    private fun hideAddDropDown() {
        todo_add_button.isEnabled = true
        keyboard.hideSoftInputFromWindow(todo_description_edit.windowToken, 0)
        animationSet.playTogether(
            ObjectAnimator.ofFloat(todo_newcontainer_linear, "translationY", 0f, -250f)
        )
        animationSet.start()
    }


//data binding stuff below

    override fun syncView() {
        todo_create_button.isEnabled =
            todoListModel.isValidItemDescription(todo_description_edit.text.toString())
        todo_showdone_switch.isChecked = todoListModel.displayDoneItems()
        todo_clear_button.isEnabled = todoListModel.hasAnyItems()
        todoListAdapter.notifyDataSetChangedAuto()
    }
}
