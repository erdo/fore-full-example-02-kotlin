package foo.bar.example.fore.fullapp02.feature.fruitcollector


import android.os.Build
import co.early.fore.adapters.ChangeAwareArrayList
import co.early.fore.adapters.ChangeAwareList
import co.early.fore.adapters.UpdateSpec
import co.early.fore.adapters.Updateable
import co.early.fore.core.Affirm
import co.early.fore.core.WorkMode
import co.early.fore.core.callbacks.FailureCallbackWithPayload
import co.early.fore.core.callbacks.SuccessCallback
import co.early.fore.core.logging.Logger
import co.early.fore.core.observer.ObservableImp
import co.early.fore.core.time.SystemTimeWrapper
import co.early.fore.retrofit.CallProcessor
import foo.bar.example.fore.fullapp02.api.fruits.FruitPojo
import foo.bar.example.fore.fullapp02.api.fruits.FruitService
import foo.bar.example.fore.fullapp02.message.UserMessage
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * All the unit testable logic and data for our fruit collector,
 * connects to the network via CallProcessor (which is mockable for unit tests)
 *
 * (this class knows nothing about views, contexts, nor anything else to do with the android)
 */
@Singleton
class FruitCollectorModel @Inject constructor(private val fruitService: FruitService,
                                              private val callProcessor: CallProcessor<UserMessage>,
                                              private val systemTimeWrapper: SystemTimeWrapper,
                                              private val workMode: WorkMode,
                                              private val logger: Logger) : ObservableImp(workMode, logger), Updateable{

    private val fruitList: ChangeAwareList<Fruit>

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


    init {
        fruitList = ChangeAwareArrayList(Affirm.notNull(systemTimeWrapper))
    }

    /**
     * The code here looks a bit complicated with the Busy() class etc, that class is just there
     * because in this highly contrived example we have 3 identical network calls all doing the
     * same thing, feel free to ignore this method and skip down to fetchFruits() for something a
     * bit more normal.
     */
    fun fetchFruits1(includeCitrusResults: Boolean, SuccessCallback: SuccessCallback, failureCallbackWithPayload: FailureCallbackWithPayload<UserMessage>) {

        logger.i(TAG, "fetchFruits() 1 includeCitrus:" + includeCitrusResults)

        Affirm.notNull(includeCitrusResults)

        fetchFruits(object : Busy {
            override var isBusy: Boolean
                get() = isBusy1
                set(busy) {
                    isBusy1 = busy
                }
        }, includeCitrusResults, SuccessCallback, failureCallbackWithPayload)

    }

    /**
     * The code here looks a bit complicated with the Busy() class etc, that class is just there
     * because in this highly contrived example we have 3 identical network calls all doing the
     * same thing, feel free to ignore this method and skip down to fetchFruits() for something a
     * bit more normal.
     */
    fun fetchFruits2(includeCitrusResults: Boolean, SuccessCallback: SuccessCallback, failureCallbackWithPayload: FailureCallbackWithPayload<UserMessage>) {

        logger.i(TAG, "fetchFruits() 2 includeCitrus:" + includeCitrusResults)

        Affirm.notNull(includeCitrusResults)

        fetchFruits(object : Busy {
            override var isBusy: Boolean
                get() = isBusy2
                set(busy) {
                    isBusy2 = busy
                }
        }, includeCitrusResults, SuccessCallback, failureCallbackWithPayload)

    }

    /**
     * The code here looks a bit complicated with the Busy() class etc, that class is just there
     * because in this highly contrived example we have 3 identical network calls all doing the
     * same thing, feel free to ignore this method and skip down to fetchFruits() for something a
     * bit more normal.
     */
    fun fetchFruits3(includeCitrusResults: Boolean, SuccessCallback: SuccessCallback, failureCallbackWithPayload: FailureCallbackWithPayload<UserMessage>) {

        logger.i(TAG, "fetchFruits() 3 includeCitrus:" + includeCitrusResults)

        Affirm.notNull(includeCitrusResults)

        fetchFruits(object : Busy {
            override var isBusy: Boolean
                get() = isBusy3
                set(busy) {
                    isBusy3 = busy
                }
        }, includeCitrusResults, SuccessCallback, failureCallbackWithPayload)

    }


    private fun fetchFruits(
            busy: Busy, includeCitrusResults: Boolean,
            SuccessCallback: SuccessCallback,
            failureCallbackWithPayload: FailureCallbackWithPayload<UserMessage>
    ) {

        logger.i(TAG, "fetchFruits()")

        Affirm.notNull(SuccessCallback)
        Affirm.notNull(failureCallbackWithPayload)

        if (busy.isBusy) {
            failureCallbackWithPayload.fail(UserMessage.ERROR_BUSY)
            return
        }

        busy.isBusy = true
        notifyObservers()

        // this is the network call, CallProcessor handles a lot of the complication
        // and lets us mock network calls during tests
        callProcessor.processCall(fruitService.getFruits("3s"), workMode,
                                  { successResponse -> handleSuccess(busy, includeCitrusResults, SuccessCallback, successResponse) }
        ) { failureMessage -> handleFailure(busy, failureCallbackWithPayload, failureMessage) }

    }

    private fun handleSuccess(
            busy: Busy, includeCitrus: Boolean,
            SuccessCallback: SuccessCallback, successResponse: List<FruitPojo>
    ) {

        var fruits: MutableList<Fruit> = ArrayList()

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
        SuccessCallback.success()
        complete(busy)
    }

    private fun countNewFruit(isCitrus: Boolean) {
        if (isCitrus) {
            totalCitrusFruitCount++
        }
        totalFruitCount++
    }

    private fun handleFailure(
            busy: Busy, failureCallbackWithPayload: FailureCallbackWithPayload<UserMessage>,
            failureMessage: UserMessage
    ) {
        failureCallbackWithPayload.fail(failureMessage)
        complete(busy)
    }

    private fun complete(busy: Busy) {

        logger.i(TAG, "complete()")

        busy.isBusy = false
        notifyObservers()
    }

    fun anyNetworkThreadsBusy(): Boolean {
        return isBusy1 || isBusy2 || isBusy3
    }

    fun removeFruit(index: Int) {
        val fruitToRemove = getFruit(index)

        totalFruitCount--
        totalCitrusFruitCount = totalCitrusFruitCount - if (fruitToRemove.isCitrus) 1 else 0

        fruitList.removeAt(index)
        notifyObservers()
    }

    fun clearFruit() {
        logger.i(TAG, "clearFruit()")
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

    companion object {
        private val TAG = FruitCollectorModel::class.java.simpleName
    }
}
