package actions.core.ext

import kotlinext.js.jsObject

fun getInput(name: String, required: Boolean = false): String =
    actions.core.getInput(name, jsObject { this.required = required })

private val LINE_SEPARATOR = Regex("[\r\n]+")

fun getListInput(name: String, required: Boolean = false): List<String> =
    actions.core.getInput(name, jsObject { this.required = required })
        .split(LINE_SEPARATOR)
        .map { it.trim() }
        .filterNot { it.isEmpty() }
