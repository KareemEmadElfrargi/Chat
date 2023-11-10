package com.example.chat.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.chat.R
import com.example.chat.adapter.MessageAdapter
import com.example.chat.databinding.ActivityChatBinding
import com.example.chat.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chat : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: MutableList<Message>
    private var myRoom:String? = null
    private var friendRoom:String? = null
    private  lateinit var databaseReference: DatabaseReference

    private  lateinit var authentication: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startUp()
        val nameFriend = intent.getStringExtra("namef")
        val friendUid = intent.getStringExtra("UID")
        binding.chatAppbar.userNameFriendTv.text = nameFriend

        val myUid = FirebaseAuth.getInstance().currentUser?.uid //

        friendRoom = friendUid + myUid
        myRoom = myUid + friendUid


        messageAdapter = MessageAdapter(this@Chat,messageList)

        binding.recycleChat.adapter = messageAdapter

        databaseReference.child("chats").child(friendRoom!!).child("message")
            .addValueEventListener(object :ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.child("message").getValue(String::class.java)
                        val uidFriend1 = postSnapshot.child("friendId").getValue(String::class.java)
                        val currentMessage = Message(message!!, uidFriend1!!)
                        messageList.add(currentMessage)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        binding.sendButtonMessage.setOnClickListener {
            val message = binding.messageEditText.text.toString()
            val messageObject = Message(message, myUid!!)

            databaseReference
                .child("chats").child(friendRoom!!)
                .child("message")
                .push().setValue(messageObject).addOnSuccessListener {
                    databaseReference
                        .child("chats").child(myRoom!!)
                        .child("message")
                        .push().setValue(messageObject)
                }
            binding.messageEditText.text = null
        }
    }

    private fun startUp() {
        messageList = mutableListOf()
        authentication = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        setSupportActionBar(binding.chatAppbar.toolbar)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.call -> {
                Toast.makeText(this,"Start to call your friend",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.video_call -> {
                Toast.makeText(this,"Start to video call your friend",Toast.LENGTH_SHORT).show()
                true
            }
            // Handle other menu item clicks as needed
            else -> super.onOptionsItemSelected(item)
        }
    }

}