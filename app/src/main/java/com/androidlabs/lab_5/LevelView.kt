package com.androidlabs.lab_5

import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import android.view.View
import android.graphics.Color
import android.graphics.Paint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class LevelView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var tiltAngle: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val centerLinePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        isAntiAlias = true
    }

    private val horizonLinePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
        isAntiAlias = true
    }

    private val angleTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 80f
        isAntiAlias = true
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        var centerY = height / 2f
        val circleRadius = min(width, height) / 3f

        if (width > height) {
            val margin = 300f
            val angleText = String.format("%.1f°", tiltAngle)

            val rightX = centerX - circleRadius - margin
            val rightY = centerY
            canvas.drawText(angleText, rightX, rightY, angleTextPaint)

            val leftX = centerX + circleRadius + margin
            val leftY = centerY
            canvas.save()
            canvas.rotate(180f, leftX, leftY)
            canvas.drawText(angleText, leftX, leftY, angleTextPaint)
            canvas.restore()
        } else {
            val margin = 200f
            val angleText = String.format("%.1f°", tiltAngle)
            centerY -= 100f
            canvas.drawText(angleText, centerX, centerY - circleRadius - margin, angleTextPaint)
            canvas.save()
            canvas.rotate(180f, centerX, centerY + circleRadius + margin)
            canvas.drawText(angleText, centerX, centerY + circleRadius + margin, angleTextPaint)
            canvas.restore()
        }

        drawCircleBackground(canvas, centerX, centerY, circleRadius)

        canvas.drawLine(centerX - circleRadius, centerY, centerX + circleRadius, centerY, centerLinePaint)
        canvas.save()
        canvas.rotate(-tiltAngle, centerX, centerY)
        canvas.drawLine(centerX - circleRadius, centerY, centerX + circleRadius, centerY, horizonLinePaint)
        canvas.restore()
    }

    private fun drawCircleBackground(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val fillPaint = Paint().apply {
            color = Color.YELLOW
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val borderPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 8f
            isAntiAlias = true
        }

        canvas.drawCircle(centerX, centerY, radius, fillPaint)
        canvas.drawCircle(centerX, centerY, radius, borderPaint)

        val tickLength = 30f

        val tickPaint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 4f
            isAntiAlias = true
        }

        val tickTextPaint = Paint().apply {
            color = Color.BLACK
            textSize = 40f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val textMargin = 10f

        for (angle in 0 until 180 step 30) {
            val rad = Math.toRadians(angle.toDouble())
            val startX = centerX + radius * cos(rad).toFloat()
            val startY = centerY + radius * sin(rad).toFloat()
            val endX = centerX + (radius + tickLength) * cos(rad).toFloat()
            val endY = centerY + (radius + tickLength) * sin(rad).toFloat()

            canvas.drawLine(startX, startY, endX, endY, tickPaint)

            val textX = centerX + (radius + tickLength + textMargin + tickTextPaint.textSize) * cos(rad).toFloat()
            val textY = centerY - (radius + tickLength + textMargin + tickTextPaint.textSize) * sin(rad).toFloat() + tickTextPaint.textSize / 3f

            canvas.drawText("$angle°", textX, textY, tickTextPaint)
        }
        for (angle in 0 downTo -180 step 30) {
            val rad = Math.toRadians(angle.toDouble())
            val startX = centerX + radius * cos(rad).toFloat()
            val startY = centerY + radius * sin(rad).toFloat()
            val endX = centerX + (radius + tickLength) * cos(rad).toFloat()
            val endY = centerY + (radius + tickLength) * sin(rad).toFloat()

            canvas.drawLine(startX, startY, endX, endY, tickPaint)

            val textX = centerX + (radius + tickLength + textMargin + tickTextPaint.textSize) * cos(rad).toFloat()
            val textY = centerY - (radius + tickLength + textMargin + tickTextPaint.textSize) * sin(rad).toFloat() + tickTextPaint.textSize / 3f

            canvas.drawText("$angle°", textX, textY, tickTextPaint)
        }
    }

}