package com.example.flappybirds.Model

import android.graphics.Bitmap
import android.os.Handler
import com.example.flappybirds.R
import java.lang.reflect.Field

class BackgroundImage() {

    private var backgrounds = arrayListOf<Int>()
    private var index = 0

    var x : Int = 0
        get() = field
        set(value){
            field = value
        }
    var y : Int = 0
        get() = field
        set(value){
            field = value
        }

    init {
        backgrounds.add(R.drawable.background)
        backgrounds.add(R.drawable.background1)
        backgrounds.add(R.drawable.background2)
        backgrounds.add(R.drawable.background3)

    }

    fun changeBackGround(){
        index = (index + 1) % backgrounds.size
    }

    fun getBackground(): Int{
        return backgrounds[index]
    }

    fun getNextBackground(): Int {
        return backgrounds[(index + 1) % backgrounds.size]
    }
}