package org.adt.presentation.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

internal suspend fun saveReportToDownloads(context: Context, bytes: ByteArray): Boolean =
    withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        var pendingUri: Uri? = null
        try {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "report-${System.currentTimeMillis()}.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: return@withContext false
            pendingUri = uri
            val output = resolver.openOutputStream(uri) ?: throw IOException("No output stream")
            output.use { it.write(bytes) }
            if (resolver.update(uri, ContentValues().apply { put(MediaStore.MediaColumns.IS_PENDING, 0) }, null, null) == 0) {
                throw IOException("Could not publish report")
            }
            true
        } catch (e: Exception) {
            pendingUri?.let { uri -> runCatching { resolver.delete(uri, null, null) } }
            Log.e("ReportDownloads", "Failed to save report", e)
            false
        }
    }
