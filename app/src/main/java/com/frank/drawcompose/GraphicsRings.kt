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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate

val status = booleanArrayOf(true, true, false, true, false, true, false, false, true)
var myDegrees = 0F
var _isUp by mutableStateOf(false)

@Composable
fun DrawRings(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        rotate(degrees = myDegrees) {
            drawLine(
                start = Offset(
                    x = if (myDegrees == 0f) 0f else -2 * size.height,
                    y = size.height / 3
                ),
                end = Offset(x = size.height, y = size.height / 3),
                color = Color.Red
            )
            drawLine(
                start = Offset(
                    x = if (myDegrees == 0f) 0f else -2 * size.height,
                    y = size.height * 2 / 3
                ),
                end = Offset(x = size.height, y = size.height * 2 / 3),
                color = Color.Blue
            )

            var myLeft = if (myDegrees == 0f) size.width / 10f else size.height / 10f

            if (drawIndex > 0 || myDegrees == 0f) {
                for (i in 0..8) {
                    if (status[8 - i]) {
                        translate(
                            left = myLeft - myLeft * (5 - i),
                            top = -size.height / 6
                        ) {
                            drawCircle(color[i], radius = size.height / 23, style = Stroke())
                        }
                    } else {
                        translate(
                            left = myLeft - myLeft * (5 - i),
                            top = size.height / 6
                        ) {
                            drawCircle(color[i], radius = size.height / 23, style = Stroke())
                        }
                    }
                }
            }
        }
    }
}

fun down(n: Int) {
    if (isRingsOver) return
    if (n < 0) {
        return
    }
    if (n == 0) {
        draw(n, false)
        return
    }
    if (status[n - 1]) {
        for (i in n - 2 downTo 0) {
            if (status[i]) {
                down(i)
                draw(n, false)
                down(n - 1)
                return
            }
        }
        draw(n, false)
        down(n - 1)
        return
    } else {
        up(n - 1)
        down(n - 2)
        draw(n, false)
        down(n - 1)
    }
}

fun up(n: Int) {
    if (isRingsOver) return
    if (n < 0) {
        return
    }
    if (n == 0) {
        draw(n, true)
        return
    }
    if (status[n - 1]) {
        for (i in n - 2 downTo 0) {
            if (status[i]) {
                down(i)
                draw(n, true)
                up(n - 2)
                return
            }
        }
        draw(n, true)
        up(n - 2)
        return
    } else {
        up(n - 1)
        down(n - 2)
        draw(n, true)
        up(n - 2)
    }
}

private fun draw(n: Int, isUp: Boolean) {
    if (isRingsOver) return
    status[n] = isUp
    drawIndex++
    Thread.sleep(100L)
}

fun toRings() {
    if (_isUp) {
        up(9 - 1)
    } else {
        down(9 - 1)
    }
}