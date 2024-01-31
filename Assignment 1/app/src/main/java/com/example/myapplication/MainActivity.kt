package com.example.myapplication

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt


class MainActivity (): ComponentActivity() {
    var usableSpace = 0L
    var userValue by mutableFloatStateOf(0F)
    var usedSpace by mutableLongStateOf(0L)
    var sliderPosition by  mutableFloatStateOf(0f)
    var setEnabled by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GetMemoryInfo()
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier.padding(top = 64.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        TextComponent("What If...", 30.sp)
                        MemorySlider()
                        Row(
                        ) {
                            FilledButtonExample(false, "Fill Memory")
                            Spacer(modifier = Modifier.width(48.dp))
                            FilledButtonExample(true, "Free Memory")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun GetMemoryInfo() {
        val mi = ActivityManager.MemoryInfo()
        val context = LocalContext.current
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager

        // Delete file if exists on app launch
        freeMemory(context)
        activityManager.getMemoryInfo(mi)

        val availableMem = mi.availMem / 1048576L

        usableSpace = (availableMem * .1).toLong()
    }

    @Composable
    fun TextComponent(name: String, fontSize: TextUnit) {
        Text(
            text = name,
            fontSize = fontSize,
        )
    }

    @Composable
    fun FilledButtonExample(clear: Boolean, text: String) {
        val context = LocalContext.current
        var startWrite by remember { mutableStateOf(false) }

        if (startWrite) {
            UseMemory(context)
            startWrite = false
        }

        Button(
            enabled = if (clear) true else setEnabled,
            onClick = {
                      if (clear) {
                          freeMemory(context)
                      } else if (userValue > 0){
                          startWrite = true
                      }
            },
            modifier = Modifier.size(100.dp, 100.dp)
        ) {
            Text(
                text = text
            )
        }
    }

    @Composable
    fun MemorySlider() {
        var sliderText = "Available: ${sliderPosition.toLong()} mb"
        var noSpaceLeftText = "Used all the available space!"

        Slider(
            enabled = usedSpace !== usableSpace,
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it.roundToInt().toFloat()
                userValue = sliderPosition
                setEnabled = userValue > 0
                            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            valueRange = 0f..usableSpace.toFloat() - usedSpace.toFloat(),
            modifier = Modifier.padding(top = 64.dp, start = 18.dp, end = 18.dp)
        )
        Text(text = if (usedSpace === usableSpace) noSpaceLeftText else sliderText,
            modifier = Modifier.padding(bottom = 32.dp))
        Text(text = "Used Space: $usedSpace / $usableSpace mb",
            modifier = Modifier.padding(bottom = 12.dp))
    }
    @Composable
    fun UseMemory(context: Context) {
        val composableScope = rememberCoroutineScope()

        val filename = "myfile"
        val file = File(context.filesDir, filename)
        var userValueInBytes = (userValue * 1048576L)
        var fileSizeToReach = userValueInBytes + file.length()

        LaunchedEffect(key1 = Unit){
            composableScope.launch {
                while (file.length() < fileSizeToReach) {
                    val fileContents = "Hello world!".repeat(1000)
                    context.openFileOutput(filename, Context.MODE_APPEND).use {
                        it.write(fileContents.toByteArray())
                    }
                }

                System.out.println("Used Space: ${file.length() / 1048576L} mb")
                usedSpace = file.length() / 1048576L
                sliderPosition = 0f
                userValue = 0f
                setEnabled = false
            }
        }

        AlertDialogExample("Currently Adding ${userValue.toInt()} mb", "Please wait...")
    }

    @Composable
    fun AlertDialogExample(
        dialogTitle: String,
        dialogText: String
    ) {
        AlertDialog(
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = {},
            confirmButton = {},
            dismissButton = {}
        )
    }

    fun freeMemory(context: Context) {
        val filename = "myfile"
        val file = File(context.filesDir, filename)

        if (file.exists()) {
            System.out.println("Deleted!")
            file.delete();
            usedSpace = 0
            setEnabled = false
        }
    }
}

