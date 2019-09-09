package foo.bar.example.fore.fullapp02

import co.early.fore.core.logging.Logger
import dagger.Component
import foo.bar.example.fore.fullapp02.feature.basket.BasketModelFactory
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import foo.bar.example.fore.fullapp02.feature.login.Authentication
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    //expose application scope dependencies we want accessible from anywhere
    val logger: Logger
    val authentication: Authentication
    val fruitCollectorModel: FruitCollectorModel
    val basketModelFactory: BasketModelFactory
    val todoListModel: TodoListModel

    //submodules follow
    //operator fun plus(basketModule: BasketModule): BasketComponent
}
