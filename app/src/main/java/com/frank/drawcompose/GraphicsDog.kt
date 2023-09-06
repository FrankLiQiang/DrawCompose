package com.frank.drawcompose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

var margin = 0f
var dogCount by mutableStateOf(5)
var jDelta by mutableStateOf(3 * PI / 2)

@Composable
fun DrawDog(modifier: Modifier = Modifier) {
    val x = FloatArray(9)
    val y = FloatArray(9)
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val centerX = size.width / 2
        val centerY = size.height / 2

        if (showType != 0 && _timer != null) {
            margin = size.width / 20
            screenWidth = size.width
            screenHeight = size.height
            x[0] = margin
            x[1] = margin
            x[2] = size.width - margin
            x[3] = size.width - margin
            y[0] = (size.height - (size.width - 2 * margin)) / 2
            y[1] = y[0] + size.width - 2 * margin
            y[2] = y[1]
            y[3] = y[0]
        }
        fun drawRect(x: FloatArray, y: FloatArray) {
            for (i in 0 until dogCount) {
                if (i > 0) {
                    drawLine(
                        start = Offset(x = x[i], y = y[i]),
                        end = Offset(x = x[i - 1], y = y[i - 1]),
                        color = color[i]
                    )
                } else {
                    drawLine(
                        start = Offset(x = x[0], y = y[0]),
                        end = Offset(x = x[dogCount - 1], y = y[dogCount - 1]),
                        color = color[i]
                    )
                }
            }
        }

        if (showType == 0 || _timer == null) {
            if (showType == 0) r = (size.width / 3).toInt()
            for (i in 0 until dogCount) {
                x[i] = centerX + r * cos(2 * PI / dogCount * i + jDelta)
                y[i] = centerY - (r * sin(2 * PI / dogCount * i + jDelta))
            }
        }
        var m: Int
        var n: Int
        var a: Float
        var b: Float
        var k: Float
        var count = 0
        var i = 0
        while (abs(x[0] - x[2]) + abs(y[0] - y[2]) > 3) {
            if (count == 0) {
                drawRect(x, y)
                if (showType == 0 || _timer == null) count = 40
            }
            count--
            n = 0
            while (n < dogCount) {
                drawLine(
                    start = Offset(x = x[n], y = y[n]),
                    end = Offset(x = x[n] + 1, y = y[n] + 1),
                    color = color[n]
                )
                m = if (n > 0) n - 1 else dogCount - 1
                k = (y[n] - y[m]) / (x[n] - x[m])
                a = (if (x[m] > x[n]) 1 else -1).toFloat()
                b = (if (y[m] > y[n]) 1 else -1).toFloat()
                x[n] = if (abs(k) <= 1) x[n] + a else x[n] + 1 / k * b
                y[n] = if (abs(k) < 1) y[n] + k * a else y[n] + b
                n++
            }
            i++
            if (showType == 1 && i >= drawIndex && _timer != null) {
                return@Canvas
            }
            if (abs(x[0] - x[2]) + abs(y[0] - y[2]) <= 3) {
                if (showType == 1) {
                    stopDrawTimer()
                }
                return@Canvas
            }
        }
    }
}
