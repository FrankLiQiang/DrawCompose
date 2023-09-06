package com.frank.drawcompose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

var x_Start = 0.0f
var y_Start = 0.0f
var LoopNum = 2
var picString = arrayOf("", "", "", "")
var edge = 0
var startDirFirst = 2
private const val G3 = 1.732f
private var edge2 = edge / 2
private var edge32 = edge2 * G3
private val xStart0 = intArrayOf(0, 0, 0, 0)
private val yStart0 = intArrayOf(0, 0, 0, 0)
private var _left = 0
private var _right = 0
private var _top = 0
private var _bottom = 0
var isSnowOver = false
var isRingsOver = false

@Composable
fun DrawSnow(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if (_timer == null && !isSnowOver) {
            LoopNum = 2
            x_Start = size.width / 10
            edge = (size.width / 30).toInt()
            if (picString[1] == "") {
                createString()
            }
        } else {
            LoopNum = 3
            x_Start = 1f
            edge = (size.width / 51).toInt()
            if (drawIndex < 5) {
                picString[0] = ""
                picString[1] = ""
                picString[2] = ""
                picString[3] = ""
                createString()
            }
        }
        y_Start = size.height / 2
        edge2 = edge / 2
        edge32 = edge2 * G3
        val size: Int = if (_timer == null) picString[LoopNum - 1].length else drawIndex
        fun drawMyLine(dType: Int) {
            var dX = 0
            var dY = 0
            when (dType) {
                0 -> {
                    dX = 0
                    dY = -edge
                }

                1 -> {
                    dX = edge32.toInt()
                    dY = -edge2
                }

                2 -> {
                    dX = edge32.toInt()
                    dY = edge2
                }

                3 -> {
                    dX = 0
                    dY = edge
                }

                4 -> {
                    dX = -edge32.toInt()
                    dY = edge2
                }

                5 -> {
                    dX = -edge32.toInt()
                    dY = -edge2
                }
            }
            if (xStart0[LoopNum - 1] == 0 && yStart0[LoopNum - 1] == 0) {
                if (x_Start < _left) _left = x_Start.toInt()
                if (x_Start > _right) _right = x_Start.toInt()
                if (y_Start < _top) _top = y_Start.toInt()
                if (y_Start > _bottom) _bottom = y_Start.toInt()
            }
            drawLine(
                start = Offset(x = x_Start, y = y_Start),
                end = Offset(x = x_Start + dX, y = y_Start + dY),
                color = Color.LightGray
            )
            x_Start += dX
            y_Start += dY
            if (xStart0[LoopNum - 1] == 0 && yStart0[LoopNum - 1] == 0) {
                if (x_Start < _left) _left = x_Start.toInt()
                if (x_Start > _right) _right = x_Start.toInt()
                if (y_Start < _top) _top = y_Start.toInt()
                if (y_Start > _bottom) _bottom = y_Start.toInt()
            }
        }
        for (i in 0 until size) {
            drawMyLine(picString[LoopNum - 1].substring(i, i + 1).toInt())
        }
        if (_timer != null && size == picString[LoopNum - 1].length) {
            stopDrawTimer()
        }
    }
}

fun createString() {
    val isDrawType1 = booleanArrayOf(true, false, false, true, true, true, false)
    val startDirType1 = intArrayOf(0, 0, -2, -2, 0, 0, 2)
    getUnitString(LoopNum, isDrawType1, startDirType1, 0)
}

fun getUnitString(n: Int, is3Center: BooleanArray, startDir: IntArray, dirChange: Int) {
    val isDrawType1 = booleanArrayOf(true, false, false, true, true, true, false)
    val isDrawType2 = booleanArrayOf(true, false, false, false, true, true, false)
    val startDirType1 = intArrayOf(0, 0, -2, -2, 0, 0, 2)
    val startDirType2 = intArrayOf(0, 0, 0, -2, 2, -2, 0)
    var is3CenterSmall: BooleanArray
    var startDirSmall: IntArray
    if (n > 1) {
        for (j in 0..6) {
            if (is3Center[j]) {
                startDirSmall = cloneInt(startDirType1)
                is3CenterSmall = cloneBoolean(isDrawType1)
            } else {
                startDirSmall = cloneInt(startDirType2)
                is3CenterSmall = cloneBoolean(isDrawType2)
            }
            startDir[j] = getNewDir(startDir[j], dirChange).toInt()
            getUnitString(n - 1, is3CenterSmall, startDirSmall, startDir[j])
        }
    } else {
        for (i in 0..6) {
            startDir[i] = getNewDir(startDir[i], dirChange).toInt()
            picString[LoopNum - 1] += drawUnit(startDirFirst + startDir[i], is3Center[i])
        }
    }
}

fun cloneInt(list: IntArray): IntArray {
    val ret = IntArray(list.size)
    for (i in list.indices) {
        ret[i] = list[i]
    }
    return ret
}

fun cloneBoolean(list: BooleanArray): BooleanArray {
    val ret = BooleanArray(list.size)
    for (i in list.indices) {
        ret[i] = list[i]
    }
    return ret
}

fun getNewDir(iDir0: Int, dD: Int): String {
    var iDir = iDir0
    iDir += dD
    if (iDir < 0) {
        iDir += 6
    }
    if (iDir > 5) {
        iDir -= 6
    }
    return iDir.toString()
}

private fun drawUnit(iDir: Int, type: Boolean): String {
    var ret = ""
    val startDir1 = intArrayOf(1, 0, -2, -1, 1, 1, 2)
    val startDir2 = intArrayOf(1, 0, 0, -2, -3, -1, 0)
    for (i in startDir1.indices) {
        ret += if (type) {
            getNewDir(iDir, startDir1[i])
        } else {
            getNewDir(iDir, startDir2[i])
        }
    }
    return ret
}
