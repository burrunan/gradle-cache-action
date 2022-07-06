/*
 * Copyright 2020 Vladimir Sitnikov <sitnikov.vladimir@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fs2.promises

import kotlinx.coroutines.await

suspend fun unlink(paths: String) =
    unlinkAsync(paths).await()

suspend fun readFile(path: String, encoding: String = "utf-8") =
    readFileAsync(path, encoding).await()

suspend fun writeFile(path: String, data: Any, encoding: String = "utf-8") =
    writeFileAsync(path, data, encoding).await()

suspend fun copyFile(src: String, dst: String) =
    copyFileAsync(src, dst).await()

suspend fun copyFile(src: String, dst: String, flags: Number) =
    copyFileAsync(src, dst, flags).await()

suspend fun stat(path: String) =
    statAsync(path).await()

suspend fun rename(oldPath: String, newPath: String) =
    renameAsync(oldPath, newPath).await()

suspend fun mkdir(path: String) =
    mkdirAsync(path).await()

suspend fun chmod(path: String, mode: Number) =
    chmodAsync(path, mode).await()

suspend fun chmod(path: String, mode: String) =
    chmodAsync(path, mode).await()
