package com.eularium.sightandsound.overlay

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class GraphicOverlay(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val lock: Any = Any()
    private val overlays: MutableList<Graphic> = mutableListOf()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(lock) {
            for (overlay in overlays) {
                overlay.draw(canvas)
            }
        }
    }

    fun add(overlay: Graphic) {
        synchronized(lock) { overlays.add(overlay) }
        postInvalidate()
    }

    fun addAll(overlay: List<Graphic>) {
        synchronized(lock) { overlays.addAll(overlay) }
        postInvalidate()
    }

    fun clear() {
        synchronized(lock) { overlays.clear() }
        postInvalidate()
    }
}