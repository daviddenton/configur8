package io.github.konfigur8

/**
 * Determines how a property should be displayed
 */
interface ExposeMode {
    companion object {
        /**
         * Displays input value. Use for public property values
         */
        val Public = object : ExposeMode {
            override fun display(value: String) = value
        }

        /**
         * Conceals value with '*' characters. Use for Passwords and other sensitive values
         */
        val Private = object : ExposeMode {
            override fun display(value: String) = (0..value.length).map { '*' }.joinToString("*")
        }
    }

    /**
     * converts a value to how it should be exposed publicly

     * @param value the input value
     * *
     * @return the displayable value
     */
    fun display(value: String): String
}
