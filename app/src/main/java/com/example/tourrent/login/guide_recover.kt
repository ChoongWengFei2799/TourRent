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
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.Dclass.Profile
import com.example.tourrent.GMailSender
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideRecoverBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class guide_recover : Fragment() {

    var rootRef = FirebaseDatabase.getInstance().reference
    lateinit var recoverP: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideRecoverBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_guide_recover,
            container,
            false
        )

        binding.recover.setOnClickListener {
            rootRef.child("Guide").orderByChild("email").equalTo(binding.email.text.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()) {
                        dataSnapshot.children.forEach{
                            val guide = it.getValue(Guide::class.java)

                            if (guide != null) {
                                val recoverP = guide.password
                                val sender = GMailSender(
                                    "tourrent.fyp@gmail.com",
                                    "Tourrent1234"
                                )

                                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                                StrictMode.setThreadPolicy(policy)

                                Toast.makeText(activity, "Sending...", Toast.LENGTH_SHORT).show()

                                sender.sendMail(
                                    "Password Recovery",
                                    "Password is $recoverP",
                                    "tourrent.fyp@gmail.com",
                                    binding.email.text.toString()
                                )

                                Toast.makeText(activity, "Recovery Email Sent", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(activity,"Invalid Email", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
        }

        binding.back.setOnClickListener { view?.findNavController()?.navigate(R.id.action_guide_recover_to_homePage) }

        return binding.root
    }
}
