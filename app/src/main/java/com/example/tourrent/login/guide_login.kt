package com.example.tourrent.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.MainActivity
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class guide_login : Fragment() {

    private var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideLoginBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_login,
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

        binding.forgot.setOnClickListener { view?.findNavController()?.navigate(R.id.action_guide_login_to_guide_recover) }

        return binding.root
    }

    private fun findEmail(email: String, pass: String){
        rootRef.child("Guide").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
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
        rootRef.child("Guide").child(key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val prof = dataSnapshot.getValue(Guide::class.java)
                if (prof!!.password == pass) {

                    val prefs =
                        requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("Key", key)
                    editor.apply()

                    val storageRef = FirebaseStorage.getInstance().reference
                    storageRef.child(key).downloadUrl.addOnSuccessListener {
                        if(prof.tags != "" && prof.location != "") {
                            if(prof.bio != "" && prof.price != "") {
                                val prefs =
                                    requireActivity().getSharedPreferences(
                                        "INFO",
                                        Context.MODE_PRIVATE
                                    )
                                val editor = prefs.edit()
                                editor.putString("Mode", "G")
                                editor.apply()

                                val intent = Intent (activity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                activity!!.startActivity(intent)
                            }
                            else{
                                view?.findNavController()
                                    ?.navigate(R.id.action_guide_login_to_guide_setup3)
                            }
                        }
                        else{
                            view?.findNavController()
                                ?.navigate(R.id.action_guide_login_to_guide_setup2)
                        }
                    }.addOnFailureListener {
                        view?.findNavController()
                            ?.navigate(R.id.action_guide_login_to_guide_setup1)
                    }
                }
                else {
                    Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
