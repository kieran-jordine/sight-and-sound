package com.eularium.camerax.vizion

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions

typealias TextListener = (text: Text) -> Unit

class TextAnalyzer(private val listener: TextListener)  : ImageAnalysis.Analyzer {

    private val options = TextRecognizerOptions.Builder()
            .build()
    private val recognizer = TextRecognition.getClient(options)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val visionImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            recognizer.process(visionImage)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            listener(it.result)
                            /*val text = it.result
                            text.textBlocks.forEach { tb ->
                                tb.lines.forEach { line ->
                                    line.elements.forEach { el ->
                                        el.text
                                    }
                                }
                            }*/
                        }
                        image.close()
                    }
        }
    }

}