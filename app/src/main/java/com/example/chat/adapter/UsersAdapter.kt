package com.example.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.ui.Chat
import com.example.chat.R
import com.example.chat.models.User
import com.example.chat.databinding.UserItemBinding
import com.example.chat.models.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UsersAdapter(private val context :Context, private val listOfFriends:List<User>,
                   private val listener:SendDataToHomeActivity):
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>(){

    private  lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false)
        return UserViewHolder(view)
    }
    override fun getItemCount() = listOfFriends.size
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        databaseReference = FirebaseDatabase.getInstance().reference
        val currentFriend = listOfFriends[position]
        holder.binding.apply {
            userNameFriendTv.text = currentFriend.name

            listener.sendData(listOfFriends.size)
            
            itemViewFriend.setOnClickListener {

                val intent = Intent(context, Chat::class.java)
                intent.putExtra("namef",currentFriend.name)
                intent.putExtra("UID",currentFriend.uid)
                context.startActivity(intent)
            }
        }
    }

    class UserViewHolder(view : View):RecyclerView.ViewHolder(view){
        val binding = UserItemBinding.bind(view)
    }

    interface SendDataToHomeActivity{
        fun sendData(numberOfFriends:Int)
    }
}