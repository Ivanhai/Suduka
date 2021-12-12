package com.example.suduka

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Board constructor(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint()
    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.textSize = 100f
    }

    private var numbers = listOf<List<Int>>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var liney = 0f
        var linex = 0f
        for(i in numbers.indices) {
            for(z in numbers[i]) {
                val y : Float = (i + 1) * 100f
                val x : Float = numbers[i].indexOf(z) * 110f
                canvas.drawText(z.toString(), x, y, paint)
            }
        }
        for(i in 0..3) {
            liney += height / 5
            canvas.drawLine(0f, liney, width.toFloat(), liney, paint)
        }
        for(i in 0..3) {
            linex += width / 5
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
                (event.x / 25).toInt(),
                (event.y / 25).toInt()
            )
            true
        }
    }
}