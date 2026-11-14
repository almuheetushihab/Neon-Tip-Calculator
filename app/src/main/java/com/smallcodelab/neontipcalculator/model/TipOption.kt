package com.smallcodelab.neontipcalculator.model

/**
 * Predefined tip percentage options and Custom option.
 *
 * @property percentage Tip value as an integer percentage.
 * @property isCustom Indicates whether this option represents a custom tip slider.
 */
enum class TipOption(val percentage: Int, val isCustom: Boolean = false) {
    TEN(10),
    FIFTEEN(15),
    TWENTY(20),
    TWENTY_FIVE(25),
    CUSTOM(0, isCustom = true);

    /**
     * Display label for tip option chip and summary.
     */
    val displayLabel: String
        get() = if (isCustom) "Custom" else "$percentage%"
}
