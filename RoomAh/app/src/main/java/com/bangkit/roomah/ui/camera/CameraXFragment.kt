package com.bangkit.roomah.ui.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bangkit.roomah.R
import com.bangkit.roomah.databinding.FragmentCameraXBinding
import com.bangkit.roomah.ui.home.HomeActivity
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXFragment : Fragment() {

    private var _binding: FragmentCameraXBinding? = null
    private val binding get() = _binding!!

    private lateinit var safeContext: Context
    private lateinit var cameraExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { permission ->
            Log.d(TAG, "${permission.key} = ${permission.value}")
        }

        if (allPermissionGranted()) {
            startCamera()
        } else {
            Toast.makeText(safeContext, R.string.permission_not_granted, Toast.LENGTH_SHORT).show()
            Intent(safeContext, HomeActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraXBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionGranted()) {
            startCamera()
        } else {
            requestPermissions.launch(REQUIRED_PERMISSIONS)
        }

        // Action
        binding.apply {
            btnSwitch.setOnClickListener {
                cameraSelector =
                    if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    else
                        CameraSelector.DEFAULT_BACK_CAMERA
                startCamera()
            }

            btnCapture.setOnClickListener {
                takePhoto()
            }

            btnBars.setOnClickListener {
                Intent(safeContext, HomeActivity::class.java).also { intent ->
                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    private fun allPermissionGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e(TAG, "startCamera Failure:", e)
            }
        }, ContextCompat.getMainExecutor(safeContext))
    }

    private fun takePhoto() {
        Toast.makeText(safeContext, resources.getString(R.string.captured_image), Toast.LENGTH_SHORT).show()
//        val imageCapture = imageCapture ?: return
//
//        val photoFile = createFile(application)
//
//        val outputOptions = ImageCapture
//            .OutputFileOptions
//            .Builder(photoFile)
//            .build()
//
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(safeContext),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val toValidateFragment = CameraXFragmentDirections
//                        .actionCameraXFragmentToCameraValidateFragment(
//                            photoFile,
//                            cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
//                        )
//                    findNavController().navigate(toValidateFragment)
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Log.e(TAG, "onError: ${exception.message}")
//
//                    Toast.makeText(
//                        safeContext,
//                        resources.getString(R.string.capture_failure),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        )
    }

    companion object {
        private const val TAG = "cameraX"
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}