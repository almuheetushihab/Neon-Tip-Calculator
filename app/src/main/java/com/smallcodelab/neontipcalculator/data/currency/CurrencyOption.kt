package com.smallcodelab.neontipcalculator.data.currency

/**
 * Supported currency options for tip calculations.
 *
 * @property symbol Currency character symbol.
 * @property code ISO 4217 currency code.
 */
enum class CurrencyOption(val symbol: String, val code: String) {
    USD("$", "USD"),
    EUR("€", "EUR"),
    GBP("£", "GBP"),
    BDT("৳", "BDT");

    /**
     * Formats a raw amount string with currency symbol prefix.
     */
    fun formatAmount(amount: String): String = "$symbol$amount"
}
