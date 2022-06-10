package com.bangkit.roomah.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.bangkit.roomah.databinding.ActivitySplashBinding
import com.bangkit.roomah.ui.camera.CameraActivity
import com.bangkit.roomah.ui.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isOnBoardingFinished()) {
                Intent(
                    this@SplashActivity,
                    CameraActivity::class.java
                ).also { intent ->
                    startActivity(intent)
                    finish()
                }
                Log.d(TAG, "Go To Camera")
            } else {
                Intent(
                    this@SplashActivity,
                    OnboardingActivity::class.java
                ).also { intent ->
                    startActivity(intent)
                    finish()
                }
                Log.d(TAG, "Go To OnBoarding")
            }
        }, SPLASH_DELAY)
    }

    private fun isOnBoardingFinished() : Boolean =
        getSharedPreferences(
            "onBoarding",
            Context.MODE_PRIVATE
        ).getBoolean("Finished",false)

    companion object {
        private const val TAG = "SPLASH DECISION"
        private const val SPLASH_DELAY: Long = 3000
    }
}