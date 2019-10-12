package foo.bar.example.fore.fullapp02.feature.basket

import androidx.lifecycle.ViewModel
import co.early.fore.core.WorkMode
import co.early.fore.core.logging.Logger
import co.early.fore.core.observer.Observable
import co.early.fore.core.observer.ObservableImp
import java.util.*


/**
 * All the unit testable logic and data for a basket.
 *
 * This time we extend android's ViewModel
 *
 * We added a Logger parameter to show how
 * ViewModelProvider.Factory works
 *
 * Apart from that change, the model is written in the same as all the others.
 *
 * Note, we are using one fore Observable for the databinding here, rather than
 * a number of livedata items (our code will end up much smaller, and
 * more robust because of this). See here for more info
 * (https://dev.to/erdo/tutorial-spot-the-deliberate-bug-165k)
 */
class BasketModel(logger: Logger, workMode: WorkMode) : ViewModel(),
    Observable by ObservableImp(workMode) {


    private val items = ArrayList<BasketItem>()


    var isDiscountEnabled: Boolean = false
        set(discountEnabled) {
            field = discountEnabled
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

    fun calculateCosts() {

        totalCost = 0
        totalDiscount = 0

        for (basketItem in items) {
            totalCost += basketItem.cost
        }

        if (isDiscountEnabled) {
            totalDiscount = if (isDiscountEnabled) totalCost * discountPercent / 100 else totalCost
            totalCost = totalCost - totalDiscount
        }
    }
}
