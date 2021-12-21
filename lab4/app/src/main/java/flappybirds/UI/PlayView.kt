package com.example.flappybirds.UI

import android.content.Context
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.flappybirds.Thread.PlayThread

class PlayView(context: Context?): SurfaceView(context), SurfaceHolder.Callback {

    private var playThread: PlayThread? = null


    init{
        val holder = holder
        holder.addCallback(this)
        isFocusable = true
        playThread = context?.let { PlayThread(it, holder, resources) }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if(playThread!!.isRunning){
            playThread!!.isRunning = false
            val isCheck = true
            while(isCheck){
                try{
                    playThread!!.join()
                }catch (e: InterruptedException){

                }
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if(!playThread!!.isRunning){
            playThread = holder.let {
                PlayThread(context, it!!, resources) }
        }else{
            playThread!!.start()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val ev = event!!.action
        if(ev == MotionEvent.ACTION_DOWN){
            playThread!!.jump()
        }
        return true
    }
}