package com.ytclient.watch

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.draw.clip

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
    var currentVideoId by remember { mutableStateOf("dQw4w9WgXcQ") }
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                isPlaying = isPlaying,
                onPlayPause = { isPlaying = !isPlaying },
                onClose = { showPlayer = false },
                onFullscreen = { /* Fullscreen */ },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            when (selectedTab) {
                0 -> HomeScreen(onVideoClick = { videoId ->
                    currentVideoId = videoId
                    showPlayer = true
                    isPlaying = true
                })
                1 -> PlaceholderScreen(title = "Subscriptions")
                2 -> PlaceholderScreen(title = "Search")
                3 -> PlaceholderScreen(title = "Library")
                else -> HomeScreen(onVideoClick = { videoId ->
                    currentVideoId = videoId
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
fun HomeScreen(onVideoClick: (String) -> Unit) {
    val videos = listOf(
        VideoItem("dQw4w9WgXcQ", "Never Gonna Give You Up", "Rick Astley", "3:33", "1.4B views", "4 years ago"),
        VideoItem("jNQXAC9IVRw", "Me at the zoo", "jawed", "0:19", "300M views", "19 years ago"),
        VideoItem("kJQP7kiw5Fk", "Despacito", "Luis Fonsi", "4:42", "8.3B views", "7 years ago"),
        VideoItem("fJ9rUzIMcZQ", "Shape of You", "Ed Sheeran", "3:53", "6.1B views", "7 years ago"),
        VideoItem("kXYiU_JCYtU", "See You Again", "Wiz Khalifa", "3:50", "6.2B views", "7 years ago"),
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videos) { video ->
            VideoCard(
                video = video,
                onClick = { onVideoClick(video.id) }
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
                            fontSize = 10.sp
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
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(colors.surfaceContainer, FoundationRoundedCornerShape(12.dp))
            .clip(FoundationRoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail placeholder
            Box(
                modifier = Modifier
                    .size(120.dp, 68.dp)
                    .background(colors.surfaceContainerHighest, FoundationRoundedCornerShape(8.dp))
                    .clip(FoundationRoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Video thumbnail",
                    tint = colors.onSurfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center)
                )
                
                // Duration badge - wrapped in Box for background
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = colors.surface.copy(alpha = 0.9f),
                                shape = FoundationRoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = video.duration,
                            color = colors.onSurface,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Video info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = video.title,
                    color = colors.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Channel avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(colors.surfaceContainerHighest, CircleShape)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = video.channel,
                            color = colors.onSurfaceVariant,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = video.views,
                                color = colors.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                            Text(
                                text = " • ",
                                color = colors.onSurfaceVariant.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                            Text(
                                text = video.age,
                                color = colors.onSurfaceVariant.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
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
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onClose: () -> Unit,
    onFullscreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        // WebView for YouTube embed
        androidx.compose.ui.viewinterop.AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        setSupportZoom(false)
                        builtInZoomControls = false
                        displayZoomControls = false
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        userAgentString = "Mozilla/5.0 (Linux; Android 10; Pixel Watch) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36"
                    }
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            // Inject CSS to hide YouTube branding and controls we don't need
                            view?.loadUrl("javascript:(function() { " +
                                "var style = document.createElement('style'); " +
                                "style.innerHTML = '.ytp-chrome-bottom { display: none !important; } .ytp-chrome-top { display: none !important; }'; " +
                                "document.head.appendChild(style); " +
                                "})()")
                        }
                    }
                    webChromeClient = object : WebChromeClient() {
                        override fun onShowCustomView(view: android.view.View?, callback: WebChromeClient.CustomViewCallback?) {
                            super.onShowCustomView(view, callback)
                        }
                        override fun onHideCustomView() {
                            super.onHideCustomView()
                        }
                    }
                    settings.apply {
                        setAllowFileAccess(false)
                        setAllowContentAccess(true)
                        setAllowUniversalAccessFromFileURLs(false)
                        setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
                    }
                    loadUrl("https://www.youtube.com/embed/$videoId?autoplay=1&controls=1&modestbranding=1&rel=0&playsinline=1&fs=1")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Controls overlay
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Progress bar
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = colors.primary,
                trackColor = colors.onSurfaceVariant.copy(alpha = 0.3f)
            )
            
            // Controls
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = colors.onSurface,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        IconButton(onClick = onPlayPause) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = colors.onSurface,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onFullscreen) {
                            Icon(
                                imageVector = Icons.Filled.Fullscreen,
                                contentDescription = "Fullscreen",
                                tint = colors.onSurface,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}