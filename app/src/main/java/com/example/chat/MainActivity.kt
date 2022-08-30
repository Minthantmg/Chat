package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val editTextMessage by lazy {
        findViewById<EditText>(R.id.editTextMessage)
    }
    private val rvChat by lazy {
        findViewById<RecyclerView>(R.id.rvChat)
    }
    private val buttonSend by lazy {
        findViewById<Button>(R.id.buttonSend)
    }

    private val chatRecyclerViewAdapter = ChatRecyclerViewAdapter()

    private lateinit var chatNodeReference: DatabaseReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_main)

        rvChat.apply {
            adapter = chatRecyclerViewAdapter
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }
        buttonSend.setOnClickListener {
            editTextMessage.isSingleLine = true
            val message = editTextMessage.text.toString()
            sendMessage(message)
        }
        chatNodeReference = Firebase.database.reference.child("chats")

        chatNodeReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatList = mutableListOf<Chat>()

                snapshot.children.forEach {
                    val message = it.child("message").getValue<String>() ?: return@forEach
                    val sender = it.child("sender").getValue<String>() ?: return@forEach
                    val chatId = it.key ?: return@forEach
                    val chat = Chat(
                        chatId = chatId,
                        message = message,
                        userName = sender
                    )
                    chatList.add(chat)
                }
                chatRecyclerViewAdapter.submitList(chatList)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }

        })
    }

    fun sendMessage(message: String) {
        chatNodeReference.push().apply {
            child("message").setValue(message)
            child("sender").setValue(auth.currentUser?.email ?: "Dummy")
        }
    }

    fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            logout()
        }
        return super.onOptionsItemSelected(item)
    }
}