package actions.core.ext

import kotlinx.js.jso

fun getInput(name: String, required: Boolean = false): String =
    actions.core.getInput(name, jso { this.required = required })

private val LINE_SEPARATOR = Regex("[\r\n]+")

fun getListInput(name: String, required: Boolean = false): List<String> =
    actions.core.getInput(name, jso { this.required = required })
        .split(LINE_SEPARATOR)
        .map { it.trim() }
        .filterNot { it.isEmpty() }
