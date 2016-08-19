package examples

import io.github.konfigur8.ConfigurationTemplate
import io.github.konfigur8.ExposeMode
import io.github.konfigur8.Property
import java.lang.Integer.parseInt

data class Title(private val value: String) {
    override fun toString() = value
}


data class Duration(private val value: Int) {
    override fun toString() = value.toString()
    companion object {
        fun parse(s: String) = Duration(parseInt(s.removeSuffix("s")))
    }
    fun describe() = value.toString() + "s"
}

object CreatingAConfiguration {

    @JvmStatic fun main(args: Array<String>) {
        val USER = Property.string("USER")
        val AGE = Property.int("AGE")
        val TITLE = Property("TITLE", { Title(it) })
        val PASSWORD = Property.string("PASSWORD", ExposeMode.Private)
        val RUNTIME = Property.string("java.runtime.version")
        val PATIENCE_LEVEL = Property("DURATION", { i: String -> Duration.parse(i) }, { it.describe() })
        val UNKNOWN = Property.string("UNKNOWN")

        val configTemplate = ConfigurationTemplate().requiring(USER) // will be supplied by the environment
                .requiring(RUNTIME) // will be supplied by the VM
                .withProp(AGE, 2) // falls back to a default value
                .withProp(PASSWORD, "my_secret_value") // falls back to a default value
                .requiring(TITLE) // no value - requires overriding
                .withProp(PATIENCE_LEVEL, Duration(10)) // custom type property with default

        println("Attempt to build an incomplete config: " + tryIt({ configTemplate.reify() }))

        val config = configTemplate.withProp(TITLE, Title("Dr")).reify()

        println("Attempt to get 'UNKNOWN' property: " + tryIt({ config.valueOf(UNKNOWN) }))
        println("The 'TITLE' supplied by the user is: " + config.valueOf(TITLE))
        println("The 'USER' supplied by the environment is: " + config.valueOf(USER))
        println("The 'RUNTIME' supplied by the System is: " + config.valueOf(USER))
        println("The 'AGE' fell back to the default value of: " + config.valueOf(AGE))
        println("The 'PASSWORD' fell back to the default value of: " + config.valueOf(PASSWORD))
        println("Type-safe retrieval of 'PATIENCE_LEVEL': " + config.valueOf(PATIENCE_LEVEL))
        println("Publicly visible settings hides the private values: " + config.settings())
    }

    private fun tryIt(supplier: () -> Any): Any {
        try {
            return supplier.invoke()
        } catch (t: Throwable) {
            return t
        }

    }
}
