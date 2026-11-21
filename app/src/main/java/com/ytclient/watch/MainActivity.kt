package com.ytclient.watch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape as FoundationRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    YTWatchScreen()
                }
            }
        }
    }
}

@Composable
fun YTWatchScreen() {
    val context = LocalContext.current
    var showPlayer by remember { mutableStateOf(false) }
    var currentVideoId by remember { mutableStateOf("") }
    var currentVideoTitle by remember { mutableStateOf("") }
    var currentVideoChannel by remember { mutableStateOf("") }
    var isPlaying by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "Subscriptions", "Search", "Library")

    BackHandler(enabled = showPlayer) {
        showPlayer = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "YT Watch",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (!showPlayer) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { /* Search */ }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Main content
        if (showPlayer) {
            VideoPlayerScreen(
                videoId = currentVideoId,
                videoTitle = currentVideoTitle,
                videoChannel = currentVideoChannel,
                isPlaying = isPlaying,
                onPlayPause = { isPlaying = !isPlaying },
                onClose = { showPlayer = false },
                onFullscreen = { /* Fullscreen */ },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            when (selectedTab) {
                0 -> HomeScreen(onVideoClick = { videoId, title, channel ->
                    currentVideoId = videoId
                    currentVideoTitle = title
                    currentVideoChannel = channel
                    showPlayer = true
                    isPlaying = true
                })
                1 -> PlaceholderScreen(title = "Subscriptions")
                2 -> PlaceholderScreen(title = "Search")
                3 -> PlaceholderScreen(title = "Library")
                else -> HomeScreen(onVideoClick = { videoId, title, channel ->
                    currentVideoId = videoId
                    currentVideoTitle = title
                    currentVideoChannel = channel
                    showPlayer = true
                    isPlaying = true
                })
            }
        }

        // Bottom navigation (only when player not visible)
        if (!showPlayer) {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs
            )
        }
    }
}

@Composable
fun HomeScreen(onVideoClick: (String, String, String) -> Unit) {
    val videos = listOf(
        VideoItem("dQw4w9WgXcQ", "Never Gonna Give You Up", "Rick Astley", "3:33", "1.4B views", "4 years ago"),
        VideoItem("jNQXAC9IVRw", "Me at the zoo", "jawed", "0:19", "300M views", "19 years ago"),
        VideoItem("kJQP7kiw5Fk", "Despacito", "Luis Fonsi", "4:42", "8.3B views", "7 years ago"),
        VideoItem("fJ9rUzIMcZQ", "Shape of You", "Ed Sheeran", "3:53", "6.1B views", "7 years ago"),
        VideoItem("kXYiU_JCYtU", "See You Again", "Wiz Khalifa", "3:50", "6.2B views", "7 years ago"),
        VideoItem("9bZkp7q19f0", "PSY - GANGNAM STYLE", "PsyOfficial", "4:13", "4.6B views", "12 years ago"),
        VideoItem("2Vv-BfVoq4g", "Shape of You - Live", "Ed Sheeran", "4:26", "23M views", "7 years ago"),
        VideoItem("kffacxfA7G4", "Baby Shark Dance", "Pinkfong", "3:15", "13B views", "5 years ago"),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(videos) { video ->
            VideoCard(
                video = video,
                onClick = { onVideoClick(video.id, video.title, video.channel) }
            )
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$title screen coming soon...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                val selected = (selectedTab == index)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable(onClick = { onTabSelected(index) }),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = when (index) {
                                0 -> Icons.Filled.Home
                                1 -> Icons.Filled.PlayArrow
                                2 -> Icons.Filled.Search
                                3 -> Icons.Filled.PlayArrow
                                else -> Icons.Filled.Home
                            },
                            contentDescription = tab,
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = tab,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp,
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VideoCard(
    video: VideoItem,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val thumbnailUrl = "https://i.ytimg.com/vi/${video.id}/hqdefault.jpg"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Thumbnail with duration badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(colors.surfaceContainerHighest, FoundationRoundedCornerShape(8.dp))
                    .clip(FoundationRoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xB3000000))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                            .clip(FoundationRoundedCornerShape(2.dp))
                    ) {
                        Text(
                            text = video.duration,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Video info
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(colors.surfaceContainerHighest, CircleShape)
                    ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(14.dp).align(Alignment.Center)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = video.title,
                        color = colors.onSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = video.channel,
                        color = colors.onSurfaceVariant,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = video.views,
                            color = colors.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                        Text(
                            text = " • ",
                            color = colors.onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 10.sp
                        )
                        Text(
                            text = video.age,
                            color = colors.onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

data class VideoItem(
    val id: String,
    val title: String,
    val channel: String,
    val duration: String,
    val views: String,
    val age: String
)

@Composable
fun VideoPlayerScreen(
    videoId: String,
    videoTitle: String,
    videoChannel: String,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onClose: () -> Unit,
    onFullscreen: () -> Unit,
    modifier: Modifier = Modifier,
    context: android.content.Context = LocalContext.current
) {
    val colors = MaterialTheme.colorScheme
    var streamInfo by remember { mutableStateOf<YouTubeStreamExtractor.StreamInfo?>(null) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var videoViewError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    var videoView by remember { mutableStateOf<VideoView?>(null) }

    // Extract stream URL when videoId changes
    LaunchedEffect(videoId) {
        streamInfo = null
        loadError = null
        isLoading = true

        try {
            val extractor = YouTubeStreamExtractor()
            val info = extractor.extractStream(videoId)
            streamInfo = info

            if (info.videoUrl == null) {
                loadError = "No playable streams found"
                videoViewError = true
            }
        } catch (e: Exception) {
            loadError = "Failed to load video: ${e.message ?: "unknown error"}"
        }
        isLoading = false
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    color = colors.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "Loading video...",
                    color = colors.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else if (loadError != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error",
                        tint = colors.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Cannot play video",
                        color = colors.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = loadError!!,
                        color = colors.onSurfaceVariant,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { loadError = null; isLoading = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Try Again")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = { onClose() }) {
                        Text(text = "Back to Videos")
                    }
                }
            }
        } else if (videoViewError) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    tint = colors.error,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Cannot play video",
                    color = colors.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Stream could not be loaded",
                    color = colors.onSurfaceVariant,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { videoViewError = false; isLoading = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Try Again")
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = { onClose() }) {
                    Text(text = "Back to Videos")
                }
            }
        } else {
            // Show VideoView with stream URL
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            videoView = this
                            streamInfo?.videoUrl?.let { url ->
                                setVideoURI(android.net.Uri.parse(url))
                                if (isPlaying) start()
                            }
                            setOnErrorListener { _, _, _ ->
                                loadError = "Video playback failed"
                                false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = {
                        streamInfo?.videoUrl?.let { url ->
                            it.setVideoURI(android.net.Uri.parse(url))
                            if (isPlaying) it.start() else it.pause()
                        }
                    }
                )

                // Video title overlay at bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = videoTitle,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = videoChannel,
                            color = Color(0xFFFFFFFF).copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }

                // Close button overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Play/pause button overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (videoView == null || !videoView!!.isPlaying()) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xB3000000))
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
