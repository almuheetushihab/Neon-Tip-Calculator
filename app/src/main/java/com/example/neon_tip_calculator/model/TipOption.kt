package com.example.neon_tip_calculator.model

enum class TipOption(val percentage: Int) {
    TEN(10),
    FIFTEEN(15),
    TWENTY(20),
    TWENTY_FIVE(25);

    val displayLabel: String
        get() = "$percentage%"
}
