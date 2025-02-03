package com.hamy.hubmovies.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.hamy.hubmovies.ui.screens.activity.MainActivityViewModel
import com.hamy.hubmovies.utils.Constants.MyLOGS
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

object Extensions {
    fun mLogs(data: String) {
        Log.e(MyLOGS, data)
    }

    fun mToast(data: String, context: Context) {
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
    }

    fun isValidPassword(password: String, confirmPassword: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[@#\$%^&+=!]).{6,12}$".toRegex()
        return password == confirmPassword && password.matches(passwordRegex)
    }

    @Composable
    fun GenericErrorScreen(onRetry: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Oops! Something went wrong.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text(text = "Try Again")
                }
            }
        }
    }


    @Composable
    fun TextWithIcon(text: String, icon: ImageVector, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Red),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
                    .clickable { onClick() },
                text = text,
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = Color.White,
            )
            Spacer(modifier = Modifier.width(4.dp)) // Add some space between text and icon
            Icon(
                imageVector = icon,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.size(16.dp) // Adjust icon size as needed
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun FullScreenYouTubePlayer(videoId: String, onClose: () -> Unit) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        var isFullScreen by remember { mutableStateOf(true) }
        val activity = context as? Activity

        fun setFullScreen(fullScreen: Boolean) {
            isFullScreen = fullScreen
            activity?.window?.apply {
                if (fullScreen) {
                    (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    insetsController?.hide(WindowInsets.Type.systemBars()) // Hide system bars
                    addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                } else {
                    (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    insetsController?.show(WindowInsets.Type.systemBars()) // Show system bars
                    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
            }
        }

        DisposableEffect(isFullScreen) {
            onDispose { setFullScreen(false) } // Restore system UI when exiting
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)) {
            // 🎥 YouTube Player
            AndroidView(
                factory = { ctx ->
                    YouTubePlayerView(ctx).apply {
                        enableAutomaticInitialization = false
                        lifecycleOwner.lifecycle.addObserver(this)
                        initialize(object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.loadVideo(videoId, 0f)
                                youTubePlayer.play()
                            }
                        })
                    }
                },
                modifier = if (isFullScreen) Modifier.fillMaxSize() else Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            // ❌ Close Button (Top Right)
            IconButton(
                onClick = {
                    // (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    setFullScreen(false) // Exit fullscreen before closing
                    onClose()
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
            }

            // 🔄 Fullscreen Toggle Button (Bottom Right)
            IconButton(
                onClick = {
                    (context as? Activity)?.requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isFullScreen) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
                    contentDescription = "Toggle Fullscreen",
                    tint = Color.White
                )
            }
        }
    }


    @Composable
    fun ShimmerDescriptionPage() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ShimmerColumn()
                Divider(
                    modifier = Modifier
                        .height(25.dp)
                        .width(1.dp),
                    color = Color.Gray // Change color as needed
                )
                ShimmerColumn()
                Divider(
                    modifier = Modifier
                        .height(25.dp)
                        .width(1.dp),
                    color = Color.Gray // Change color as needed
                )
                ShimmerColumn()
                Divider(
                    modifier = Modifier
                        .height(25.dp)
                        .width(1.dp),
                    color = Color.Gray // Change color as needed
                )
                ShimmerColumn()
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .width(100.dp)
                            .height(250.dp)
                            .shimmerEffect()
                    )
                }
            }
        }
    }

    @Composable
    fun ShimmerColumn() {
        Column(modifier = Modifier.wrapContentSize()) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(15.dp)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(15.dp)
                    .shimmerEffect()
            )
        }
    }

    fun Modifier.shimmerEffect(): Modifier = composed {
        var size by remember { mutableStateOf(IntSize.Zero) }
        val transition = rememberInfiniteTransition(label = "")
        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )

        background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFB8B5B5),
                    Color(0xFF8F8B8B),
                    Color(0xFFB8B5B5),
                ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
            )
        ).onGloballyPositioned {
            size = it.size
        }
    }

    @Composable
    fun mBackPressHandle(
        navController: NavHostController,
        splashViewModel: MainActivityViewModel
    ) {
        var lastClickTime by remember { mutableLongStateOf(0L) }
        val debounceInterval = 500L // Interval in milliseconds
        BackHandler {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > debounceInterval) {
                lastClickTime = currentTime
                splashViewModel.bottomTabsVisible = true // Show bottom tabs
                navController.popBackStack()
            }
        }
    }
}