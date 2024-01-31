package com.example.myapplication

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import java.io.InputStreamReader
import java.io.OutputStreamWriter
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
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier.padding(top = 64.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        TextComponent("What If...", 30.sp)
                        GetMemoryInfo()
                        MemoryInfo()
                        Row(
                        ) {
                            FilledButtonExample(false, "Set Memory")
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

//        totalAvailableSpace = mi.totalMem / 1048576L
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
        val composableScope = rememberCoroutineScope()

        Button(
            enabled = if (clear) true else setEnabled,
            onClick = {
                      if (clear) {
                          freeMemory(context)
                      } else if (userValue > 0){
                          composableScope.launch { useMemory(context) }

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
    fun MemoryInfo() {

        Slider(
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
        Text(text = "Available: ${sliderPosition.toLong()} mb",
            modifier = Modifier.padding(bottom = 32.dp))
        Text(text = "Used Space: $usedSpace / $usableSpace mb",
            modifier = Modifier.padding(bottom = 12.dp))
//        Text(text = "Actual Total Space: $totalAvailableSpace mb",
//            modifier = Modifier.padding(bottom = 64.dp))
    }

    suspend fun useMemory(context: Context) {
        val filename = "myfile"
        val file = File(context.filesDir, filename)
//        var fileLength = file.length()
        var userValueInBytes = (userValue * 1048576L) + file.length()

        while (file.length() < userValueInBytes) {
            val fileContents = "Hello world!".repeat(100)
            context.openFileOutput(filename, Context.MODE_APPEND).use {
                it.write(fileContents.toByteArray())
//                it.close()
            }
            System.out.println("${file.length()} / $userValueInBytes")
            usedSpace = file.length() / 1048576L
        }

        System.out.println("Added ${file.length() / 1048576L} mb")
        sliderPosition = 0f
        userValue = 0f


//        val fileInputStream = context.openFileInput(filename)
//        val inputReader = InputStreamReader(fileInputStream)
//        val output = inputReader.readText()
//
//        System.out.println(file.length())


//        while (fileLength/1024 < userValue) {
//            val mi = ActivityManager.MemoryInfo()
//            val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
//            activityManager.getMemoryInfo(mi)
//
//            val availableMem = mi.availMem / 1048576L
//
//            val fileContents = "Hello world!"
//            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
//                it.write(fileContents.toByteArray())
//            }
//
//            fileLength = file.length()
//        }
    }

    fun freeMemory(context: Context) {
        val filename = "myfile"
        val file = File(context.filesDir, filename)

        if (file.exists()) {
            System.out.println("Deleted!")
            file.delete();
            usedSpace = 0
        }
    }
}

