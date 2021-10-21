package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var clickNum = 0
    private lateinit var textView: TextView
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var FirstName: EditText
    private lateinit var LastName: EditText
    private lateinit var Phone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setSupportActionBar(toolbar)
        textView = findViewById<TextView>(R.id.myText)
        analytics = FirebaseAnalytics.getInstance(this)

        val upButton = findViewById<Button>(R.id.button)
        upButton.setOnClickListener {

            // when the button is clicked the values
            // in the boxes are stored to thse variables
            FirstName = et_fName
            LastName = et_lName
            Phone = et_phone
            // launch coroutine
            // 3 possible coroutine scopes
            // IO, Main, Default(heavy computational work)
            CoroutineScope(Dispatchers.IO).launch {
                // write the edit text values
                // to firebase in a background thread
                WriteToFirebase(FirstName.toString(), LastName.toString(), Phone.toString())
            }

            clickNum++
            textView.text = "Submitted -> {$clickNum} times."

        }

    }


    suspend fun WriteToFirebase(FNAME: String, LNAME: String, PHONE: String) {
        // [START write_message]
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val FnameRef = database.getReference("First Name")
        val LnameRef = database.getReference("Last Name")
        val PhoneRef = database.getReference("Phone Number")

        FnameRef.setValue(FNAME)
        LnameRef.setValue(LNAME)
        PhoneRef.setValue(PHONE)

    }

}