package io.github.konfigur8

/**
 * Determines how a property should be displayed
 */
enum class ExposeMode {
    /**
     * Displays input value. Use for public property values
     */
    Public {
        override fun display(value: kotlin.String) = value
    },

    /**
     * Conceals value with '*' characters. Use for Passwords and other sensitive values
     */
    Private {
        override fun display(value: String) = (0..value.length).map { '*' }.joinToString("*")
    };

    /**
     * converts a value to how it should be exposed publicly

     * @param value the input value
     * *
     * @return the displayable value
     */
    abstract fun display(value: String): String
}
