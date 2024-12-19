package com.example.componentslearning

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class LearningBroadcast : BroadcastReceiver() {

    //this is the only relevant lifecycle event for Broadcast Receivers
    //this is called when there are changes or updates made
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BroadcastReference.BROADCAST_ITEM -> {
                Toast.makeText(
                    context,
                    "OnReceive triggered in LearningBroadcast",
                    Toast.LENGTH_LONG
                ).show()
                println("RETRIEVED BROADCAST")
            }

        }
    }
}

object BroadcastReference {
    const val BROADCAST_ITEM = "broadcast Item"
}