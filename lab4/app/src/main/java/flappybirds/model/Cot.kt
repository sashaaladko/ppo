package com.example.flappybirds.Model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.example.flappybirds.MainActivity
import com.example.flappybirds.R
import kotlin.random.Random

class Cot {

    var between = Random.nextInt(1200, 1300)
        get() = field

    var x = Random.nextInt(800, 1200)
        get() = field
        set(value) {
            field = value
        }
    var y = Random.nextInt(-500, 500)
        get() = field

    var isChecked = false


}

class CotManager{

    var image: Bitmap
    var rotateImage: Bitmap
    private var cots = arrayListOf<Cot>()

    constructor(res: Resources){
        image = BitmapFactory.decodeResource(res, R.drawable.pngegg)
        val matrix = Matrix()

        matrix.postRotate(180f)

        val scaledBitmap = Bitmap.createScaledBitmap(image, image.width,  image.height, true)

        rotateImage = Bitmap.createBitmap(scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true)
    }

    fun moveAll(delx: Int){

        cots.removeAll{ it.x < -700 }

        cots.forEach {
            it.x -= delx
        }

        if(cots.size == 0 || cots.last().x < 70)
            add()
    }

    fun add(){
        cots.add(RandomCots())
    }

    private fun RandomCots() : Cot{
        return Cot()
    }

    fun getCots(): ArrayList<Cot>{
        return cots
    }

    fun removeAll() {
        cots.clear()
    }


}