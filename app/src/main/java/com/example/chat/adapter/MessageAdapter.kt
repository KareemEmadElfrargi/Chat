package com.example.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.models.Message
import com.example.chat.R
import com.example.chat.databinding.FriendMessageLayoutBinding
import com.example.chat.databinding.MeMessageLayoutBinding
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context:Context, private val messageList: List<Message>):RecyclerView
    .Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            ITEM_RECEIVE_ME ->{
                val view = LayoutInflater.from(context).inflate(R.layout.me_message_layout,parent,false)
                return MeViewHolder(view)
            }
            ITEM_SENDER_FRIEND ->{
                val view = LayoutInflater.from(context).inflate(R.layout.friend_message_layout,parent,false)
                return FriendViewHolder(view)
            }
        }
        return super.createViewHolder(parent,viewType)
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == MeViewHolder::class.java){
            val viewHolder = holder as MeViewHolder
            viewHolder.binding.apply {
                messageTextSender.text = currentMessage.message
            }
        }else{
            val viewHolder = holder as FriendViewHolder
            viewHolder.binding.apply{
                messageTextReceive.text = currentMessage.message
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        val meUid = FirebaseAuth.getInstance().currentUser?.uid
        val friendId = currentMessage.friendId

        return if (meUid == friendId){
            ITEM_RECEIVE_ME
        }else{
            ITEM_SENDER_FRIEND
        }
    }
    //abstract class BaseViewHolder(view :View):RecyclerView.ViewHolder(view){}
    class MeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = MeMessageLayoutBinding.bind(view)
    }

    class FriendViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = FriendMessageLayoutBinding.bind(view)
    }

    companion object{
        const val ITEM_RECEIVE_ME = 2
        const val ITEM_SENDER_FRIEND = 3
    }

}