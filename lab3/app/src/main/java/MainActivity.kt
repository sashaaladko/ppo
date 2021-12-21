package id.ac.poltek_kediri.informatika.appx09

import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.MediaController
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.progress?.let { mediaPlayer.seekTo(it) }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
         }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnPlay ->{
                audioPlay(posMus)
            }
            R.id.btnNext ->{
                audioNext()
            }
            R.id.btnPrev ->{
                audioPrev()
            }
            R.id.btnStop ->{
                audioStop()
            }
        }
    }

    val arrMusic = intArrayOf(R.raw.music_1, R.raw.music_2, R.raw.music_3)
    val arrVideo = intArrayOf(R.raw.vid_1, R.raw.vid_2, R.raw.vid_3)
    val arrCover = intArrayOf(R.drawable.cover_1, R.drawable.cover_2, R.drawable.cover_3)
    val arrSongs = arrayOf("Frank Sinatra - let it snow", "Green day - american idiot", "Mozart - requiem")

    var posMus = 0
    var posVid = 0
    var handler = Handler()
    lateinit var mediaPlayer : MediaPlayer
    lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaController = MediaController(this)
        mediaPlayer = MediaPlayer()
        seekSong.max=100
        seekSong.progress = 0
        seekSong.setOnSeekBarChangeListener(this)
        btnNext.setOnClickListener(this)
        btnPrev.setOnClickListener(this)
        btnStop.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        mediaController.setPrevNextListeners(nextVid,prevVid)
        mediaController.setAnchorView(videoView2)
        videoView2.setMediaController(mediaController)
        videoSet(posVid)
    }

    var nextVid = View.OnClickListener { v:View ->
        if(posVid<(arrVideo.size-1)) posVid++
        else posVid = 0
        videoSet(posVid)
    }

    var prevVid = View.OnClickListener { v:View ->
        if(posVid>0) posVid--
        else posVid = arrVideo.size-1
        videoSet(posVid)
    }

    fun videoSet(pos : Int){
        videoView2.setVideoURI(Uri.parse("android.resource://"+packageName+"/"+arrVideo[pos]))
    }

    fun milliSecondToString(ms : Int):String{
        var detik = TimeUnit.MILLISECONDS.toSeconds(ms.toLong())
        val menit = TimeUnit.SECONDS.toMinutes(detik)
        detik = detik % 60
        return "$menit : $detik"
    }

    fun audioPlay(pos : Int){
        mediaPlayer = MediaPlayer.create(this, arrMusic[pos])
        seekSong.max = mediaPlayer.duration
        txMaxTime.setText(milliSecondToString(seekSong.max))
        txCrTime.setText(milliSecondToString(mediaPlayer.currentPosition))
        seekSong.progress = mediaPlayer.currentPosition
        imV.setImageResource(arrCover[pos])
        txPlayer.setText(arrSongs[pos])
        mediaPlayer.start()
        var updateSeekBarThread = UpdateSeekBarProgressThread()
        handler.postDelayed(updateSeekBarThread,50)
    }

    fun audioNext(){
        if(mediaPlayer.isPlaying)mediaPlayer.stop()
        if(posMus<(arrMusic.size-1)){
            posMus++
        }else{
            posMus = 0
        }
        audioPlay(posMus)
    }

    fun audioPrev(){
        if(mediaPlayer.isPlaying) mediaPlayer.stop()
        if(posMus>0){
            posMus--
        }else{
            posMus = arrMusic.size-1
        }
        audioPlay(posMus)
    }

    fun audioStop(){
        if(mediaPlayer.isPlaying) mediaPlayer.stop()
    }

    inner class UpdateSeekBarProgressThread : Runnable{
        override fun run() {
            var currTime = mediaPlayer.currentPosition
            txCrTime.setText(milliSecondToString(currTime))
            seekSong.progress=currTime
            if(currTime != mediaPlayer.duration) handler.postDelayed(this,50)
        }
    }

}
