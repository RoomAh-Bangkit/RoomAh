package com.bangkit.roomah.ui.onboarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.roomah.R
import com.bangkit.roomah.adapter.ViewPagerAdapter
import com.bangkit.roomah.databinding.ActivityOnboardingBinding
import com.bangkit.roomah.ui.camera.CameraActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.attachTo(viewPager)

        val myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    binding.btnNext.text = resources.getString(R.string.onboarding_move)
                    binding.btnSkip.visibility = View.INVISIBLE
                } else {
                    binding.btnNext.text = resources.getString(R.string.onboarding_next)
                    binding.btnSkip.visibility = View.VISIBLE
                }

                binding.btnNext.setOnClickListener {
                    if (position == 2) {
                        finishOnBoarding()
                    } else {
                        viewPager.currentItem += 1
                    }
                }
            }
        }
        viewPager.registerOnPageChangeCallback(myPageChangeCallback)

        binding.btnSkip.setOnClickListener {
            finishOnBoarding()
        }
    }

    private fun finishOnBoarding() {
        setFinishedOnBoarding()

        Intent(this@OnboardingActivity, CameraActivity::class.java).also { intent ->
            startActivity(intent)
            finish()
        }
    }

    private fun setFinishedOnBoarding() {
        val pref = getSharedPreferences(
            "onBoarding",
            Context.MODE_PRIVATE
        ).edit()

        pref.putBoolean("Finished", true)
        pref.apply()
    }
}
