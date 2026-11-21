package com.ytclient.watch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * Extracts direct YouTube stream URLs using the TubeRepair API.
 *
 * TubeRepair is a custom YouTube server that proxies YouTube content and returns
 * direct stream URLs without requiring signature cipher decryption.
 *
 * API: https://tuberepair.uptimetrackers.com/
 * Endpoint: GET /<resolution>/getvideo/<video_id>
 * Returns: Direct MP4 stream URL (307 redirect) or HLS m3u8 playlist
 *
 * Reference: https://github.com/kevinf100/tuberepair.uptimetrackers.com
 */
class YouTubeStreamExtractor {

    companion object {
        const val TUBEREPAIR_BASE_URL = "https://tuberepair.uptimetrackers.com"
        const val DEFAULT_RESOLUTION = "360"
    }

    data class StreamInfo(
        val videoUrl: String?,
        val audioUrl: String?,
        val title: String?,
        val duration: String?,
        val thumbnail: String?
    )

    /**
     * Extracts a direct stream URL for a YouTube video ID using TubeRepair API.
     * Returns a direct playable MP4 URL that can be used with VideoView/ExoPlayer.
     */
    suspend fun extractStream(videoId: String): StreamInfo {
        return withContext(Dispatchers.IO) {
            try {
                // Follow redirects to get the final stream URL
                val streamUrl = resolveStreamUrl("$DEFAULT_RESOLUTION/getvideo/$videoId")
                
                StreamInfo(
                    videoUrl = streamUrl,
                    audioUrl = null, // TubeRepair provides combined video+audio
                    title = null,   // TubeRepair doesn't return metadata
                    duration = null,
                    thumbnail = null
                )
            } catch (e: Exception) {
                StreamInfo(
                    videoUrl = null,
                    audioUrl = null,
                    title = null,
                    duration = null,
                    thumbnail = null
                )
            }
        }
    }

    /**
     * Resolves the final stream URL by following TubeRepair redirects.
     */
    private fun resolveStreamUrl(path: String): String? {
        var currentUrl = "$TUBEREPAIR_BASE_URL/$path"
        var maxRedirects = 10
        
        while (maxRedirects > 0) {
            maxRedirects--
            
            val url = URL(currentUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "YouTube/1.0.0")
            connection.instanceFollowRedirects = false // Don't auto-follow, we handle it
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            
            val responseCode = connection.responseCode
            
            // Handle redirect (301, 302, 307, 308)
            when (responseCode) {
                301, 302, 303, 307, 308 -> {
                    val location = connection.getHeaderField("Location") ?: break
                    currentUrl = if (location.startsWith("http")) location else "$TUBEREPAIR_BASE_URL$location"
                    connection.disconnect()
                    continue
                }
                200 -> {
                    // Direct response - return the URL
                    connection.disconnect()
                    return currentUrl
                }
                else -> {
                    connection.disconnect()
                    return null
                }
            }
        }
        
        return currentUrl
    }
}
