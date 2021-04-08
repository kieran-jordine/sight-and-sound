package com.eularium.sightandsound.overlay

import android.graphics.*

abstract class Graphic(protected val overlay: GraphicOverlay) {

    abstract fun draw(canvas: Canvas)

    companion object {
        val colors = arrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED, Color.WHITE, Color.YELLOW)
    }

    fun color(): Int {
        return colors[(0..6).random()]
    }

    fun postInvalidate() {
        overlay.postInvalidate()
    }

}

class GraphicRect : Graphic {

    private var rect: Rect? = null
    private val paint = Paint().apply {
        isAntiAlias = true
        color = color()
        style = Paint.Style.STROKE
        strokeWidth = 2F
    }

    constructor(overlay: GraphicOverlay, rect: Rect?): super(overlay) {
        rect?.let {
            this.rect = rect //Rect(rect.left, rect.top, rect.right, rect.bottom)
        }
    }

    constructor(overlay: GraphicOverlay, rectF: RectF): super(overlay) {
        this.rect = Rect().apply {
            left = rectF.left.toInt()
            top = rectF.top.toInt()
            right = rectF.right.toInt()
            bottom = rectF.bottom.toInt()
        }
    }

    constructor(overlay: GraphicOverlay, left:Float, top:Float, right:Float, bottom:Float): super(overlay) {
        this.rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    override fun draw(canvas: Canvas) {
        rect?.let {
            canvas.drawRect(it, paint)
        }
    }

}