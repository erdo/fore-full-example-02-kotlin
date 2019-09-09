package foo.bar.example.fore.fullapp02.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*


object MoneyFormatter {

    private var format: NumberFormat? = null
    private val HUNDRED = BigDecimal.valueOf(100)

    init {
        format = NumberFormat.getCurrencyInstance()
        format!!.currency = Currency.getInstance("USD")
    }

    fun format(amount: Int): String {

        val bigDecimal: BigDecimal
        if (amount != 0) {
            bigDecimal = BigDecimal.valueOf(amount.toLong()).divide(HUNDRED)
        } else {
            bigDecimal = BigDecimal.valueOf(amount.toLong())
        }

        return format!!.format(bigDecimal)
    }
}
