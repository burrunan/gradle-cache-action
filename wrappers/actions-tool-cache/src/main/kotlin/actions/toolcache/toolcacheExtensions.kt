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

package actions.toolcache

import kotlinx.coroutines.await

suspend fun downloadTool(url: String) = downloadToolAsync(url).await()
suspend fun downloadTool(url: String, dest: String) = downloadToolAsync(url, dest).await()
suspend fun downloadTool(url: String, dest: String, auth: String) = downloadToolAsync(url, dest, auth).await()

suspend fun extract7z(file: String) =
    extract7zAsync(file).await()
suspend fun extract7z(file: String, dest: String) =
    extract7zAsync(file, dest).await()
suspend fun extract7z(file: String, dest: String, _7zPath: String) =
    extract7zAsync(file, dest, _7zPath).await()

suspend fun extractTar(file: String) =
    extractTarAsync(file).await()
suspend fun extractTar(file: String, dest: String) =
    extractTarAsync(file, dest).await()
suspend fun extractTar(file: String, dest: String, flags: String) =
    extractTarAsync(file, dest, flags).await()
suspend fun extractTar(file: String, dest: String, flags: Array<String>) =
    extractTarAsync(file, dest, flags).await()

suspend fun extractXar(file: String) =
    extractXarAsync(file).await()
suspend fun extractXar(file: String, dest: String) =
    extractXarAsync(file, dest).await()
suspend fun extractXar(file: String, dest: String, flags: String) =
    extractXarAsync(file, dest, flags).await()

suspend fun extractXar(file: String, dest: String, flags: Array<String>) =
    extractXarAsync(file, dest, flags).await()

suspend fun extractZip(file: String) =
    extractZipAsync(file).await()

suspend fun extractZip(file: String, dest: String) =
    extractZipAsync(file, dest).await()

suspend fun cacheDir(sourceDir: String, tool: String, version: String) =
    cacheDirAsync(sourceDir, tool, version).await()

suspend fun cacheDir(sourceDir: String, tool: String, version: String, arch: String) =
    cacheDirAsync(sourceDir, tool, version, arch).await()

suspend fun cacheFile(sourceFile: String, targetFile: String, tool: String, version: String) =
    cacheFileAsync(sourceFile, targetFile, tool, version).await()
suspend fun cacheFile(sourceFile: String, targetFile: String, tool: String, version: String, arch: String) =
    cacheFileAsync(sourceFile, targetFile, tool, version, arch).await()

suspend fun getManifestFromRepo(owner: String, repo: String) =
    getManifestFromRepoAsync(owner, repo).await()
suspend fun getManifestFromRepo(owner: String, repo: String, auth: String) =
    getManifestFromRepoAsync(owner, repo, auth).await()
suspend fun getManifestFromRepo(owner: String, repo: String, auth: String, branch: String) =
    getManifestFromRepoAsync(owner, repo, auth, branch).await()

suspend fun findFromManifest(versionSpec: String, stable: Boolean, manifest: Array<IToolRelease>) =
    findFromManifestAsync(versionSpec, stable, manifest).await()
suspend fun findFromManifest(versionSpec: String, stable: Boolean, manifest: Array<IToolRelease>, archFilter: String) =
    findFromManifestAsync(versionSpec, stable, manifest, archFilter).await()
