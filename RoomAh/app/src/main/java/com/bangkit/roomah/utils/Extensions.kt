package com.bangkit.roomah.utils

import android.animation.ObjectAnimator
import android.view.View

/**
 * Animate visibility by transforming alpha value of a view
 *
 * @param isVisible View visibility
 * @param duration Animation duration, default 400
 */
fun View.animateVisibility(isVisible: Boolean, duration: Long = 400) {
    ObjectAnimator
        .ofFloat(this, View.ALPHA, if (isVisible) 1f else 0f)
        .setDuration(duration)
        .start()
}