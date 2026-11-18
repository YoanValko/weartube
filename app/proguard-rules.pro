# ProGuard rules for YT Watch

# Keep application class
-keep class com.ytclient.watch.YTWatchApplication { *; }

# Keep model classes for Parcelable/Serialization
-keep class com.ytclient.watch.model.** { *; }

# Keep ViewModels
-keep class com.ytclient.watch.ui.**ViewModel { *; }

# Keep Retrofit/Gson
-keep class com.ytclient.watch.api.** { *; }
-keep class com.google.gson.** { *; }

# Keep OkHttp
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Keep Coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.activity.** { *; }

# Keep Wear OS
-keep class androidx.wear.** { *; }

# Keep Glide
-keep class com.bumptech.glide.** { *; }
-keep class com.bumptech.glide.module.** { *; }

# Keep DataStore
-keep class androidx.datastore.** { *; }

# Keep Navigation
-keep class androidx.navigation.** { *; }

# WebView (for YouTube embed)
-keep class android.webkit.** { *; }

# Suppress warnings
-dontwarn okio.**
-dontwarn kotlinx.coroutines.**
-dontwarn androidx.compose.**
-dontwarn com.bumptech.glide.**