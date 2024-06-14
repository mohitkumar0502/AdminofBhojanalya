package com.example.adminofbhojanalya

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminofbhojanalya.databinding.ActivityAddItemBinding
import com.example.adminofbhojanalya.databinding.ActivityMainBinding
import com.example.adminofbhojanalya.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddItemActivity : AppCompatActivity() {

    //food item Details
    private lateinit var foodName:String
    private lateinit var foodPrice:String
    private lateinit var foodDescription:String
    private  var foodImageUri: Uri? = null
    private lateinit var foodIngredients:String

    //Firebase invoking
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding:ActivityAddItemBinding by lazy{
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initialization of Firebase
        auth= FirebaseAuth.getInstance()
        //Initialization of Firebase database Instance
        database= FirebaseDatabase.getInstance()

        binding.addItemButton.setOnClickListener {
            //get Data from Fields
            foodName=binding.foodName.text.toString().trim()
            foodPrice=binding.foodPrice.text.toString().trim()
            foodDescription=binding.description.text.toString().trim()
            foodIngredients=binding.ingridents.text.toString().trim()

            if (!(foodName.isBlank()||foodPrice.isBlank()||foodDescription.isBlank()||foodIngredients.isBlank())){
                uploadData()
                Toast.makeText(this, "Item Added Successfully" , Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()

            }
        }
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backButton.setOnClickListener {
           finish()
        }
    }

    private fun uploadData() {
        //get a reference of the "menu" node in the database
        val menuRef:DatabaseReference = database.getReference("menu")
        //Generate a unique key for the menu item
        val newItemKey: String? = menuRef.push().key

        if (foodImageUri != null){

            //Firebase Storage image uploading and saving variables with unique key
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef:StorageReference=storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask= imageRef.putFile(foodImageUri !!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl ->
                    //create a new menu item
                    val newItem = AllMenu(
                        newItemKey,
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredients,
                        foodImage = downloadUrl.toString(),)

                    newItemKey?.let {
                        key->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data uploaded successfully", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this, "Data uploading Failure", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

            } .addOnFailureListener {
                Toast.makeText(this, "Image uploading Failed", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
            }
    }

    private val pickImage=registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        if (uri !=null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}