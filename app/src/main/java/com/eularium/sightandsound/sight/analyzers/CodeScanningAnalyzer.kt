package com.eularium.sightandsound.sight.analyzers

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

typealias CodeScanningListener = (code: List<Barcode>) -> Unit

class CodeScanningAnalyzer(private val listener: CodeScanningListener) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
    private val scanner = BarcodeScanning.getClient(options)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val visionImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            scanner.process(visionImage)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        listener(it.result)
                    }
                    image.close()
                }
        }
    }


}