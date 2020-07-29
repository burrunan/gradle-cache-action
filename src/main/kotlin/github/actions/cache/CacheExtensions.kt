package github.actions.cache

import com.github.burrunan.gradle.cache.RestoreType
import github.actions.core.info
import github.actions.core.warning
import kotlinx.coroutines.await

suspend fun restoreAndLog(
    paths: Array<String>, primaryKey: String,
    restoreKeys: Array<String> = arrayOf(),
): RestoreType {
    val result = try {
        when {
            restoreKeys.isEmpty() -> restoreCache(paths, primaryKey)
            else -> restoreCache(paths, primaryKey, restoreKeys)
        }.await()
    } catch (t: Throwable) {
        when (t.asDynamic().name) {
            "ValidationError" -> throw t
            else -> warning("Error while loading $primaryKey: ${t.message}")
        }
    }
    if (result != null) {
        info("Restored from $result")
        return if (result == primaryKey) RestoreType.EXACT else RestoreType.PARTIAL
    }
    info("Cache was not found for $primaryKey, restore keys: ${restoreKeys.joinToString(", ")}")
    return RestoreType.NONE
}

suspend fun saveAndLog(
    paths: Array<String>,
    key: String,
) {
    try {
        saveCache(paths, key).await()
    } catch (t: Throwable) {
        when (t.asDynamic().name) {
            "ValidationError" -> throw t
            "ReserveCacheError" -> info(t.message ?: "Unknown ReserveCacheError")
            else -> warning("Error while uploading $key: ${t.message}")
        }
    }
}
