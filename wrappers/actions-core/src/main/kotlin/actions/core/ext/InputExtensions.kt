package actions.core.ext

import kotlinext.js.jsObject

fun getInput(name: String, required: Boolean = false): String =
    actions.core.getInput(name, jsObject { this.required = required })
