package com.example.tourrent.login

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.GMailSender
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristRecoverBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * A simple [Fragment] subclass.
 */
class tourist_recover : Fragment() {

    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTouristRecoverBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tourist_recover,
            container,
            false
        )

        binding.recover.setOnClickListener {
            rootRef.child("Profile").orderByChild("email").equalTo(binding.email.text.toString()).addListenerForSingleValueEvent(
                object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(activity, "Sending...", Toast.LENGTH_SHORT).show()
                            dataSnapshot.children.forEach{
                                val prof = it.getValue(Profile::class.java)

                                if (prof != null) {

                                    val recoverP = prof.password
                                    val sender = GMailSender(
                                        "tourrent.fyp@gmail.com",
                                        "Tourrent1234"
                                    )

                                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                                    StrictMode.setThreadPolicy(policy)

                                    sender.sendMail(
                                        "Password Recovery",
                                        "Password is $recoverP",
                                        "tourrent.fyp@gmail.com",
                                        binding.email.text.toString()
                                    )

                                    Toast.makeText(activity, "Recovery Email Sent", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(activity, "Invalid Email", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
        }

        binding.back.setOnClickListener { view?.findNavController()?.navigate(R.id.action_tourist_recover_to_homePage) }

        return binding.root
    }
}
