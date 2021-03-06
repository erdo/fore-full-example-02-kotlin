package foo.bar.example.fore.fullapp02.feature.basket

import androidx.lifecycle.ViewModel
import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.observer.ObservableImp
import java.util.*


/**
 * All the unit testable logic and data for a basket.
 *
 * This time we extend android's ViewModel
 *
 * We added a Logger parameter to show how
 * ViewModelProvider.Factory works, but as we're using
 * koin for DI we don't even need to worry about the factory
 *
 * Apart from that change, the model is written in the same as all the others.
 *
 * Note, we are using one fore Observable for the databinding here, rather than
 * a number of livedata items (our code will end up much smaller, and
 * more robust because of this). See here for more info
 * (https://dev.to/erdo/tutorial-spot-the-deliberate-bug-165k)
 */
class BasketModel : ViewModel(), Observable by ObservableImp() {

    private val items = ArrayList<BasketItem>()

    var isDiscount: Boolean = false
        set(discountOn) {
            field = discountOn
            calculateCosts()
            notifyObservers()
        }
    val discountPercent = 10

    var totalCost = 0
        private set
    var totalDiscount = 0
        private set

    val numberOfItems: Int
        get() = items.size

    fun addItem() {
        items.add(BasketItem())
        calculateCosts()
        notifyObservers()
    }

    fun removeItem() {
        if (canRemoveItems()) {
            items.removeAt(0)
            calculateCosts()
            notifyObservers()
        }
    }

    fun canRemoveItems(): Boolean {
        return numberOfItems > 0
    }

    private fun calculateCosts() {

        totalCost = 0
        totalDiscount = 0

        for (basketItem in items) {
            totalCost += basketItem.cost
        }

        if (isDiscount) {
            totalDiscount = if (isDiscount) totalCost * discountPercent / 100 else totalCost
            totalCost -= totalDiscount
        }
    }
}
