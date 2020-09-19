package com.example.tourrent.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class guide_register : Fragment() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Guide")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideRegisterBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_register,
                container,
                false
        )

        binding.register.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.pass.text.toString()

            if (name == "" || pass == "") {
                Toast.makeText(activity, "Invalid Data Filled In", Toast.LENGTH_LONG).show()
            }
            else if (email == "" || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(activity, "Invalid Email Filled In", Toast.LENGTH_LONG).show()
            }
            else {
                val newProf = Guide(email, name, pass, "", "" , "","")
                readGuide(newProf)
            }
        }

        return binding.root
    }

    private fun readGuide(guide: Guide){
        myRef.orderByChild("email").equalTo(guide.email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists())
                    saveGuide(guide)
                else {
                    Toast.makeText(activity, "Email Exist", Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    fun saveGuide(guide: Guide){
        val key = myRef.push().key
        if (key != null) {
            myRef.child(key).setValue(guide)

            val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
            val editor = prefs.edit()

            editor.putString("Key", key)
            editor.apply()

            view?.findNavController()?.navigate(R.id.action_guide_register_to_guide_setup1)
        }
    }
}
