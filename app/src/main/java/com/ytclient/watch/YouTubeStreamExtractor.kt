@file:Suppress("DEPRECATION")

package com.ytclient.watch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class YouTubeStreamExtractor {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    companion object {
        const val INNERTUBE_API_KEY = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"
        const val INNERTUBE_URL = "https://www.youtube.com/youtubei/v1/player?key=$INNERTUBE_API_KEY"
        const val ANDROID_VR_CLIENT_NAME = "ANDROID_VR"
        const val ANDROID_VR_CLIENT_VERSION = "1.57.29"
        const val ANDROID_VR_USER_AGENT = "com.google.android.apps.youtube.vr.oculus/1.57.29 (Linux; U; Android 12L; eureka-user Build/SQ3A.220605.009.A1) gzip"
    }

    data class StreamInfo(
        val videoUrl: String?,
        val audioUrl: String?,
        val title: String?,
        val duration: String?,
        val thumbnail: String?
    )

    @Suppress("DEPRECATION")
    suspend fun extractStream(videoId: String): StreamInfo {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = JSONObject().apply {
                    put("videoId", videoId)
                    put("context", JSONObject().apply {
                        put("client", JSONObject().apply {
                            put("clientName", ANDROID_VR_CLIENT_NAME)
                            put("clientVersion", ANDROID_VR_CLIENT_VERSION)
                            put("hl", "en")
                        })
                    })
                    put("playbackContext", JSONObject().apply {
                        put("contentPlaybackContext", JSONObject().apply {
                            put("html5Preference", "HTML5_PREF_WANTS")
                        })
                    })
                }.toString()

                val body = RequestBody.create(null, requestBody)
                val request = Request.Builder()
                    .url(INNERTUBE_URL)
                    .post(body)
                    .header("User-Agent", ANDROID_VR_USER_AGENT)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""
                response.close()

                if (!response.isSuccessful) {
                    StreamInfo(null, null, null, null, null)
                } else {
                    parseInnertubeResponse(responseBody)
                }
            } catch (e: Exception) {
                StreamInfo(null, null, null, null, null)
            }
        }
    }

    private fun parseInnertubeResponse(json: String): StreamInfo {
        val parsed = JSONObject(json)

        val videoDetails = parsed.optJSONObject("videoDetails")
        val title = videoDetails?.optString("title")

        var duration: String? = null
        videoDetails?.optString("lengthSeconds")?.let { lengthStr ->
            val secs = lengthStr.toIntOrNull() ?: 0
            val min = secs / 60
            val sec = secs % 60
            duration = "$min:${sec.toString().padStart(2, '0')}"
        }

        val thumbnail = videoDetails?.optJSONObject("thumbnail")?.optJSONArray("thumbnails")?.optJSONObject(0)?.optString("url")

        val streamingData = parsed.optJSONObject("streamingData")
        val adaptiveFormats = streamingData?.optJSONArray("adaptiveFormats")

        var videoUrl: String? = null
        var audioUrl: String? = null

        if (adaptiveFormats != null) {
            for (i in 0 until adaptiveFormats.length()) {
                val format = adaptiveFormats.getJSONObject(i)
                val mimeType = format.optString("mimeType", "")
                val url = format.optString("url")

                if (url.isEmpty()) continue

                if (mimeType.contains("video") && videoUrl == null) {
                    videoUrl = url
                } else if (mimeType.contains("audio") && audioUrl == null) {
                    audioUrl = url
                }

                if (videoUrl != null && audioUrl != null) break
            }
        }

        return StreamInfo(
            videoUrl = videoUrl,
            audioUrl = audioUrl,
            title = title,
            duration = duration,
            thumbnail = thumbnail
        )
    }
}
