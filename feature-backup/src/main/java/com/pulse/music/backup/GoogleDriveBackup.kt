package com.pulse.music.backup

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveBackup @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val driveScopes = listOf(DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE)

    fun getSignInClient(): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        return GoogleSignIn.getClient(context, signInOptions)
    }

    fun getDriveService(accountName: String): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(context, driveScopes)
        credential.selectedAccountName = accountName
        return Drive.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
            .setApplicationName("Pulse Music")
            .build()
    }

    suspend fun uploadBackup(driveService: Drive, filePath: String, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val fileMetadata = File().apply {
                    name = fileName
                    mimeType = "application/zip"
                    parents = listOf("appDataFolder")
                }
                val javaFile = java.io.File(filePath)
                val mediaContent = com.google.api.client.http.FileContent("application/zip", javaFile)
                val uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, name, size")
                    .execute()
                uploadedFile.id
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun downloadBackup(driveService: Drive, fileId: String, outputPath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val outputFile = java.io.File(outputPath)
                outputFile.parentFile?.mkdirs()
                FileOutputStream(outputFile).use { fos ->
                    driveService.files().get(fileId).executeMediaAndDownloadTo(fos)
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun listBackups(driveService: Drive): List<File> {
        return withContext(Dispatchers.IO) {
            try {
                driveService.files().list()
                    .setSpaces("appDataFolder")
                    .setFields("files(id, name, size, modifiedTime)")
                    .setOrderBy("modifiedTime desc")
                    .execute()
                    .files ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}