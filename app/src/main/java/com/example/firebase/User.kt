package com.example.firebase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val firstname: String? = null,
                val lastname: String? = null,
                val phonenumber: String? = null){
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
