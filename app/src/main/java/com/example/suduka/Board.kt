package com.example.suduka

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Board constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private val paint = Paint()

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.textSize = 100f
    }

    private var numbers = mutableListOf<MutableList<Int>>()

    private val heightSize: Int
        get() = height / numbers.size
    private val widthSize: Int
        get() = width / numbers.first().size

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (y in numbers.indices) {
            for (x in numbers[y].indices) {
                val y1: Float = y * heightSize.toFloat() + heightSize.toFloat()
                val x1: Float = x * widthSize.toFloat()
                println("$x1 $y1")
                canvas.drawText(numbers[y][x].toString(), x1, y1, paint)
            }
        }
        for (i in 0..numbers.size) {
            canvas.drawLine(
                0f,
                i * heightSize.toFloat(),
                width.toFloat(),
                i * heightSize.toFloat(),
                paint
            )
        }
        for (i in 0..numbers.first().size) {
            canvas.drawLine(
                i * widthSize.toFloat(),
                0f,
                i * widthSize.toFloat(),
                height.toFloat(),
                paint
            )
        }
    }

    fun drawNumber(number: MutableList<MutableList<Int>>) {
        numbers = number
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnClickPlaceListener(listener: (x: Int, y: Int) -> Unit) {
        setOnTouchListener { _, event ->
            listener(
                event.x.toInt() / widthSize,
                event.y.toInt() / heightSize
            )
            true
        }
    }
}