package com.example.hackofpi
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.hackofpi.model.User

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : BaseActivity()
{
    private lateinit var auth : FirebaseAuth

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        /**
        // setting flags for full screen
        window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

         */


        setUpActionBar()

        val btn_sign_in : Button = findViewById(R.id.btn_sign_in)
        btn_sign_in.setOnClickListener{
            signInRegisteredUser()
        }

    }


    /**
     * A function for actionBar Setup.
     */
    private fun setUpActionBar()
    {
        val toolbar_sign_in_activity : Toolbar
        toolbar_sign_in_activity   = findViewById(R.id.toolbar_sign_in_activity)

        setSupportActionBar(toolbar_sign_in_activity)

        val actionBar = supportActionBar

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
            actionBar.title = resources.getString(R.string.sign_in_page_title)
        }

        toolbar_sign_in_activity.setNavigationOnClickListener{
            onBackPressed()
        }

    }

    /**
     * A function for Sign-In using the registered user using the email and password.
     */
    private fun signInRegisteredUser() {
        // Here we get the text from editText and trim the space
        val email: String = et_email_sign_in.text.toString().trim { it <= ' ' }
        val password: String = et_password_sign_in.text.toString().trim { it <= ' ' }

        if (validateForm(email, password))
        {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        // Calling the FirestoreClass signInUser function to get the data of user from database.
                        FirestoreClass().loadUserData(this@SignInActivity)
                    }
                    else
                    {
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }


    /**
     * A function to validate the entries of a user.
     */
    private fun validateForm(email: String, password: String): Boolean
    {
        return if (TextUtils.isEmpty(email))
        {
            showErrorSnackBar("Please enter email.")
            false
        }
        else if (TextUtils.isEmpty(password))
        {
            showErrorSnackBar("Please enter password.")
            false
        }
        else
        {
            true
        }
    }


    /**
     * A function to get the user details from the firestore database after authentication.
     */
    fun signInSuccess(user: User)
    {

        hideProgressDialog()

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        this.finish()
    }


}