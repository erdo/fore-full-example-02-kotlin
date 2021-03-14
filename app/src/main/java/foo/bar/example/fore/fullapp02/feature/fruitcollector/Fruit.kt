package foo.bar.example.fore.fullapp02.feature.fruitcollector

import foo.bar.example.fore.fullapp02.api.fruits.FruitPojo

class Fruit(fruitPojo: FruitPojo) {
    val name: String = fruitPojo.name
    val isCitrus: Boolean = fruitPojo.isCitrus
    val tastyPercentScore: Int = fruitPojo.tastyPercentScore
    val firstLetterUpperCase: Char = fruitPojo.name.substring(0, 1).toUpperCase()[0]
}
