package kz.sdk.sandykprot.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kz.sdk.sandykprot.R
import kotlin.math.*

class CustomWheelView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var segments: List<String> = listOf()
    private var angle = 0f
    private var radius = 0f

    init {
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 3f
        borderPaint.color = Color.parseColor("#A47CF3")
    }

    fun setSegments(segments: List<String>) {
        this.segments = segments
        invalidate()
    }

    fun getCurrentAngle(): Float = angle

    fun setAngle(newAngle: Float) {
        angle = newAngle % 360
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        radius = min(centerX, centerY) - 40f

        if (segments.isNotEmpty()) {
            val segmentAngle = 360f / segments.size

            for (i in segments.indices) {
                paint.color = Color.parseColor("#E5E6FB")
//                paint.color = if (i % 2 == 0) Color.parseColor("#8BC34A") else Color.parseColor("#03A9F4")
                val startAngle = angle + i * segmentAngle
                drawSegment(canvas, startAngle, segmentAngle, segments[i])
            }
        }

        drawArrow(canvas, centerX, centerY, radius)
    }

    private fun drawSegment(canvas: Canvas, startAngle: Float, sweepAngle: Float, label: String) {
        val centerX = width / 2f
        val centerY = height / 2f
        val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)

        canvas.drawArc(rectF, startAngle, sweepAngle, true, borderPaint)

        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = radius / 8

        val textAngle = startAngle + sweepAngle / 2
        val textX = (centerX + (radius / 1.5f) * cos(Math.toRadians(textAngle.toDouble()))).toFloat()
        val textY = (centerY + (radius / 1.5f) * sin(Math.toRadians(textAngle.toDouble()))).toFloat()

        canvas.drawText(label, textX, textY, paint)
    }

    private fun drawArrow(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val arrowPath = Path().apply {
            moveTo(centerX + radius, centerY)
            lineTo(centerX + radius + 50, centerY - 30)
            lineTo(centerX + radius + 50, centerY + 30)
            close()
        }

        paint.color = Color.parseColor("#A47CF3")
        paint.style = Paint.Style.FILL

        canvas.drawPath(arrowPath, paint)
    }

    fun getSegmentAtAngle(): Int {
        if (segments.isEmpty()) return -1

        val normalizedAngle = (360 - angle % 360)
        val segmentAngle = 360f / segments.size
        return (normalizedAngle / segmentAngle).toInt() % segments.size
    }
}
