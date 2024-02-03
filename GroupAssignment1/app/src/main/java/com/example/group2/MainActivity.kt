package com.example.group2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import com.example.group2.ui.theme.Group2Theme
import kotlinx.coroutines.launch
import me.bush.translator.Language
import me.bush.translator.Translator
import me.bush.translator.languageOf

//import com.github.

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Group2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        val composableScope = rememberCoroutineScope()
        var textResult by remember { mutableStateOf("") }

        LaunchedEffect(key1 = Unit) {
            composableScope.launch {
                textResult = getText()
            }
        }

        Text(
            text = textResult,
            modifier = modifier
        )
    }

    suspend fun getText(): String {
        var sysLang = Locale.current.language.lowercase()
        var language = languageOf(sysLang) ?: Language.ENGLISH

        val translator = Translator()
        return translator.translate("Hello World!", language , Language.AUTO).translatedText
    }

}