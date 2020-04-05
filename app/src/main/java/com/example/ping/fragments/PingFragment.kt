package com.example.ping.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.ping.adapters.PostListAdapter
import com.example.ping.listeners.HomeCallBack
import com.example.ping.listeners.PostListener
import com.example.ping.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.RuntimeException

abstract class PingFragment: Fragment() {
    protected var messageAdapter: PostListAdapter? = null
    protected var currentUser: User? = null
    protected val firebaseDB = FirebaseFirestore.getInstance()
    protected val userId = FirebaseAuth.getInstance().currentUser?.uid
    protected val listener: PostListener? = null
    protected var callBack: HomeCallBack? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeCallBack) {
            callBack = context
        } else {
            throw RuntimeException(context.toString() + "must implement Callback")
        }
    }

    fun setUser(user: User?){
        this.currentUser = user
    }

    abstract fun updateList()

    override fun onResume() {
        super.onResume()
        updateList()
    }
}