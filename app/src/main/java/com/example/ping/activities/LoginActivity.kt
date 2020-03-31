package com.example.ping.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.ping.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity()  {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = firebaseAuth.addAuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        val users = FirebaseAuth.getInstance().currentUser
        user?.let {
            startActivity(HomeActivity.newIntent(this))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setTextChangeListener(emailET, emailTIL)
        setTextChangeListener(passwordET, passwordTIL)
        
        loginProgressLayout.setOnTouchListener { v, event -> true }
    }
    fun setTextChangeListener(et: EditText, til: TextInputLayout) {
        et.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                til.isErrorEnabled = false

            }

        })
    }
    fun onLogin(v: View) {
        var proceed = true
        val users = FirebaseAuth.getInstance().currentUser
        if (emailET.text.isNullOrEmpty()) {
            emailTIL.error = "Email is required"
            emailTIL.isErrorEnabled = true
            proceed = false
        }
        if (passwordET.text.isNullOrEmpty()) {
            passwordTIL.error = "Password is required"
            passwordTIL.isErrorEnabled = true
            proceed = false
        }

        if (proceed) {
            while (users != null && !users.isEmailVerified) {
                loginProgressLayout.visibility = View.VISIBLE
                Toast.makeText(this, " login error please verify", Toast.LENGTH_LONG).show()
//                firebaseAuth.signInWithEmailAndPassword(
//                    emailET.text.toString(),
//                    passwordET.text.toString()
//                )
//                    .addOnCompleteListener { task: Task<AuthResult> ->
//                        if (!task.isSuccessful) {
//                            loginProgressLayout.visibility = View.GONE
//                            Toast.makeText(
//                                this,
//                                "Login error: ${task.exception?.localizedMessage}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        e.printStackTrace()
//                        loginProgressLayout.visibility = View.GONE
//                    }
            }
            Toast.makeText(this,"you are verified", Toast.LENGTH_LONG).show()
        }
    }

    fun goToSignUp(v: View){
        startActivity(SignUpActivity.newIntent(this))
        finish()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener{firebaseAuthListener}
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener{firebaseAuthListener}

    }

    companion object {
        fun newIntent(context: Context)= Intent(context, LoginActivity::class.java )
    }
}

