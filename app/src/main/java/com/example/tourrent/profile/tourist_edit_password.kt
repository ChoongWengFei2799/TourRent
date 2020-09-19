package com.example.tourrent.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristEditPasswordBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class tourist_edit_password : Fragment() {

    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentTouristEditPasswordBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tourist_edit_password,
                container,
                false
        )

        binding.change.setOnClickListener { updatePass(binding.oldpass.text.toString(), binding.newpass.text.toString()) }
        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun updatePass(oldPass:String, newPass:String){

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key","")
        val mode =  prefs.getString("Mode","")
        if(mode == "T"){
            if (key != null) {
                rootRef.child("Profile").child(key).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.exists()){
                            val prof = dataSnapshot.getValue(Profile::class.java)

                            if(prof!!.password == oldPass){

                                rootRef.child("Profile").child(key).child("password").setValue(newPass)

                                Toast.makeText(activity, "Password Successfully Changed", Toast.LENGTH_LONG).show()
                            }
                            else{
                                Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_LONG).show()
                            }
                        }
                        else {
                            Toast.makeText(activity, "User Doesn't Exist", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
            }
        }
        else if(mode == "G"){
            if (key != null) {
                rootRef.child("Guide").child(key).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.exists()){
                            val prof = dataSnapshot.getValue(Guide::class.java)

                            if(prof!!.password == oldPass){
                                rootRef.child("Guide").child(key).child("password").setValue(newPass)

                                Toast.makeText(activity, "Password Successfully Changed", Toast.LENGTH_LONG).show()
                            }
                            else{
                                Toast.makeText(activity, "Incorrect Password", Toast.LENGTH_LONG).show()
                            }
                        }
                        else {
                            Toast.makeText(activity, "User Doesn't Exist", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
            }
        }
    }
}
