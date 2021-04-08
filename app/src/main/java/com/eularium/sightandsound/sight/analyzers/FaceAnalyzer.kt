package com.eularium.camerax.vizion

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

typealias FaceListener = (faces: List<Face>) -> Unit

class FaceAnalyzer(private val listener: FaceListener) : ImageAnalysis.Analyzer {

    /*val highAccuracyOptions = FaceDetectorOptions.Builder()
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .build()*/
    val realtimeOptions = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()
    private val detector = FaceDetection.getClient()

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            Log.i("FaceAnalyzer", "width: ".plus(image.width).plus(", height:").plus(image.height))
            val visionImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            /*val visionImage = InputImage.fromByteBuffer(
                    image.planes[0].buffer,
                    image.width, image.height,
                    image.imageInfo.rotationDegrees,
                    image.format)*/
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