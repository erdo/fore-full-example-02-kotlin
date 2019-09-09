package foo.bar.example.fore.fullapp02.feature.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.early.fore.core.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This lets us tell ViewModelProvider how to create our BasketModel
 * (not required if there are no construction parameters, as in this case,
 * but if we had some dependencies like a repository we would need this)
 */
@Singleton
class BasketModelFactory @Inject constructor(private val logger: Logger): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BasketModel::class.java) -> {
                BasketModel(logger) as T
            }
            else -> throw IllegalArgumentException(
                "${modelClass.simpleName} unsupported viewmodel class"
            )
        }
    }

}
