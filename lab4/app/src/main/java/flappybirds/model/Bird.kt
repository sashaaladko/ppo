package com.example.flappybirds.Model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.number.Scale
import com.example.flappybirds.MainActivity
import com.example.flappybirds.R

class Bird(res: Resources) {
    var x = 0
        get() = field
        set(value) {
            field = value
        }
    var y = 0
        get() = field
        set(value) {
            field = value
        }
    private var currentFrame = 0
    private var birdList = arrayListOf<Bitmap>()
    private val width = 400
    private val offset = 350

    init{
        for(i in 1 until 17 ){
            val birdID = "ug$i"
            val resID: Int = res.getIdentifier(birdID, "drawable", MainActivity.PACKAGE_NAME)
            birdList.add(Scale(BitmapFactory.decodeResource(res, resID)))
        }

        x = ScreenSize.SCREEN_WIDTH/2 - birdList[0].width/2 - offset
        y = ScreenSize.SCREEN_HEIGHT/2 - birdList[0].height/2
    }

    private fun Scale(bitmap: Bitmap): Bitmap {
        val aspectRatio= bitmap.width.toFloat() /
                bitmap.height.toFloat()
        val height = Math.round(width / aspectRatio)
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    fun getBird(): Bitmap{
        var res = tryGetBird()
        currentFrame = ( currentFrame + 1) % birdList.size
        return res
    }

    fun tryGetBird(): Bitmap{
        return birdList[currentFrame]
    }
}