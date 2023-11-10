package com.example.chat.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.chat.util.Constant
import com.example.chat.R
import com.example.chat.models.User
import com.example.chat.adapter.UsersAdapter
import com.example.chat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(),UsersAdapter.SendDataToHomeActivity {
    private lateinit var binding: ActivityMainBinding
    private lateinit var friendsList: MutableList<User>
    private lateinit var adapter: UsersAdapter
    private  lateinit var authentication: FirebaseAuth
    private  lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startUP()
        adapter = UsersAdapter(this@MainActivity,friendsList,this@MainActivity)
        binding.recycleFriends.adapter = adapter

        databaseReference.child("friends").addValueEventListener(object:ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                friendsList.clear()
                for (postSnapshot in snapshot.children){
                    val email = postSnapshot.child(Constant.Keys.EMAIL_USER).getValue(String::class.java)
                    val name = postSnapshot.child(Constant.Keys.NAME_USER).getValue(String::class.java)
                    val uid = postSnapshot.child(Constant.Keys.UID_USER).getValue(String::class.java)

                    val currentUser = User(name!!,email!!,uid!!)
                    if (authentication.currentUser?.uid != currentUser.uid){
                        friendsList.add(currentUser)
                    }

                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun startUP() {
        authentication = FirebaseAuth.getInstance()
        friendsList = mutableListOf()
        databaseReference = FirebaseDatabase.getInstance().reference
        setSupportActionBar(binding.homeAppbar.toolbarHome)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                Toast.makeText(this,"Start to Logout", Toast.LENGTH_SHORT).show()
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun logOut() {
        authentication.signOut()
        finish()
        startActivity(Intent(this@MainActivity,Login::class.java))
    }
    override fun sendData(numberOfFriends: Int) {
        binding.homeAppbar.friendsNumber.text = numberOfFriends.toString()
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}