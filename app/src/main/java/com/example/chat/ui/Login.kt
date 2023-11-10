package com.example.chat.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chat.R
import com.example.chat.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding :ActivityLoginBinding
    private  lateinit var authentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(requireNotNull(binding.root))

        startUp()
        binding.apply {
            navTextSignUp.setOnClickListener {
                // Coming up! to use Navigation component in future Commit
                val intent = Intent(this@Login, SignUp::class.java)
                startActivity(intent)
            }
            signInBtn.setOnClickListener {
                val email = binding.emailSignInEt.text.toString()
                val password = binding.passwordSignInEt.text.toString()
                login(email,password)
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun login(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()){
            authentication.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                loginAccount ->
                if (loginAccount.isSuccessful){
                    Log.i(TAG, getString(R.string.user_sign_in_successfully))
                    val intentToHomeActivity = Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intentToHomeActivity)
                    Snackbar.make(binding.signInBtn, getString(R.string.user_sign_in_successfully), Snackbar.LENGTH_SHORT).show()
                }else{
                    Log.i(TAG,loginAccount.exception?.message.toString())
                    val snackBar =  Snackbar.make(binding.signInBtn, getString(R.string.inValidAccount), Snackbar.LENGTH_SHORT)
                        snackBar.setAction("Create account!"){
                            startActivity(Intent(this@Login, SignUp::class.java))
                        }
                    snackBar.show()
                }
            }
        }else{
            Snackbar.make(binding.signInBtn,
                getString(R.string.please_enter_email_and_password), Snackbar.LENGTH_SHORT).show()
        }
    }
    private fun startUp() {
        authentication = FirebaseAuth.getInstance()
    }
    companion object{
        const val TAG = "LOGIN"
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}