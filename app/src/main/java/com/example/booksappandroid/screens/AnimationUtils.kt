package com.example.booksappandroid.screens

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.BounceInterpolator

fun animateFadeIn(delay: Int, view: View) {
    view.setAlpha(0f)
    view.animate()
        .alpha(1F)
        .setStartDelay(delay.toLong())
        .setDuration(1000)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.alpha = 1f
                view.visibility = View.VISIBLE
            }
        })
}

fun animateTranslation(view: View) {
    val screenHeight = (view.resources.displayMetrics.heightPixels).toFloat()
    view.animate()
        .translationY(screenHeight/4)  // move down 25% of screen height
        .setInterpolator(BounceInterpolator()) //Animation type
        .setDuration(2000)
}
