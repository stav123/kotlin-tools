package tools.validation

import java.util.function.Predicate

/**
 * Created by Novarto on 6/6/17.
 */


fun main(args: Array<String>) {


    println(Validation<String, String>()
            .rule(Predicate { p -> p.length > 5 }, "Length Should be < than 5", "test")
            .on("test", "zxc"))


}


class Validation<T, R> {

    private val rules = mutableListOf<Pair<Predicate<T>, R>>()

    fun rule(p: Predicate<T>, result: R, reason: String): Validation<T, R> {
        rules.add(Pair(p, result))
        return this
    }

    fun on(t: T, default: R): R {
        return rules.filter { it.first.test(t) }.map { pair -> pair.second }.getOrElse(0, { default })
    }


}

object Helpers {

    fun <T> accept(t: T, c: (T) -> (Unit)) : Unit = if (t != null) c(t) else Unit
    fun <T, R> change(t: T, m: (T) -> (R)) = if (t != null) m(t) else Unit
    fun <T> either(a: T, b: T) = a ?: b
    fun <T> each(c: Collection<T>, t: (T) -> (Unit)) : Unit = accept(c, {c -> c.forEach(t)})


}

data class Vehicle(var a: String, var b: String)
