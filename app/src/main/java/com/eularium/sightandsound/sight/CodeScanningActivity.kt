package com.eularium.sightandsound.sight

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import com.eularium.sightandsound.databinding.ActivitySightBinding
import com.eularium.sightandsound.overlay.GraphicOverlay
import com.eularium.sightandsound.overlay.GraphicRect
import com.eularium.sightandsound.services.CameraXController
import com.eularium.sightandsound.sight.analyzers.CodeScanningAnalyzer

class CodeScanningActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySightBinding

    private var isFullScreen = true
    private lateinit var controller: CameraXController
    private lateinit var previewView: PreviewView
    private lateinit var graphicOverlay: GraphicOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySightBinding.inflate(layoutInflater)
        setContentView(binding.root)
        previewView = binding.previewView
        graphicOverlay = binding.graphicOverlay
        graphicOverlay.setOnClickListener { toggleFullScreen() }
        graphicOverlay.setOnLongClickListener {
            controller.switchCamera()
            true
        }
        controller = CameraXController(this, previewView, true, CodeScanningAnalyzer { codes ->
            graphicOverlay.clear()
            graphicOverlay.addAll(codes.map { GraphicRect(graphicOverlay, it.boundingBox) })
        })
        lifecycle.addObserver(controller)
        fullScreenOn()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        controller.switchOrientation()
    }

    private fun fullScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                        //immersive, lean back, stick
                        View.SYSTEM_UI_FLAG_IMMERSIVE or
                        //content adjust when bars show/hide
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        //hide nav bar and status bar
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }

    private fun fullScreenOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }

    private fun toggleFullScreen() {
        if (isFullScreen) {
            fullScreenOff()
        } else {
            fullScreenOn()
        }
        isFullScreen = isFullScreen.not()
    }

}