package com.eularium.sightandsound.sight.analyzers

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

typealias ObjectListener = (objects: List<DetectedObject>) -> Unit

class ObjectDetectionAnalyzer(private val listener: ObjectListener) : ImageAnalysis.Analyzer {

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification()
        .enableMultipleObjects()
        .build()
    private val detector = ObjectDetection.getClient(options)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val visionImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            detector.process(visionImage)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        listener(it.result)

                    }
                    image.close()
                }
        }
    }

}