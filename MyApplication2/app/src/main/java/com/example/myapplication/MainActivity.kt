package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
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
                        SliderAdvancedExample()
                        FilledButtonExample(onClick = { Log.d("Filled button", "Filled button clicked.") })
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

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyApplicationTheme {
//        Greeting("Android")
//    }
//}

@Composable
fun FilledButtonExample(onClick: () -> Unit) {
    Button(onClick = { onClick() },
        modifier = Modifier.size(100.dp, 100.dp),
        ) {
        Text("Set Used")
    }
}

@Composable
fun SliderAdvancedExample() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var sliderText = "Size: $sliderPosition";
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it.roundToInt().toFloat() },
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.secondary,
            activeTrackColor = MaterialTheme.colorScheme.secondary,
            inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        valueRange = 0f..50f,
        modifier = Modifier.padding(top = 64.dp, start = 18.dp, end = 18.dp)
    )
    Text(text = sliderText,
        modifier = Modifier.padding(bottom = 64.dp))
}
