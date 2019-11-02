package foo.bar.example.fore.fullapp02.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*


object MoneyFormatter {

    private val HUNDRED = BigDecimal.valueOf(100)
    private val format: NumberFormat = NumberFormat.getCurrencyInstance()

    init {
        format.currency = Currency.getInstance("USD")
    }

    fun format(amount: Int): String {

        val bigDecimal: BigDecimal = if (amount != 0) {
            BigDecimal.valueOf(amount.toLong()).divide(HUNDRED)
        } else {
            BigDecimal.valueOf(amount.toLong())
        }

        return format.format(bigDecimal)
    }
}
