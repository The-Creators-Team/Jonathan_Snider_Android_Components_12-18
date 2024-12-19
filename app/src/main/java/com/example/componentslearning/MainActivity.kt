package com.example.componentslearning

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.componentslearning.databinding.ActivityMainBinding


const val TAG = "Main Activity"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    //private var cursorAdapter: SimpleCursorAdapter? = null
    val contactList= mutableListOf<ContactModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        startLearningService()
        startLearningReceiver()
        startLearningContentProvider()

        binding.recyclerViewContacts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ContactAdapter(contactList)
        }

        Log.d(TAG, "OnCreate in MainActivity")
    }

    private fun startLearningService() {
        binding.musicButton.setOnClickListener {
            //if the music is playing:
            if (isMyServiceRunning(LearningService::class.java)) {
                binding.musicButton.text = getString(R.string.play_music)
                stopService(
                    Intent(this, LearningService::class.java)
                )
            } else {
                binding.musicButton.text = getString(R.string.stop_music)
                startService(
                    Intent(this, LearningService::class.java)
                )
            }
        }
    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == serviceClass.name }
    }


    private fun startLearningReceiver() {
        //Intent filter holds data that needs to be sent to receivers
        val filter = IntentFilter()
        filter.addAction(BroadcastReference.BROADCAST_ITEM)
        //registering the receiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(LearningBroadcast(), filter, RECEIVER_NOT_EXPORTED)
        }

        sendBroadcast(Intent(BroadcastReference.BROADCAST_ITEM))
    }


    private fun startLearningContentProvider() {
        getContacts()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "OnStart in MainActivity")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OnResume in MainActivity")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "OnPause in MainActivity")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "OnPause in MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        //this log may not activate due to the on destroy being called above
        Log.d(TAG, "OnDestroy in MainActivity")
    }

    fun getContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 0)

        }

        println("IN CONTACTS BACKUP")
        // create cursor and query the data
        val contentResolver = getContentResolver()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            println("TOTAL NUMBER OF CONTACTS FROM CURSOR: ${cursor.count}")
            val nameColumn = cursor.getColumnIndex(Contacts.DISPLAY_NAME)
            val numberColumn =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (cursor.moveToNext()) {
                contactList.add(ContactModel(cursor.getString(nameColumn),cursor.getString(numberColumn)))
            }
        }
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    fun getContacts() {
        println("I AM IN GETCONTACTS")
        val contentResolver = getContentResolver()
        val projections = arrayOf(
            Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER

        )
        contentResolver.query(
            ContactsContract.Contacts.CONTENT_LOOKUP_URI,
            projections, //similar to an SQL call, projection indicates what columns I'm interested in receiving
            null, //basically the WHERE of the query. If I wanted to filter some entries from the list, I'd
            //add them here
            null //sort order can be inserted here if desired
        )?.use { cursor ->
            val nameColumn = cursor.getColumnIndex(Contacts.DISPLAY_NAME)
            val numberColumn =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (cursor.moveToNext()) {
                println("NAME: ${cursor.getString(nameColumn)}")
                println("NUMBER: ${cursor.getString(numberColumn)}")

            }

        }
    }*/
}
