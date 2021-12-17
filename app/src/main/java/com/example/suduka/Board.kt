package com.example.suduka

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.roundToInt

class Board constructor(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint()
    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.textSize = 100f
    }
    private var numbers = listOf<List<Int>>()

    val height_size : Int
        get() = height / numbers.size

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var liney = 0f
        var linex = 0f
        for(i in numbers.indices) {
            for(z in numbers[i]) {
                val y : Float = i * height_size.toFloat()
                val x : Float = numbers[i].indexOf(z) * width.toFloat() / 5f
                println("$x $y")
                canvas.drawText(z.toString(), x, y, paint)
            }
        }
        for(i in 0..3) {
            liney += height / numbers.size
            canvas.drawLine(0f, liney, width.toFloat(), liney, paint)
        }
        for(i in 0..3) {
            linex += width / numbers[1].size
            canvas.drawLine(linex, 0f, linex, height.toFloat(), paint)
        }
    }
    fun drawNumber(number : List<List<Int>>) {
        numbers = number
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnClickPlaceListener(listener : (x : Int, y : Int) -> Unit) {
        setOnTouchListener { _, event ->
            listener(
                (event.x / (width * 5)).roundToInt(),
                event.y.toInt() / height_size
            )
            true
        }
    }
}