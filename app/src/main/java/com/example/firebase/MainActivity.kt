package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.parcel.Parcelize
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

        // [START write_message]
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val FnameRef = database.getReference("First Name")
        val LnameRef = database.getReference("Last Name")
        val PhoneRef = database.getReference("Phone Number")

        // function to write input to Firebase
        // suspended so it can run in a coroutine
        suspend fun WriteToFirebase(FN: String, LN: String, PN: String){
            FnameRef.setValue(FN)
            LnameRef.setValue(LN)
            PhoneRef.setValue(PN)
        }

        // function to read the data from Firebase
        // also suspended in order to run in a coroutine

        suspend fun ReadFromFirebase(){
            // Reads from Firebase
            FnameRef.addValueEventListener(object : ValueEventListener{
                // this is executed on data changes
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with
                    // the initial value and again whenever
                    // data at this location is updated
                    val value = dataSnapshot.getValue()
                    tv_DBRead.text = value.toString()
                }
                // this is executed when an error occurs
                override fun onCancelled(error: DatabaseError) {

                }

            })
        }



        //val upButton = findViewById<Button>(R.id.btn_writeFB)
        btn_writeFB.setOnClickListener {
            // launch coroutine
            // 3 possible coroutine scopes
            // IO, Main, Default(heavy computational work)
            CoroutineScope(Dispatchers.IO).launch {
                // write the edit text values
                // to firebase in a background thread
                WriteToFirebase(et_fName.toString(), et_lName.toString(), et_phone.toString())
            }
            // reset the edit text values
            et_fName?.text = null
            et_lName?.text = null
            et_phone?.text = null
        }




        // this button is for to read the
        // previously written data to Firbase
        // and display it in the UI somehow
        btn_readGFB.setOnClickListener{
            // set tv to null
            tv_DBRead.text = null
            CoroutineScope(Dispatchers.IO).launch {
                ReadFromFirebase()
            }
        }

        // should clear the UI/possibly
        // the firebase entry also
        btn_clear.setOnClickListener{
            tv_DBRead.text = null
        }


    }
}
