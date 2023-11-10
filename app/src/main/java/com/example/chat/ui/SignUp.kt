package com.example.chat.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chat.util.Constant
import com.example.chat.R
import com.example.chat.models.User
import com.example.chat.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private  lateinit var authentication: FirebaseAuth
    private  lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(requireNotNull(binding.root))
        startUp()
        binding.signUpBtn.setOnClickListener {
            val email = binding.emailSignUpEt.text.toString()
            val password = binding.passwordSignUpEt.text.toString()
            val confirmPassword = binding.confirmPasswordSignUpEt.text.toString()
            val userName = binding.nameSignUpEt.text.toString()
            registerUser(email,password,confirmPassword,userName)

        }
        binding.navTextLogin.setOnClickListener {
            finish()
            startActivity(Intent(this@SignUp,Login::class.java))

        }

    }
    private fun startUp() {
        authentication = FirebaseAuth.getInstance()
        binding.scrollViewSignUp.isVerticalScrollBarEnabled = false // to hide scroll icon
        databaseReference = FirebaseDatabase.getInstance().getReference()

    }

    private fun registerUser(email: String, password: String, confirmPassword: String,userName:String) {
        if (email.isNotEmpty()&&password.isNotEmpty()&&confirmPassword.isNotEmpty()&&userName.isNotEmpty()){
            if (password==confirmPassword){
                authentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                    emailCreated ->
                    if (emailCreated.isSuccessful){
                        Log.i(TAG, getString(R.string.created_user_success))
                        val uid = authentication.currentUser?.uid // to get uid
                        addUsersToDatabase(userName,email,uid)
                        val intentToSignActivity = Intent(this@SignUp, Login::class.java)
                        finish()
                        startActivity(intentToSignActivity)
                        Snackbar.make(binding.signUpBtn, getString(R.string.account_created_successfully), Snackbar.LENGTH_SHORT).show()
                    }else{
                        Log.i(TAG,emailCreated.exception?.message.toString())
                        Snackbar.make(binding.signUpBtn, getString(R.string.please_try_again), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }else{
                Snackbar.make(binding.signUpBtn,getString(R.string.password_doesn_t_match), Snackbar.LENGTH_SHORT).show()
            }
        }else{
            Snackbar.make(binding.signUpBtn,
                getString(R.string.please_enter_the_fields), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun addUsersToDatabase(userName: String, email: String, uid: String?) {
        val user = User(userName,email,uid!!)
        databaseReference.child(Constant.Keys.ROOT_CHILD).child(uid).setValue(user)
    }

    companion object{
        const val TAG = "SIGN_UP"
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}