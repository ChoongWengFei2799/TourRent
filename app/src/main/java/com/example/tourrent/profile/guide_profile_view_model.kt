package com.example.tourrent.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourrent.Dclass.Guide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class guide_profile_view_model : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _price = MutableLiveData<String>()
    val price: LiveData<String>
        get() = _price

    private val _location = MutableLiveData<String>()
    val location: LiveData<String>
        get() = _location

    private val _tags = MutableLiveData<String>()
    val tags: LiveData<String>
        get() = _tags

    private val _bio = MutableLiveData<String>()
    val bio: LiveData<String>
        get() = _bio

    var rootRef = FirebaseDatabase.getInstance().reference

    fun setInfo(key: String) {
        rootRef.child("Guide").child(key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val prof = dataSnapshot.getValue(Guide::class.java)

                    _name.value = prof!!.name
                    _price.value = prof!!.price
                    _location.value = prof!!.location
                    _tags.value = prof!!.tags
                    _bio.value = prof!!.bio
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}