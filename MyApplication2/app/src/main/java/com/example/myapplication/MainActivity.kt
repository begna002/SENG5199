package com.example.myapplication

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    var size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier.padding(top = 64.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        TextComponent("What If...", 30.sp)
                        MemoryInfo()
                        Row(
                        ) {
                            FilledButtonExample(onClick = { Log.d("Filled button", "Filled button clicked.") }, "Set Memory")
                            Spacer(modifier = Modifier.width(48.dp))
                            FilledButtonExample(onClick = { Log.d("Filled button", "Filled button clicked.") }, "Free Memory")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextComponent(name: String, fontSize: TextUnit) {
    Text(
        text = name,
        fontSize = fontSize,
    )
}

@Composable
fun FilledButtonExample(onClick: () -> Unit, text: String) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.size(100.dp, 100.dp)
    ) {
        Text(
            text = text
        )
    }
}

@Composable
fun MemoryInfo() {
    // Put this code in the class level ---->
    val mi = ActivityManager.MemoryInfo()
    val context = LocalContext.current
    val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    val freeMemory = mi.availMem / 1048576L
    val availableMemory = (freeMemory * .8).toLong()
    //<------

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var sliderText = "Size: ${sliderPosition.toLong()} / $availableMemory mb";
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it.roundToInt().toFloat() },
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.secondary,
            activeTrackColor = MaterialTheme.colorScheme.secondary,
            inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        valueRange = 0f..availableMemory.toFloat(),
        modifier = Modifier.padding(top = 64.dp, start = 18.dp, end = 18.dp)
    )
    Text(text = sliderText,
        modifier = Modifier.padding(bottom = 64.dp))
}