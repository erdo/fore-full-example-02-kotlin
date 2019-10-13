package foo.bar.example.fore.fullapp02.feature.todolist

import co.early.fore.core.WorkMode
import co.early.fore.core.logging.Logger
import co.early.fore.core.observer.ObservableImp
import co.early.fore.core.time.SystemTimeWrapper
import foo.bar.example.fore.fullapp02.feature.csv.CSVBuilder
import foo.bar.example.fore.fullapp02.feature.csv.ReaderWriter
import foo.bar.example.fore.fullapp02.message.UserMessage
import foo.bar.example.fore.fullapp02.utils.FailureWithPayload
import foo.bar.example.fore.fullapp02.utils.Success

/**
 *
 * (this class knows nothing about views, contexts, nor anything else to do with the android)
 */
class TodoListExporter(
    private val todoListModel: TodoListModel,
    private val readerWriter: ReaderWriter,
    private val systemTimeWrapper: SystemTimeWrapper,
    private val workMode: WorkMode,
    private val logger: Logger
) : ObservableImp(workMode, logger) {

    var isBusy = false
        private set


    fun exportCurrentTodoList(
        success: Success,
        failureWithPayload: FailureWithPayload<UserMessage>
    ) {

        if (isBusy) {
            failureWithPayload(UserMessage.ERROR_BUSY)
            return
        }

        isBusy = true
        notifyObservers()

        logger.i(LOG_TAG, "starting export...")
        logger.i(LOG_TAG, "> building CSV data")

        val csvDataBuilder = CSVBuilder()

        for (todoItem: TodoItem in todoListModel) {
            csvDataBuilder.addLine(
                todoItem.description, if (todoItem.isDone) "done" else "not done"
            )
        }

        logger.i(LOG_TAG, "> saving file\n");

        readerWriter.writeToFileAsync(
            "CSV_EXP_" + systemTimeWrapper.currentTimeMillis() + ".csv",
            csvDataBuilder.toString(),
            {
                complete()
                logger.i(LOG_TAG, "completed success")
                success()
            },
            {
                complete()
                logger.i(LOG_TAG, "completed fail")
                failureWithPayload(UserMessage.ERROR_MISC)
            }
        )
    }

    private fun complete() {
        isBusy = false
        notifyObservers()
    }

    companion object {
        var LOG_TAG = TodoListExporter::class.java.simpleName
    }

}
