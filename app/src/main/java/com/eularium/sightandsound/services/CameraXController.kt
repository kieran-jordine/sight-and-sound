package com.eularium.sightandsound.services

import android.content.Context
import android.os.Environment
import android.util.Size
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.io.File
import java.util.concurrent.Executors

class CameraXController(val context: Context, val previewView: PreviewView, backCamera:Boolean = true, val analyzer: ImageAnalysis.Analyzer? = null) :
    LifecycleObserver {

    private val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imagePreview: Preview
    private lateinit var imageCapture: ImageCapture
    private var imageAnalysis: ImageAnalysis = ImageAnalysis.Builder().build()
    private var analysisExecutor = Executors.newSingleThreadExecutor()
    private var cameraSelector: CameraSelector = if (backCamera) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA
    private lateinit var camera: Camera
    private var previewWidth: Int = 0
    private var previewHeight: Int = 0

    init {
        val runnable = Runnable {
            cameraProvider = cameraProviderFuture.get()
            //Toast.makeText(context, "".plus(previewView.width).plus(", ").plus(previewView.height), Toast.LENGTH_SHORT).show()
            previewWidth = previewView.width
            previewHeight = previewView.height
            buildImagePreview()
            buildImageAnalysis()
            analyzer?.let {
                imageAnalysis.setAnalyzer(analysisExecutor, it)
            }
            buildImageCapture()

            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector,
                    imagePreview, imageAnalysis, imageCapture)
        }
        cameraProviderFuture.addListener(runnable, ContextCompat.getMainExecutor(context))
    }

    fun setImageAnalyzer(analyzer: ImageAnalysis.Analyzer) {
        if (!cameraProvider.isBound(imageAnalysis)) {
            cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector, imageAnalysis)
        }
        imageAnalysis.clearAnalyzer()
        imageAnalysis.setAnalyzer(analysisExecutor, analyzer)
    }

    fun takePicture() {
        //externalMediaDirs.firstOrNull()?
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).let {
            val file = File(it, "".plus(System.currentTimeMillis()).plus(".jpg"))
            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
            val imageSavedCallback = object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(context, exc.localizedMessage, Toast.LENGTH_LONG).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    //val savedUri = Uri.fromFile(file)
                    Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show()
                }
            }
            imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), imageSavedCallback)
        }
    }

    fun takePicture(callback: ImageCapture.OnImageCapturedCallback) {
        imageCapture.takePicture(ContextCompat.getMainExecutor(context), callback)
    }

    fun takePicture(options: ImageCapture.OutputFileOptions, callback: ImageCapture.OnImageSavedCallback) {
        imageCapture.takePicture(options, ContextCompat.getMainExecutor(context), callback)
    }

    fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        cameraProvider.unbindAll()
        camera = cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector,
                imagePreview, imageAnalysis, imageCapture)
    }

    fun switchOrientation() {
        val runnable = Runnable {
            cameraProvider = cameraProviderFuture.get()
            previewWidth = previewView.height
            previewHeight = previewView.width
            buildImagePreview()

            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector,
                    imagePreview, imageAnalysis, imageCapture)
        }
        cameraProviderFuture.addListener(runnable, ContextCompat.getMainExecutor(context))
    }

    private fun buildImagePreview() {
        imagePreview = Preview.Builder()
            //.setTargetResolution(Size(previewView.width, previewView.height))
            .setTargetResolution(Size(previewWidth, previewHeight))
            .build()
        imagePreview.setSurfaceProvider(previewView.surfaceProvider)
    }

    private fun buildImageAnalysis() {
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(previewView.width, previewView.height))
            .build()
        //imageAnalysis.target
    }

    private fun buildImageCapture() {
        imageCapture = ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(previewView.rotation.toInt()) //??
            //.setTargetResolution(Size(previewView.width, previewView.height))
            .build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        analysisExecutor.shutdown()
    }


}