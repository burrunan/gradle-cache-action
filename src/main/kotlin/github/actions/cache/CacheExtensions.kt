package github.actions.cache

import com.github.burrunan.gradle.cache.RestoreType
import github.actions.core.info
import github.actions.core.warning
import kotlinx.coroutines.await

suspend fun restoreAndLog(
    paths: List<String>, primaryKey: String,
    restoreKeys: List<String> = listOf(),
): RestoreType {
    val result = try {
        when {
            restoreKeys.isEmpty() -> restoreCache(paths.toTypedArray(), primaryKey)
            else -> restoreCache(paths.toTypedArray(), primaryKey, restoreKeys.toTypedArray())
        }.await()
    } catch (t: Throwable) {
        when (t.asDynamic().name) {
            "ValidationError" -> throw t
            else -> {
                warning("Error while loading $primaryKey: ${t.message}")
                return RestoreType.None
            }
        }
    }
    if (result != null) {
        info("Restored from $result")
        return if (result == primaryKey) RestoreType.Exact(result) else RestoreType.Partial(result)
    }
    info("Cache was not found for $primaryKey, restore keys: ${restoreKeys.joinToString(", ")}")
    return RestoreType.None
}

suspend fun saveAndLog(
    paths: List<String>,
    key: String,
) {
    try {
        saveCache(paths.toTypedArray(), key).await()
    } catch (t: Throwable) {
        when (t.asDynamic().name) {
            "ValidationError" -> throw t
            "ReserveCacheError" -> info(t.message ?: "Unknown ReserveCacheError")
            else -> warning("Error while uploading $key: ${t.message}")
        }
    }
}
