package com.frank.drawcompose

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.frank.drawcompose.ui.theme.DrawComposeTheme
import java.util.Timer
import java.util.TimerTask
import kotlin.system.exitProcess

const val PI = 3.1415927F
val color: Array<Color> = arrayOf(
    Color.Red,
    Color.Blue,
    Color.Green,
    Color.Cyan,
    Color.White,
    Color.Magenta,
    Color.Yellow,
    Color.Gray,
    Color.DarkGray
)

var _timer: Timer? = null
private var _timerTask: TimerTask? = null
var showType by mutableStateOf(0)
var screenHeight = 0.0f
var screenWidth = 0.0f
var r by mutableStateOf(0)
var drawIndex by mutableStateOf(0)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawComposeTheme {
                when (showType) {
                    0 ->
                        Column(Modifier.background(Color.Black)) {
                            Row(
                                Modifier
                                    .weight(1.0f)
                                    .background(Color.Black)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.dog),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(start = 20.dp)
                                        .clickable {
                                            Toast
                                                .makeText(
                                                    applicationContext,
                                                    R.string.dog_help,
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                )
                                DrawDog(
                                    Modifier
                                        .weight(1.0f)
                                        .clickable {
                                            drawIndex = 0
                                            dogCount = 4
                                            showType = 1
                                        }
                                )
                            }
                            Row(
                                Modifier
                                    .weight(1.0f)
                                    .background(Color.Black)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.snow),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(15.dp)
                                        .clickable {
                                            Toast
                                                .makeText(
                                                    applicationContext,
                                                    R.string.snow_help,
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                )
                                DrawSnow(
                                    Modifier
                                        .weight(1.0f)
                                        .clickable {
                                            drawIndex = 0
                                            showType = 2
                                        }
                                )
                            }
                            Row(
                                Modifier
                                    .weight(1.0f)
                                    .background(Color.Black)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.rings),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(76.dp)
                                        .padding(15.dp)
                                        .clickable {
                                            Toast
                                                .makeText(
                                                    applicationContext,
                                                    R.string.rings_help,
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                )
                                DrawRings(
                                    Modifier
                                        .weight(1.0f)
                                        .clickable {
                                            myDegrees = 90F
                                            showType = 3
                                            for(i in 0 until status.size) {
                                                status[i] = true
                                            }
                                            isRingsOver = false
                                            Thread {
                                                toRings()
                                                isRingsOver = true
                                            }.start()
                                        }
                                )
                            }
                        }

                    1 -> {
                        startDrawTimer()
                        DrawDog(
                            modifier = Modifier.pointerInteropFilter {
                                when (it.action) {
                                    MotionEvent.ACTION_MOVE -> {
                                        if (it.y < screenHeight / 3) {
                                            if (_timer == null) {
                                                dogCount = 3 + (it.x / (screenWidth / 7)).toInt()
                                                if (dogCount < 3) {
                                                    dogCount = 3
                                                }
                                                if (dogCount > 9) {
                                                    dogCount = 9
                                                }
                                            }
                                        } else if (it.y < screenHeight * 2 / 3) {
                                            r = (it.x / 2).toInt()
                                        } else {
                                            jDelta = (3 * PI * it.x / screenWidth)
                                        }
                                    }
                                }
                                true
                            }
                        )
                    }

                    2 -> {
                        startDrawTimer()
                        DrawSnow(
                            modifier = Modifier.clickable {
                                drawIndex = picString[LoopNum - 1].length - 10
                                isSnowOver = true
                            }
                        )
                    }

                    3 -> {
                        DrawRings(
                            modifier = Modifier.clickable {
                                if (isRingsOver) {
                                    isRingsOver = false
                                    _isUp = !_isUp
                                    Thread {
                                        toRings()
                                        isRingsOver = true
                                    }.start()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    //@Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (showType == 0) {
            exitProcess(0)
        } else {
            stopDrawTimer()
            if (showType == 2) {
                isSnowOver = false
            } else {
                isRingsOver = true
            }
            myDegrees = 0F
            status[0] = true
            status[1] = true
            status[2] = false
            status[3] = true
            status[4] = false
            status[5] = true
            status[6] = false
            status[7] = false
            status[8] = true
            showType = 0
        }
    }
}

fun startDrawTimer() {
    drawIndex = 0
    _timer = Timer()
    _timerTask = object : TimerTask() {
        override fun run() {
            if (showType == 3) {
                for(i in 0 until status.size) {
                    status[i] = true
                }
            } else {
                drawIndex++
            }
        }
    }
    _timer!!.schedule(_timerTask, 0, 20)
}

fun stopDrawTimer() {
    if (_timerTask != null) {
        _timerTask!!.cancel()
        _timerTask = null
    }
    if (_timer != null) {
        _timer!!.cancel()
        _timer!!.purge()
        _timer = null
    }
}
