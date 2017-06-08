package tools.hybris

/**
 *
 *
 * Created by Novarto on 6/5/17.
 */
fun wrap(q: String, prefix: String = "{", postfix: String = "}") = prefix + q + postfix

infix fun String.tAl(alias: String) = this + " as " + alias //table alias
infix fun String.fAl(alias: String) = alias + "." + this //field alias

fun mathTest(listOfNumbers: List<Int>, myFun: (num: Int, num2: Int) -> Int) {
    for (listOfNumber in listOfNumbers) {

    }
}


fun main(args: Array<String>) {
    val sb = StringBuilder()
    val fs = FS(sb)

    println(fs.select().fields("pk", "creationtime").from("Order", Join(sb, "BillingTime" tAl "bt", "billingTime", "pk" fAl "bt"))
            .where("user")
            .equals("customer")
            .and()
            .fields("versionId")
            .isNull()
            .and()
            .fields("store")
            .equals("store")
            .and("code" tAl "bt")
            .like("paynow", LIKE_TYPE.EQUALS)
            .build())
}


class FS(val sb: StringBuilder) {

    fun isNull(): FS {
        sb.append(" IS NULL");return this
    }

    fun select(): FS {
        sb.append("SELECT ");return this
    }

    fun equals(field: String, placeHolder: HYBRIS_PLACEHOLDER = HYBRIS_PLACEHOLDER.ON): FS {
        if(placeHolder == HYBRIS_PLACEHOLDER.ON){
            sb.append(" = ?").append(field)
        } else {
            sb.append("= '").append(field).append("'")
        }
        return this
    }

    fun like(field: String, likeType: LIKE_TYPE): FS {
        sb.append(" LIKE ")
        when (likeType) {
            LIKE_TYPE.ENDS_WITH -> sb.append("'%").append(field).append("'")
            LIKE_TYPE.EQUALS -> sb.append(field)
            LIKE_TYPE.STARTS_WITH -> sb.append(field).append("%")
            LIKE_TYPE.CONTAINS -> sb.append("%").append(field).append("%")
        }
        return this
    }

    //    in is a keyword in kotlin ...
    fun within(vararg values: String): FS {
        sb.append(" IN (?")
        values?.forEach { v -> sb.append(v).append(",") }
        sb.deleteCharAt(sb.length - 1)
        sb.append(")")
        return this
    }

    fun from(tableName: String, vararg joins: Join): FS {
        sb.append(" FROM ").append(wrap(tableName, postfix = ""))
        joins?.forEach { j -> j.on() }
        sb.append("}")
        return this
    }

    fun where(whereClause: String): FS {
        sb.append(" WHERE ").append(wrap(whereClause));return this
    }

    fun and(field: String): FS {
        sb.append(" AND ").append(wrap(field));return this
    }

    fun fields(vararg fields: String): FS {
        fields?.forEach { f -> sb.append(wrap(f)).append(",") }
        sb.deleteCharAt(sb.length - 1)
        return this
    }

    fun and(): FS {
        sb.append(" AND ")
        return this
    }

    fun build() = sb.toString()
}


class Join(val sb: StringBuilder, val targetTable: String, val pk: String, val fk: String) {

    fun on() {
        sb.append(" JOIN ").append(targetTable).append(" ON ")
                .append(wrap(pk))
                .append(" = ")
                .append(wrap(fk))
    }


}

enum class LIKE_TYPE {
    STARTS_WITH,
    ENDS_WITH,
    EQUALS,
    CONTAINS
}

enum class HYBRIS_PLACEHOLDER {
    ON,
    OFF
}







