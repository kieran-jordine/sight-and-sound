package com.eularium.sightandsound.sight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eularium.sightandsound.databinding.ActivitySightBinding

class FaceDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySightBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySightBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}