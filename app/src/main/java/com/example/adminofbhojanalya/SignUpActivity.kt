package com.example.adminofbhojanalya
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.adminofbhojanalya.databinding.ActivitySignUpBinding
import com.example.adminofbhojanalya.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth :FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var nameOfResturant:String
    private lateinit var database :DatabaseReference

    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //Firebase Auth initialization
        auth =Firebase.auth
        // Firebase Database initialization
        database=Firebase.database.reference

        binding.createbutton.setOnClickListener {
            //getting text from edittext
            userName=binding.signupownername.text.toString().trim()
            nameOfResturant=binding.signupResturantname.text.toString().trim()
            email=binding.signupEmail.text.toString().trim()
            password=binding.signupPassword.text.toString().trim()

            if (userName.isBlank() || nameOfResturant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }

        }
        binding.alreadybutton.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        val locationList= arrayOf("Sultanpur")
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView = binding.listofLocation
        autoCompleteTextView.setAdapter(adapter)
            }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount:Failure",task.exception)
            }
        }
    }
 // save data in to Database
    private fun saveUserData() {
        userName=binding.signupownername.text.toString().trim()
        nameOfResturant=binding.signupResturantname.text.toString().trim()
        email=binding.signupEmail.text.toString().trim()
        password=binding.signupPassword.text.toString().trim()

        val user=UserModel(userName,nameOfResturant,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
     //save user data in firebase database
        database.child("user").child(userId).setValue(user)


    }
}
