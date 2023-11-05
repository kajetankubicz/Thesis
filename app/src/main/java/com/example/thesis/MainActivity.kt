package com.example.thesis

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.thesis.ui.theme.ThesisTheme
import androidx.compose.foundation.layout.fillMaxSize
import com.google.accompanist.pager.ExperimentalPagerApi


class MainActivity : ComponentActivity() {

    lateinit var fontSizeManager: FontSizeManager

    override fun attachBaseContext(newBase: Context) {
        fontSizeManager = FontSizeManager(newBase.prefs())
        val newConfig = Configuration(newBase.resources.configuration)
        newConfig.fontScale = fontSizeManager.fontSize.scale
        applyOverrideConfiguration(newConfig)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ThesisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = LastViewedPage.BookDetailsViewModel())
                }
            }
        }
    }

    private fun updateFontSize(fontSize: FontSize) {
        fontSizeManager.fontSize = fontSize
        recreate()
    }
}

fun Context.prefs(): SharedPreferences = getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE)

class FontSizeManager(private val prefs: SharedPreferences) {

    private val unsetFontSizeValue = -1f

    var fontSize: FontSize
        get() {
            val scale = prefs.getFloat("font_scale", unsetFontSizeValue)
            return if (scale == unsetFontSizeValue) {
                FontSize.DEFAULT
            } else {
                FontSize.values().first { fontSize -> fontSize.scale == scale }
            }
        }
        set(value) {
            prefs.edit()
                .putFloat("font_scale", value.scale)
                .apply()
        }

}

enum class FontSize(val scale: Float) {
    SMALL(1.1f),
    DEFAULT(1.3f),
    LARGE(1.5f)
}

