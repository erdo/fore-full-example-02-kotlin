package foo.bar.example.fore.fullapp02.ui.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import foo.bar.example.fore.fullapp02.ui.todolist.TodoListFragment
import kotlinx.android.synthetic.main.common_navigation_badge.view.*


/**
 * For these examples we are managing fore observers at the **Fragment**
 * level, [TodoListFragment]
 */
class BadgeView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {

    fun setText(charSequence: CharSequence) {
        navigation_badge_text.text = charSequence
    }
}
