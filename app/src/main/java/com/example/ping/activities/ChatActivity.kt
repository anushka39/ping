package com.example.ping.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import chatt
import com.example.ping.R
import com.firebase.ui.database.FirebaseListAdapter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDBe = FirebaseDatabase.getInstance().reference
    private val adapter: FirebaseListAdapter<chatt>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        if (FirebaseAuth.getInstance().currentUser != null){
            Toast.makeText(this, "welcome", Toast.LENGTH_LONG).show()

            displayChatMessages()

        }
        fab.setOnClickListener {
            val input = input
            FirebaseDatabase.getInstance().reference.push()
                .setValue()

            chatt(
                input.text.toString(),
                FirebaseAuth.getInstance()
                    .currentUser
                    ?.getDisplayName()
            )
            input.setText("")
        }
    }
    private fun displayChatMessages() {
        var listOfMessages: ListView = list_of_messages
        adapter = object : FirebaseListAdapter<chatt>
    }
}

private fun DatabaseReference.setValue() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
