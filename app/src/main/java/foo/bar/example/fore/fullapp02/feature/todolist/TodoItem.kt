package foo.bar.example.fore.fullapp02.feature.todolist

import co.early.fore.core.Affirm

/**
 *
 */
class TodoItem(done: Boolean, description: String) {

    var isDone: Boolean = false
        // kotlin doesn't have package access so I have to make this internal :/
        // I don't want it to have this much access as I only want this to
        // be accessible by TodoListModel so that we have a
        // chance to correctly notify our observers when it is called
        // as it is we have nothing to prevent this being called directly
        internal set
    val description: String

    init {
        this.isDone = Affirm.notNull(done)
        this.description = Affirm.notNull(description)
    }
}
