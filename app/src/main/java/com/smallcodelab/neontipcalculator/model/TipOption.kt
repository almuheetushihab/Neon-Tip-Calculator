package com.smallcodelab.neontipcalculator.model

/**
 * Predefined tip percentage options.
 *
 * @property percentage Tip value as an integer percentage.
 */
enum class TipOption(val percentage: Int) {
    TEN(10),
    FIFTEEN(15),
    TWENTY(20),
    TWENTY_FIVE(25);

    /**
     * Display label for tip option chip and summary.
     */
    val displayLabel: String
        get() = "$percentage%"
}
