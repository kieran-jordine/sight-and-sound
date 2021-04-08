package com.eularium.sightandsound

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eularium.sightandsound.databinding.ActivityMainBinding
import com.eularium.sightandsound.sight.CodeScanningActivity
import com.eularium.sightandsound.sight.FaceDetectionActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (permissionsGranted()) {
            enableButtons()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }

        binding.btnFace.setOnClickListener { startActivity(Intent(this, FaceDetectionActivity::class.java))}
        binding.btnCode.setOnClickListener { startActivity(Intent(this, CodeScanningActivity::class.java))}
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (permissionsGranted()) {
                enableButtons()
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun permissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableButtons() {
        binding.btnCode.isEnabled = true
        binding.btnFace.isEnabled = true
        binding.btnText.isEnabled = true
        binding.btnObject.isEnabled = true
        binding.btnLabel.isEnabled = true
        binding.btnNlp.isEnabled = true
    }

}