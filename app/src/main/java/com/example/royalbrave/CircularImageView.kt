package com.example.royalbrave

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircularImageView : AppCompatImageView {
    private val paint: Paint = Paint()
    private val borderPaint: Paint = Paint()
    private val borderThickness: Float = 4f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        paint.isAntiAlias = true
        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.strokeWidth = borderThickness
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return

        val radius = width / 2f

        val bitmap = drawableToBitmap(drawable)
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

        // Dibuja la imagen circular
        canvas.drawRoundRect(rect, radius, radius, paint)

        // Dibuja el borde alrededor de la imagen circular
        canvas.drawRoundRect(rect, radius, radius, borderPaint)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
