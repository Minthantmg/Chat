package com.example.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val editTextEmail by lazy {
        findViewById<TextInputEditText>(R.id.textInputEditTextEmail)
    }

    private val editTextPassword by lazy {
        findViewById<TextInputEditText>(R.id.textInputEditTextPassword)
    }

    private val buttonLogin by lazy {
        findViewById<MaterialButton>(R.id.buttonLogin)
    }

    private val buttonRegister by lazy {
        findViewById<MaterialButton>(R.id.buttonRegister)
    }

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        buttonRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener{
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task->
                    if (task.isSuccessful){
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }else{
                        Log.w("LoginActivity", "create failure", task.exception)
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}