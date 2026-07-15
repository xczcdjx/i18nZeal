package com.djx.i18n.runtime

/**
 * Formats positional placeholders (`{0}`) and named placeholders (`{name}`).
 */
fun formatI18nText(
    text: String,
    vararg args: Any?,
): String {
    var result = text
    val namedArguments = mutableListOf<Pair<String, Any?>>()
    val plainArguments = mutableListOf<Any?>()

    args.forEach { argument ->
        when (argument) {
            is Pair<*, *> -> {
                val name = argument.first as? String
                if (name != null) {
                    namedArguments += name to argument.second
                } else {
                    plainArguments += argument
                }
            }

            is Map<*, *> -> argument.forEach namedArgument@{ (rawName, value) ->
                val name = rawName as? String ?: return@namedArgument
                namedArguments += name to value
            }

            else -> plainArguments += argument
        }
    }

    namedArguments.forEach { (name, value) ->
        result = result.replace(
            oldValue = "{$name}",
            newValue = value?.toString().orEmpty(),
        )
    }

    if (indexedPlaceholderRegex.containsMatchIn(text)) {
        plainArguments.forEachIndexed { index, value ->
            result = result.replacePositional(index, value)
        }
    } else {
        plainArguments.forEach { value ->
            val placeholder = namedPlaceholderRegex.find(result)?.value ?: return@forEach
            result = result.replace(
                oldValue = placeholder,
                newValue = value?.toString().orEmpty(),
            )
        }
    }

    return result
}

private val indexedPlaceholderRegex = Regex("\\{\\d+\\}")
private val namedPlaceholderRegex = Regex("\\{[A-Za-z_][A-Za-z0-9_.-]*\\}")

private fun String.replacePositional(index: Int, value: Any?): String {
    return replace(
        oldValue = "{$index}",
        newValue = value?.toString().orEmpty(),
    )
}
