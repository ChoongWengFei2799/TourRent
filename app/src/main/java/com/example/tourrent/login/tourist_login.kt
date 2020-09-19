package com.example.tourrent.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class tourist_login : Fragment() {

    private var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTouristLoginBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tourist_login,
                container,
                false
        )

        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.pass.text.toString()

            if (pass == "") {
                Toast.makeText(activity, "Password Cannot be Empty", Toast.LENGTH_LONG).show()

            }
            else if (email == "" || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(activity, "Invalid Email", Toast.LENGTH_LONG).show()
            }
            else {
                findEmail(email, pass)
            }
        }

        binding.forgot.setOnClickListener { view?.findNavController()?.navigate(R.id.action_tourist_login_to_tourist_recover) }

        return binding.root
    }

    private fun findEmail(email: String, pass: String){

        rootRef.child("Profile").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    var key = ""
                    for (childSnapshot in dataSnapshot.children) {
                        key = childSnapshot.key.toString()
                    }
                    findProfile(key, pass)
                }
                else {
                    Toast.makeText(activity, "Email Doesn't Exist", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun findProfile(key: String, pass:String){
        rootRef.child("Profile").child(key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val prof = dataSnapshot.getValue(Profile::class.java)

                if (prof!!.password == pass) {
                    val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("Key", key)
                    editor.apply()

                    editor.putString("Mode", "T")
                    editor.apply()

                    view?.findNavController()
                       ?.navigate(R.id.action_tourist_login_to_tourist_home)
                }
                else {
                    Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
