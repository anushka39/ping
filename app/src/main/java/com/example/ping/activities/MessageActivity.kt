package com.example.ping.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ping.R
import com.example.ping.util.DATA_MESSAGES
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val imageUrl: String? = null
    private var userId:String? = null
    private var userName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        if(intent.hasExtra(PARAM_USER_ID)&&intent.hasExtra(PARAM_USER_NAME)){
            userId = intent.getStringExtra(PARAM_USER_ID)
            userName = intent.getStringExtra(PARAM_USER_NAME)
        }else{
            Toast.makeText(this,"Error creating message", Toast.LENGTH_SHORT).show()
            finish()
        }
        messageProgressLayout.setOnTouchListener { v, event -> true }
    }

    fun addImage(v: View){

    }

    fun postMessage(v: View){
        messageProgressLayout.visibility = View.VISIBLE
        val text = messageText.text.toString()
        val hashtags = getHashtags(text)

        val messageId = firebaseDB.collection(DATA_MESSAGES).document()
        val message = com.example.ping.util.Message(messageId.id, arrayListOf(userId!!), userName, text, imageUrl, System.currentTimeMillis(), hashtags, arrayListOf())
        messageId.set(message)
            .addOnCompleteListener { finish() }
            .addOnFailureListener { e ->
                e.printStackTrace()
                messageProgressLayout.visibility = View.GONE
                Toast.makeText(this,"Failed to post message",Toast.LENGTH_SHORT).show()
            }
    }

    fun getHashtags(source: String): ArrayList<String> {
        val hashtags = arrayListOf<String>()
        var text = source

        while (text.contains("#")){
            var hashtag = ""
            val hash = text.indexOf("#")
            text = text.substring(hash + 1)

            val firstSpace = text.indexOf("")
            val firstHash = text.indexOf("#")

            if(firstSpace == -1 && firstHash == -1){
                hashtag = text.substring(0)
            }else if (firstSpace != -1 && firstSpace < firstHash ){
                hashtag = text.substring(0, firstSpace)
                text = text.substring(firstSpace + 1)
            } else {
                hashtag = text.substring(0,firstHash)
                text = text.substring(firstHash)
            }
            if (!hashtag.isNullOrEmpty()){
                hashtags.add(hashtag)
            }
        }
        return hashtags
    }
    companion object {
        val PARAM_USER_ID = "UserId"
        val PARAM_USER_NAME = "UserName"
        fun newIntent(context: Context, userId: String?, userName: String? ):Intent {
            val intent = Intent(context, MessageActivity::class.java )
            intent.putExtra(PARAM_USER_ID,userId)
            intent.putExtra(PARAM_USER_NAME, userName)
            return intent
        }
    }
}
