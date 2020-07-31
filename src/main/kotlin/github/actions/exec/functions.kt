package github.actions.exec

import com.github.burrunan.gradle.jsObject
import kotlinx.coroutines.await

suspend fun exec(
    commandLine: String, vararg args: String,
    options: ExecOptions.() -> Unit = {}
): ExecResult {
    val stdout = mutableListOf<String>()
    val exitCode = exec(
        commandLine,
        args.copyOf(),
        jsObject(options).apply {
            listeners = jsObject {
                this.stdout = {
                    stdout.add(it.toString())
                }
            }
        }
    ).await()
    return ExecResult(
        exitCode = exitCode.toInt(),
        stdout = stdout.joinToString("\n")
    )
}
