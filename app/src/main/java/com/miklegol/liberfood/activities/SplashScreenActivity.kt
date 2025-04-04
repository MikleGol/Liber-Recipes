package com.miklegol.liberfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miklegol.liberfood.R

import android.content.Intent

import android.os.Handler
import android.view.View
import android.view.Window
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val relativeLayout: RelativeLayout = findViewById(R.id.relative_layout)
        val liberAppLogo: ImageView = findViewById(R.id.liber_app_logo)

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = AccelerateInterpolator()
        fadeIn.duration = 2000

        liberAppLogo.startAnimation(fadeIn)
        relativeLayout.startAnimation(fadeIn)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }, 3000)

        val window: Window = window
        val actionBar: ActionBar? = supportActionBar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        actionBar?.hide()
    }
}
