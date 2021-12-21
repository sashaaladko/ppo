package com.example.flappybirds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.example.flappybirds.Model.ScreenSize
import com.example.flappybirds.UI.PlayView

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var PACKAGE_NAME: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ScreenSize.getScreenSize(this)
        PACKAGE_NAME = applicationContext.packageName
        val playView = PlayView(this)
        setContentView(playView)

    }
}