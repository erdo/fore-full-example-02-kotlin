package foo.bar.example.fore.fullapp02.feature.todolist

import co.early.fore.adapters.ChangeAwareArrayList
import co.early.fore.adapters.ChangeAwareList
import co.early.fore.adapters.UpdateSpec
import co.early.fore.adapters.Updateable
import co.early.fore.core.Affirm
import co.early.fore.core.WorkMode
import co.early.fore.core.logging.Logger
import co.early.fore.core.observer.ObservableImp
import co.early.fore.core.time.SystemTimeWrapper
import java.util.*

/**
 * All the unit testable logic and data for our todolist
 *
 * (this class knows nothing about views, contexts, nor anything else to do with the android)
 */
class TodoListModel (systemTimeWrapper: SystemTimeWrapper, logger: Logger) : ObservableImp(WorkMode.SYNCHRONOUS, logger),
                                                                            Updateable {

    private val todoList: MutableList<TodoItem>
    private val displayList: ChangeAwareList<TodoItem>
    private var displayDoneItems = false
    private var itemsNotYetDone: Int = 0

    val size: Int
        get() = displayList.size

    init {
        this.todoList = ArrayList()
        this.displayList = ChangeAwareArrayList(Affirm.notNull(systemTimeWrapper))
    }

    fun addItem(todoItem: TodoItem) {
        todoList.add(Affirm.notNull(todoItem))
        if (displayDoneItems || !todoItem.isDone) {
            displayList.add(todoItem)
        }

        itemsNotYetDone = itemsNotYetDone + if (todoItem.isDone) 0 else 1

        notifyObservers()
    }

    fun removeItem(index: Int) {
        val removedItem = todoList.removeAt(Affirm.notNull(index))
        displayList.remove(removedItem)

        itemsNotYetDone = itemsNotYetDone - if (removedItem.isDone) 0 else 1

        notifyObservers()
    }

    fun getItem(index: Int): TodoItem {
        return displayList[index]
    }

    fun setDone(done: Boolean, index: Int) {
        val item = getItem(index)

        if (item.isDone != done) {

            item.isDone = done

            itemsNotYetDone = itemsNotYetDone + if (!done) 1 else -1

            if (displayDoneItems) {
                displayList.makeAwareOfDataChange(index)
            } else {
                displayList.removeAt(index)
            }

            notifyObservers()
        }
    }

    fun hasAnyItems(): Boolean {
        return todoList.size > 0
    }

    fun clear() {
        todoList.clear()
        displayList.clear()
        itemsNotYetDone = 0
        notifyObservers()
    }

    fun displayDoneItems(): Boolean {
        return displayDoneItems
    }

    fun setDisplayDoneItems(displayDoneItems: Boolean) {

        if (displayDoneItems != this.displayDoneItems) {

            this.displayDoneItems = displayDoneItems

            displayList.clear()

            for (todoItem in todoList) {
                if (displayDoneItems || !todoItem.isDone) {
                    displayList.add(todoItem)
                }
            }

            //this clears the updatespec so that the next one will be a full update
            displayList.getAndClearLatestUpdateSpec(50)

            notifyObservers()
        }
    }

    fun numberOfItemsStillToDo(): Int {
        return itemsNotYetDone
    }

    fun isValidItemDescription(description: String?): Boolean {
        return if (description == null) false else description.length > 0
    }

    override fun getAndClearLatestUpdateSpec(maxAgeMs: Long): UpdateSpec {
        return displayList.getAndClearLatestUpdateSpec(maxAgeMs)
    }
}
