package com.eularium.sightandsound.sight

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import com.eularium.sightandsound.databinding.ActivitySightBinding
import com.eularium.sightandsound.services.CameraXController

class CodeScanningActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySightBinding

    private lateinit var controller: CameraXController
    private lateinit var previewView: PreviewView
    private var isFullScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySightBinding.inflate(layoutInflater)
        setContentView(binding.root)
        previewView = binding.previewView
        controller = CameraXController(this, previewView)
        previewView.setOnClickListener { toggleFullScreen() }
        previewView.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                controller.switchCamera()
                return true
            }
        })
        Toast.makeText(this, "on create", Toast.LENGTH_SHORT).show()
        fullScreenOn()
    }

    override fun onResume() {
        super.onResume()
        controller.initialize()
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