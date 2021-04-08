package com.eularium.camerax.vizion

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

typealias BarcodeListener = (code: List<Barcode>) -> Unit

class BarcodeAnalyzer(private val listener: BarcodeListener) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
    private val scanner = BarcodeScanning.getClient(options)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            Log.i("CODE-Rotation", image.imageInfo.rotationDegrees.toString())
            val visionImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            scanner.process(visionImage)
                .addOnSuccessListener {
                    listener(it)
                }
                .addOnFailureListener {

                }
                .addOnCompleteListener { image.close() }
        }
    }


}