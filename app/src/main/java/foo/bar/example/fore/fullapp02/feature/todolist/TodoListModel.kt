package foo.bar.example.fore.fullapp02.feature.todolist

import co.early.fore.adapters.Adaptable
import co.early.fore.adapters.mutable.ChangeAwareList
import co.early.fore.adapters.mutable.UpdateSpec
import co.early.fore.adapters.mutable.Updateable
import co.early.fore.core.Affirm
import co.early.fore.core.observer.Observable
import co.early.fore.kt.adapters.mutable.ChangeAwareArrayList
import co.early.fore.kt.core.observer.ObservableImp

/**
 * All the unit testable logic and data for our todolist
 *
 * (this class knows nothing about views, contexts, nor anything else to do with the android)
 *
 * - it's driving an adapter so we have functions like get() and size()
 * - we want nice easily animated list changes so we implement Updateable
 * - we also implement Iterable as this make it nice and easy when we come to export the data
 */
class TodoListModel :
    Observable by ObservableImp(),
    Updateable,
    Iterable<TodoListModel.TodoItem>,
    Adaptable<TodoListModel.TodoItem> {

    //list of all items, including the ones that are done
    private val todoList: MutableList<TodoItem> = mutableListOf()

    //only the list of items that are currently visible on the display
    private val displayList: ChangeAwareList<TodoItem> = ChangeAwareArrayList()
    private var displayDoneItems = false
    private var itemsNotYetDone: Int = 0

    fun addItem(done: Boolean, description: String) {
        val todoItem = TodoItem(done, description)
        todoList.add(todoItem)
        if (displayDoneItems || !todoItem.isDone) {
            displayList.add(todoItem)
        }

        itemsNotYetDone += if (todoItem.isDone) 0 else 1

        notifyObservers()
    }

    fun removeItem(index: Int) {
        val removedItem = todoList.removeAt(Affirm.notNull(index))
        displayList.remove(removedItem)

        itemsNotYetDone -= if (removedItem.isDone) 0 else 1

        notifyObservers()
    }

    fun setDone(done: Boolean, index: Int) {
        val item = getItem(index)

        if (item.isDone != done) {

            item.isDone = done

            itemsNotYetDone += if (!done) 1 else -1

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
            //Updatable can only handle animated changes in stages
            //this is the first stage where we animate the displayList.clear() change
            notifyObservers()

            displayList.addAll(
                todoList.filter { item -> displayDoneItems || !item.isDone }
            )
            //Updatable can only handle animated changes in stages
            //this is the second stage where we animate the displayList.addAll
            notifyObservers()
        }
    }

    fun numberOfItemsStillToDo(): Int {
        return itemsNotYetDone
    }

    fun isValidItemDescription(description: String?): Boolean {
        return if (description == null) false else description.length > 0
    }

    override fun getItem(index: Int): TodoItem {
        return displayList[index]
    }

    override fun getItemCount(): Int {
        return displayList.size
    }

    override fun getAndClearLatestUpdateSpec(maxAgeMs: Long): UpdateSpec {
        return displayList.getAndClearLatestUpdateSpec(maxAgeMs)
    }

    override fun iterator(): Iterator<TodoItem> {
        return object : Iterator<TodoItem> {

            private var currentIndex = 0

            override fun hasNext(): Boolean {
                return currentIndex < todoList.size
            }

            override fun next(): TodoItem {
                return todoList[currentIndex++]
            }
        }
    }

    /**
     * All changes to isDone should go via TodoListModel
     */
    class TodoItem(done: Boolean = false, val description: String) {
        var isDone: Boolean internal set

        init {
            isDone = done
        }
    }
}
