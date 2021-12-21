package com.example.flappybirds.Thread

import android.content.res.Resources
import android.graphics.*
import android.view.SurfaceHolder
import DatabaseHandler
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.flappybirds.Model.*
import com.example.flappybirds.ScopesActivity
import java.lang.Exception


class PlayThread : Thread {
    private val context: Context
    private var holder: SurfaceHolder
    private var resoucres: Resources
    var isRunning: Boolean = false
        get() = field
        set(value){
            field = value
        }
    private val FPS : Int = (1000.0/60.0).toInt()
    private val backgroundImage = BackgroundImage()
    private var bitmapImage: Bitmap
    private var startTime: Long = 0
    private var frameTime : Long = 0
    private val velocity = 3
    private var bird: Bird
    private var state: State = State.STOP
    private var velocityBird = 0

    private var cotManager: CotManager

    private var score = 0

    enum class State{
        STOP, RUNNING, GAMEOVER;
    }

    constructor(context: Context, holder: SurfaceHolder, resources: Resources){
        this.context = context
        this.holder = holder
        this.resoucres = resources
        isRunning = true
        bird = Bird(resources)

        cotManager = CotManager(resources)

        bitmapImage = BitmapFactory.decodeResource(resoucres, backgroundImage.getBackground())
        bitmapImage = ScaleResize(bitmapImage)
    }

    override fun run() {
        while(isRunning){
            if(holder == null) return
            startTime = System.nanoTime()
            val canvas = holder.lockCanvas()
            if(canvas != null){
                try{
                    synchronized(holder){
                        render(canvas)
                        renderBird(canvas)
                        renderCot(canvas)
//                        renderColisionLine(canvas)
                        CheckColision()
                        renderScore(canvas)
                    }
                }finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }

            frameTime = (System.nanoTime() - startTime)/1000000
            if(frameTime < FPS){
                try{
                    Thread.sleep(FPS - frameTime)
                }catch (e: InterruptedException){
                }
            }
            if(state == State.GAMEOVER){
                isRunning = false
            }
        }
    }

    private fun Show() {

        var intent = Intent(context, ScopesActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra("score", score)
        context.startActivity(intent)
    }

    private fun renderColisionLine(canvas: Canvas) {


        bird.let {
            drawCollision(canvas,
                it.x.toFloat() + 70,
                it.y.toFloat() + 80,
                bird.tryGetBird().width.toFloat() - 170,
                bird.tryGetBird().height.toFloat() - 120)
        }

        var cots = cotManager.getCots()
        cots.forEach {
            drawCollision(canvas,
                it.x.toFloat() + 350f,
                it.y.toFloat() - it.between.toFloat(),
                cotManager.image.width.toFloat() - 510f,
                cotManager.image.height.toFloat() - 300f)

            drawCollision(canvas,
                it.x.toFloat() + 350f,
                it.y.toFloat() + it.between.toFloat() + 300f,
                cotManager.image.width.toFloat() - 510f,
                cotManager.image.height.toFloat())
        }
    }

    fun drawCollision(canvas: Canvas?, x: Float, y: Float, w: Float, h: Float){
        var paint = Paint()
        paint.setColor(Color.RED)
        paint.strokeWidth = 10f;
        canvas!!.drawLine(x, y, x + w, y, paint)
        canvas!!.drawLine(x + w, y, x + w, y + h, paint)
        canvas!!.drawLine(x + w, y + h, x, y + h, paint)
        canvas!!.drawLine(x, y + h, x, y, paint)
    }

    private fun isColision(
        bx: Float, by: Float, bw: Float, bh: Float,
        cx: Float, cy: Float, cw: Float, ch: Float,
    ): Boolean{
        return bx < cx + cw &&
                bx + bw > cx &&
                by < cy + ch &&
                bh + by > cy
    }

    private fun CheckColision() {
        var cots = cotManager.getCots()

        val birdx = bird.x.toFloat() + 70
        val birdy = bird.y.toFloat() + 80
        val birdw = bird.tryGetBird().width.toFloat() - 170
        val birdh = bird.tryGetBird().height.toFloat() - 120

        cots.forEach {

            val upcotx = it.x.toFloat() + 350f
            val upcoty = it.y.toFloat() - it.between.toFloat()
            val upcotw = cotManager.image.width.toFloat() - 510f
            val upcoth = cotManager.image.height.toFloat() - 300f

            val downcotx = it.x.toFloat() + 350f
            val downcoty = it.y.toFloat() + it.between.toFloat() + 300f
            val downcotw = cotManager.image.width.toFloat() - 510f
            val downcoth = cotManager.image.height.toFloat()

            if (isColision(birdx,birdy,birdw,birdh,
                upcotx,upcoty,upcotw,upcoth) ||
                isColision(birdx,birdy,birdw,birdh,
                    downcotx,downcoty,downcotw,downcoth)) {
                state = State.GAMEOVER
                restart()
                return
            }

            if(!it.isChecked && birdx > upcotx + upcotw)
            {
                score += 1
                it.isChecked = true
            }
        }
    }

    private fun restart() {
        Show()
    }

    private fun renderBird(canvas: Canvas?) {
        if(state == State.RUNNING)
            if(bird.y < ScreenSize.SCREEN_HEIGHT - bird.tryGetBird().height || velocityBird < 0) {
                velocityBird += 2
                bird.y += velocityBird
            }


        canvas!!.drawBitmap(bird.getBird(), bird.x.toFloat(), bird.y.toFloat(), null)
    }

    private fun render(canvas: Canvas?) {
        draw(canvas, getBitmap(), null)
    }

    private fun renderCot(canvas:Canvas?){

        if(state == State.RUNNING)

        cotManager.moveAll(velocity)
        var list = cotManager.getCots()

        list.forEach{

            canvas!!.drawBitmap(cotManager.rotateImage, it.x.toFloat(), it.y.toFloat() - it.between.toFloat(), null)


            canvas!!.drawBitmap(cotManager.image, it.x.toFloat() + 180f, it.y.toFloat() + it.between.toFloat(), null)

        }

    }

    private fun getBitmap() : Bitmap{
        backgroundImage.x -= velocity
        if(backgroundImage.x < -bitmapImage.width){
            backgroundImage.x = backgroundImage.x % bitmapImage.width
        }
        return bitmapImage
    }

    private fun renderScore(canvas: Canvas?){
        val p = Paint()
        p.textSize = 80f
        p.color = Color.BLACK
        canvas!!.drawText(score.toString(), 50f, 250f, p)
    }

    private fun draw(canvas: Canvas?, bitmapImage: Bitmap, paint: Paint?) {
        canvas!!.drawBitmap(bitmapImage, backgroundImage.x.toFloat(), backgroundImage.y.toFloat(), paint)
        if(backgroundImage.x < -bitmapImage.width + ScreenSize.SCREEN_WIDTH){
            canvas!!.drawBitmap(bitmapImage, backgroundImage.x + bitmapImage.width.toFloat(), backgroundImage.y.toFloat(), paint)
        }
    }

    private fun ScaleResize(bitmap: Bitmap): Bitmap {
        var ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val scaleWidth = (ratio + ScreenSize.SCREEN_HEIGHT).toInt()
        return Bitmap.createScaledBitmap(bitmap, scaleWidth, ScreenSize.SCREEN_HEIGHT, false)
    }

    fun jump() {
        state = State.RUNNING
        if(bird.y > 0)
            velocityBird = - 40
    }
}