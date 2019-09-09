package foo.bar.example.fore.fullapp02.feature.fruitcollector


import co.early.fore.core.Affirm
import foo.bar.example.fore.fullapp02.api.fruits.FruitPojo

class Fruit(fruitPojo: FruitPojo) {

    val name: String
    val isCitrus: Boolean
    val tastyPercentScore: Int
    val firstLetterUpperCase: Char

    init {
        Affirm.notNull(fruitPojo)

        this.name = fruitPojo.name
        this.isCitrus = fruitPojo.isCitrus
        this.tastyPercentScore = fruitPojo.tastyPercentScore
        this.firstLetterUpperCase = fruitPojo.name.substring(0, 1).toUpperCase()[0]
    }
}
