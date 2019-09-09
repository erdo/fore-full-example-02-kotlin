package foo.bar.example.fore.fullapp02.api.fruits

/**
 *
 *
 * <Code>
 *
 * The server returns us a list of fruit that look like this:
 *
 * {
 * "name":"papaya",
 * "isCitrus":false,
 * "tastyPercentScore":98
 * }
 *
 * </Code>
 *
 *
 *
 */
class FruitPojo(var name: String, var isCitrus: Boolean, var tastyPercentScore: Int)
