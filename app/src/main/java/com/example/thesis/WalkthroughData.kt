package com.example.thesis

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

data class WalkthroughData(val image: Int, val title: String, val desc: String)

@Composable
fun LoaderIntro(modifier: Modifier, image: Int) {
    Image(
        painter = painterResource(id = image),
        contentDescription = null, // You can set a proper content description if needed
        modifier = modifier
    )
}
