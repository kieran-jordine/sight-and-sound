package com.eularium.sightandsound.sight.analyzers

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

typealias ImageLabelListener = (labels: List<ImageLabel>) -> Unit

class ImageLabelAnalyzer(private val listener: ImageLabelListener) : ImageAnalysis.Analyzer {

    private val options = ImageLabelerOptions.Builder()
        .setConfidenceThreshold(0.7F)
        .build()
    private val labeler = ImageLabeling.getClient(options)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val visionImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            labeler.process(visionImage)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        listener(it.result)
                    }
                    image.close()
                }
        }
    }
}