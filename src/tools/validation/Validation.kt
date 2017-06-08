package tools.validation

import java.util.function.Predicate

/**
 * Created by Novarto on 6/6/17.
 */


class Validation<T, R> {

    var rules = mutableListOf<Pair<Predicate<T>, R>>()

    fun rule(predicate: Predicate<T> , result: R, reason: String) : Validation<T,R>{
            rules.add(Pair(predicate, result))
        return this
    }


}