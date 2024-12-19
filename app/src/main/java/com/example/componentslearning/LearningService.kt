package com.example.componentslearning

import android.widget.Toast

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class LearningService: Service() {

    var musicPlayer: MediaPlayer? =null
    //optional method to bind to another component.
    //if this isn't desired, simply return null
    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    //creating resources to be used in the service
    override fun onCreate() {
        super.onCreate()
        musicPlayer= MediaPlayer.create(this, R.raw.learning_sound)
        musicPlayer?.isLooping =true
    }

    //start and maintain the service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //START_STICKY means the service will run even if the application isn't in
        //the foreground (i.e. the music will keep playing)
        Toast.makeText(this,"Music service started.",Toast.LENGTH_LONG).show()
        musicPlayer?.start()
        return START_STICKY
    }

    //destroy the service and the reference to it
    override fun onDestroy() {
        super.onDestroy()
        musicPlayer?.stop()
        Toast.makeText(this,"Music service destroyed.",Toast.LENGTH_LONG).show()
    }
}