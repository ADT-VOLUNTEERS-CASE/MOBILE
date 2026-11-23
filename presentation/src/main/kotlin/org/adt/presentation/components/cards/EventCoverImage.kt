package org.adt.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Precision
import org.adt.core.entities.event.Cover
import org.adt.presentation.di.Application
import org.adt.presentation.theme.Milk
import java.net.URI
import java.net.URISyntaxException
import java.util.UUID

private const val CATALOG_COVER_SIZE_PX = 384

@Composable
internal fun EventCoverImage(
    cover: Cover?,
    modifier: Modifier = Modifier,
    catalogThumbnail: Boolean = false,
) {
    val context = LocalContext.current
    val link = cover?.takeIf { it.deletedAt == null }?.link?.takeIf { it.isNotBlank() }
    val key = remember(cover) { cover?.versionedCacheKey() }
    val imageLoader = if (key != null) {
        (context.applicationContext as? Application)?.versionedCoverImageLoader ?: context.imageLoader
    } else {
        context.imageLoader
    }
    val request = remember(context, link, key, catalogThumbnail) {
        ImageRequest.Builder(context)
            .data(link)
            .crossfade(false)
            .precision(Precision.INEXACT)
            .apply {
                if (catalogThumbnail) size(CATALOG_COVER_SIZE_PX)
                if (key != null) {
                    diskCacheKey(key)
                    val thumbnailKey = "$key:catalog"
                    memoryCacheKey(if (catalogThumbnail) thumbnailKey else key)
                    if (!catalogThumbnail) placeholderMemoryCacheKey(thumbnailKey)
                }
            }
            .build()
    }
    AsyncImage(
        model = request,
        imageLoader = imageLoader,
        contentDescription = null,
        modifier = modifier.background(Milk),
        contentScale = ContentScale.Crop,
    )
}

internal fun Cover.versionedCacheKey(): String? {
    if (deletedAt != null) return null
    val objectKey = fileMetadata.objectKey
    val fileName = objectKey.substringAfterLast('/')
    if (fileName.getOrNull(36) != '-') return null
    val uuid = fileName.substring(0, 36)
    try {
        if (UUID.fromString(uuid).toString() != uuid.lowercase()) return null
    } catch (_: IllegalArgumentException) {
        return null
    }

    val uri = try {
        URI(link)
    } catch (_: URISyntaxException) {
        return null
    }
    if (uri.scheme !in listOf("http", "https") || uri.host == null ||
        uri.userInfo != null || uri.path?.endsWith("/$objectKey") != true
    ) return null

    // S3 replacements get a new UUID path. Keep the ETag to distinguish versions of an object.
    return "event-cover:${uri.scheme}://${uri.rawAuthority}${uri.rawPath}:${fileMetadata.eTag}"
}
