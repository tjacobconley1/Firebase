package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize the Real Time Database
        //val database = FirebaseDatabase.getInstance("https://fir-d8cd2-default-rtdb.firebaseio.com/")
        val database = Firebase.database
        // create references for the fields
        val FnameR = database.getReference("First Name")
        val LnameR = database.getReference("Last Name")
        val PhoneR = database.getReference("Phone Number")


        // Write a message to the database
        //val myRef = database.getReference("message")
        //myRef.setValue("Hello, World!")

        // function to write input to Firebase
        // suspended so it can run in a coroutine
        suspend fun WriteToFirebase(FN: String, LN: String, PN: String) {
            // creates an instance of the User Data Class
            //val user = User(FN, LN, PN)
            // set the values to write to Firebase
            // TODO -> This is writing
            // ""androidx.appcompat.widget.AppCompatEditText{bc16016 VFED..CL. ........ 0,188-1080,346 #7f0801da app:id/et_fName}""
            // to the Firebase fields
            FnameR.setValue(FN)
            LnameR.setValue(LN)
            PhoneR.setValue(PN)
        }


        // function to read the data from Firebase
        // also suspended in order to run in a coroutine
        suspend fun ReadFromFirebase() {
            // Reads 'First Name' from Firebase
            FnameR.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // pulled the values from Firebase into this
                    // object of the User data class in order to
                    // keep it from being encoded
                    val user = User(FnameR.database.toString(),
                        LnameR.database.reference.toString(),
                        PhoneR.database.reference.toString())
                    // pull the firstname from the User object
                    // then assign that to the cooresponding text view
                    tv_first_name.text = user.firstname.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    tv_first_name.text = "DatabaseError"
                }})
            // Reads 'Last Name' from Firebase
            LnameR.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // pulled the values from Firebase into this
                    // object of the User data class in order to
                    // keep it from being encoded
                    val user = User(FnameR.database.toString(),
                        LnameR.database.reference.toString(),
                        PhoneR.database.reference.toString())
                    // pull the lastname from the User object
                    // then assign that to the cooresponding text view
                    tv_last_name.text = user.lastname.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    tv_last_name.text = "DatabaseError"
                }})
            // Reads 'Phone Number' from Firebase
            PhoneR.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // pulled the values from Firebase into this
                    // object of the User data class in order to
                    // keep it from being encoded
                    val user = User(FnameR.database.toString(),
                        LnameR.database.reference.toString(),
                        PhoneR.database.reference.toString())
                    // pull the lastname from the User object
                    // then assign that to the cooresponding text view
                    tv_phone_number.text = user.phonenumber.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    tv_last_name.text = "DatabaseError"
                }})}






            // this button is for to read the
            // previously written data to Firbase
            // and display it in the UI somehow
            btn_readGFB.setOnClickListener {
                // set tv to null
                //tv_first_name.text = null
                CoroutineScope(Dispatchers.IO).launch {
                    ReadFromFirebase()
                }}

            btn_writeFB.setOnClickListener {
                // launch coroutine
                // 3 possible coroutine scopes
                // IO, Main, Default(heavy computational work)
                CoroutineScope(Dispatchers.IO).launch {
                    // to firebase in a background thread
                    // this creates an object of the User data class
                    val user2 = User(et_fName.text.toString(), et_lName.text.toString(), et_phone.text.toString())
                    // those values are then passed into the WriteToFirebase Function up top
                    WriteToFirebase(user2.firstname.toString(), user2.lastname.toString(), user2.phonenumber.toString())
                }
                // reset the edit text values
                et_fName?.text = null
                et_lName?.text = null
                et_phone?.text = null
            }



            // should clear the UI/possibly
            // the firebase entry also
            btn_clear.setOnClickListener {
                tv_first_name.text = null
                tv_last_name.text = null
                tv_phone_number.text = null
            }}}
