package foo.bar.example.fore.fullapp02.ui.common.widget


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import foo.bar.example.fore.fullapp02.R
import java.util.*


open class BottomNavigationViewWithBadges @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BottomNavigationView(context!!, attrs, defStyleAttr) {

    private val badges = ArrayList<BadgeView>()


    override fun onFinishInflate() {
        super.onFinishInflate()

        val menuView = getChildAt(0) as BottomNavigationMenuView

        val numberOfTabs = menuView.childCount

        for (ii in 0 until numberOfTabs) {

            badges.add(
                LayoutInflater.from(context)
                    .inflate(R.layout.common_navigation_badge, menuView, false) as BadgeView
            )

            (menuView.getChildAt(ii) as BottomNavigationItemView).addView(badges[ii])
        }
    }


    fun setBadge(index: Int, number: Int) {

        badges[index].visibility = if (number < 1) View.INVISIBLE else View.VISIBLE

        if (number > 99) {
            badges[index].setText("99+")
        } else {
            badges[index].setText("" + number)
        }
    }
}

