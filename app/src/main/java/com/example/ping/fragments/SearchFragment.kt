package com.example.ping.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.ping.R
import com.example.ping.adapters.PostListAdapter
import com.example.ping.listeners.PostListener

import com.example.ping.util.DATA_MESSAGES
import com.example.ping.util.DATA_MESSAGE_HASHTAGS
import com.example.ping.util.Message
import com.example.ping.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : PingFragment() {

    private var currentHashtag = ""
    private var messageAdapter: PostListAdapter? = null
    private var currentUser: User? = null
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val listener: PostListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageAdapter = PostListAdapter(userId!!, arrayListOf())
        messageAdapter?.setListener(listener)
        postList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            updateList()
        }
    }

    fun newHashtag(term: String){
        currentHashtag = term
        followHashtag.visibility = View.VISIBLE
        updateList()
    }

    fun updateList() {
        postList?.visibility = View.GONE
        firebaseDB.collection(DATA_MESSAGES)
            .whereArrayContains(DATA_MESSAGE_HASHTAGS, currentHashtag).get()
            .addOnSuccessListener {list ->
                postList?.visibility = View.VISIBLE
                val posts = arrayListOf<Message>()
                for (document in list.documents){
                    val post = document.toObject(Message::class.java)
                    post?.let{ posts.add(it)}
                }
                val sortedMessages = posts.sortedWith(compareByDescending { it.timestamp })
                messageAdapter?.updatePosts(sortedMessages)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
