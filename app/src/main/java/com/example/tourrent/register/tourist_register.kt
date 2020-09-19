package com.example.tourrent.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tourrent.R
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.databinding.FragmentTouristRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class tourist_register : Fragment() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Profile")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTouristRegisterBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tourist_register,
                container,
                false
        )

        binding.register.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.pass.text.toString()

            if (name == "" || pass == "") {
                Toast.makeText(activity, "Invalid data Filled In", Toast.LENGTH_LONG).show()
            }
            else if (email == "" || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(activity, "Invalid Email Filled In", Toast.LENGTH_LONG).show()
            }
            else {
                val newProf = Profile(email, name, pass)
                readProfile(newProf)
            }
        }
        return binding.root
    }

    fun saveProfile(profile: Profile){
        val id = myRef.push().key
        if (id != null) {
            myRef.child(id).setValue(profile)

            Toast.makeText(activity, "Register Success, Please Login", Toast.LENGTH_LONG).show()
            view?.findNavController()?.navigate(R.id.action_tourist_register_to_tourist_login)
        }
    }

    private fun readProfile(profile: Profile){
        myRef.orderByChild("email").equalTo(profile.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists())
                    saveProfile(profile)
                else {
                    Toast.makeText(activity, "Email Exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
