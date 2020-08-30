package foo.bar.example.fore.fullapp02.feature.fruitcollector


import android.os.Build
import arrow.core.Either
import co.early.fore.kt.adapters.ChangeAwareArrayList
import co.early.fore.adapters.ChangeAwareList
import co.early.fore.adapters.UpdateSpec
import co.early.fore.adapters.Updateable
import co.early.fore.core.Affirm
import co.early.fore.kt.core.logging.Logger
import co.early.fore.core.observer.Observable
import co.early.fore.kt.core.callbacks.FailureWithPayload
import co.early.fore.kt.core.callbacks.Success
import co.early.fore.kt.core.coroutine.launchMain
import co.early.fore.kt.core.observer.ObservableImp
import co.early.fore.kt.retrofit.CallProcessor
import foo.bar.example.fore.fullapp02.api.fruits.FruitPojo
import foo.bar.example.fore.fullapp02.api.fruits.FruitService
import foo.bar.example.fore.fullapp02.message.UserMessage
import java.util.*

/**
 * All the unit testable logic and data for our fruit collector,
 * connects to the network via CallProcessor (which is mockable for unit tests)
 *
 * (this class knows nothing about views, contexts, nor anything else to do with the android)
 */
class FruitCollectorModel(
    private val fruitService: FruitService,
    private val callProcessor: CallProcessor<UserMessage>,
    private val logger: Logger
) : Observable by ObservableImp(), Updateable {

    private val fruitList: ChangeAwareList<Fruit> = ChangeAwareArrayList()

    var totalFruitCount = 0
        private set
    var totalCitrusFruitCount = 0
        private set

    var isBusy1 = false
        private set
    var isBusy2 = false
        private set
    var isBusy3 = false
        private set

    val fruitListSize: Int
        get() = fruitList.size


    /**
     * The code here looks a bit complicated with the Busy() class etc, that class is just there
     * because in this highly contrived example we have 3 identical network calls all doing the
     * same thing, feel free to ignore this method and skip down to fetchFruits() for something a
     * bit more normal.
     */
    fun fetchFruits1(
        includeCitrusResults: Boolean,
        success: Success,
        failureWithPayload: FailureWithPayload<UserMessage>
    ) {

        logger.i("fetchFruits() 1 includeCitrus:" + includeCitrusResults)

        Affirm.notNull(includeCitrusResults)

        fetchFruits(object : Busy {
            override var isBusy: Boolean
                get() = isBusy1
                set(busy) {
                    isBusy1 = busy
                }
        }, includeCitrusResults, success, failureWithPayload)

    }

    /**
     * The code here looks a bit complicated with the Busy() class etc, that class is just there
     * because in this highly contrived example we have 3 identical network calls all doing the
     * same thing, feel free to ignore this method and skip down to fetchFruits() for something a
     * bit more normal.
     */
    fun fetchFruits2(
        includeCitrusResults: Boolean,
        success: Success,
        failureWithPayload: FailureWithPayload<UserMessage>
    ) {

        logger.i("fetchFruits() 2 includeCitrus:" + includeCitrusResults)

        fetchFruits(object : Busy {
            override var isBusy: Boolean
                get() = isBusy2
                set(busy) {
                    isBusy2 = busy
                }
        }, includeCitrusResults, success, failureWithPayload)

    }

    /**
     * The code here looks a bit complicated with the Busy() class etc, that class is just there
     * because in this highly contrived example we have 3 identical network calls all doing the
     * same thing, feel free to ignore this method and skip down to fetchFruits() for something a
     * bit more normal.
     */
    fun fetchFruits3(
        includeCitrusResults: Boolean,
        success: Success,
        failureWithPayload: FailureWithPayload<UserMessage>
    ) {

        logger.i("fetchFruits() 3 includeCitrus:" + includeCitrusResults)

        Affirm.notNull(includeCitrusResults)

        fetchFruits(object : Busy {
            override var isBusy: Boolean
                get() = isBusy3
                set(busy) {
                    isBusy3 = busy
                }
        }, includeCitrusResults, success, failureWithPayload)

    }


    private fun fetchFruits(
        busy: Busy, includeCitrusResults: Boolean,
        success: Success,
        failureWithPayload: FailureWithPayload<UserMessage>
    ) {

        logger.i("fetchFruits()")

        if (busy.isBusy) {
            failureWithPayload(UserMessage.ERROR_BUSY)
            return
        }

        busy.isBusy = true
        notifyObservers()

        launchMain {

            // this is the network call, CallProcessor handles a lot of the complication
            // and lets us mock network calls during tests
            val result = callProcessor.processCallAwait {
                fruitService.getFruits("3s")
            }

            when (result) {
                is Either.Left -> handleFailure(busy, failureWithPayload, result.a)
                is Either.Right -> handleSuccess(
                    busy,
                    includeCitrusResults,
                    success,
                    result.b
                )
            }
        }

    }

    private fun handleSuccess(
        busy: Busy, includeCitrus: Boolean,
        success: Success, successResponse: List<FruitPojo>
    ) {

        val fruits: MutableList<Fruit> = ArrayList()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

            //old version to filter out citrus fruits
            for (fruitPojo in successResponse) {
                if (includeCitrus || !fruitPojo.isCitrus) {
                    fruits.add(Fruit(fruitPojo))
                    countNewFruit(fruitPojo.isCitrus)
                }
            }

        } else {

            //kotlin version to filter out citrus fruits
            fruits.addAll(successResponse
                .filter { fruitPojo -> includeCitrus || !fruitPojo.isCitrus }
                .map { fruitPojo ->
                    countNewFruit(fruitPojo.isCitrus)
                    Fruit(fruitPojo)
                })
        }

        fruitList.addAll(0, fruits)
        success()
        complete(busy)
    }

    private fun countNewFruit(isCitrus: Boolean) {
        if (isCitrus) {
            totalCitrusFruitCount++
        }
        totalFruitCount++
    }

    private fun handleFailure(
        busy: Busy, failureWithPayload: FailureWithPayload<UserMessage>,
        failureMessage: UserMessage
    ) {
        failureWithPayload(failureMessage)
        complete(busy)
    }

    private fun complete(busy: Busy) {

        logger.i("complete()")

        busy.isBusy = false
        notifyObservers()
    }

    fun anyNetworkThreadsBusy(): Boolean {
        return isBusy1 || isBusy2 || isBusy3
    }

    fun removeFruit(index: Int) {
        val fruitToRemove = getFruit(index)

        totalFruitCount--
        totalCitrusFruitCount -= if (fruitToRemove.isCitrus) 1 else 0

        fruitList.removeAt(index)
        notifyObservers()
    }

    fun clearFruit() {
        logger.i("clearFruit()")
        fruitList.clear()
        totalCitrusFruitCount = 0
        totalFruitCount = 0
        notifyObservers()
    }

    fun getFruit(index: Int): Fruit {
        checkIndex(index)
        return fruitList[index]
    }

    private fun checkIndex(index: Int) {
        if (fruitList.size == 0) {
            throw IndexOutOfBoundsException("fruitlist has no items in it, can not get index:" + index)
        } else if (index < 0 || index > fruitList.size - 1) {
            throw IndexOutOfBoundsException("fruitlist index needs to be between 0 and " + (fruitList.size - 1) + " not:" + index)
        }
    }

    override fun getAndClearLatestUpdateSpec(maxAgeMs: Long): UpdateSpec {
        return fruitList.getAndClearLatestUpdateSpec(maxAgeMs)
    }

    private interface Busy {
        var isBusy: Boolean
    }
}
