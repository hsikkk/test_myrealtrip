package com.example.test_myrealtrip

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.splash.*

class SplashActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT: Long = 1300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        val versionName = BuildConfig.VERSION_NAME

        text_version.setText("v "+versionName)

        icon_news.setBackground(ShapeDrawable(OvalShape()))
        icon_news.setClipToOutline(true)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
