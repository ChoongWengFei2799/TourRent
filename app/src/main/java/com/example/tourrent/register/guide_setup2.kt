package com.example.tourrent.register

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideSetup2Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class guide_setup2 : Fragment() {

    private var doubleBackToExitPressedOnce = false
    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideSetup2Binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_setup2,
                container,
                false
        )

        binding.location.setOnClickListener { locationSpinner() }
        binding.language.setOnClickListener { view?.findNavController()?.navigate(R.id.action_guide_setup2_to_guide_edit_language) }
        binding.tags.setOnClickListener { view?.findNavController()?.navigate(R.id.action_guide_setup2_to_guide_edit_tags) }
        binding.next.setOnClickListener {
            val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
            val key = prefs.getString("Key","")
            if (key != null) {
                rootRef.child("Guide").child(key).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val prof = dataSnapshot.getValue(Guide::class.java)
                            if (prof != null) {
                                if(prof.tags == ""){
                                    Toast.makeText(activity, "Please Select A Language / Tag", Toast.LENGTH_SHORT).show()
                                }
                                else if(prof.location == ""){
                                    Toast.makeText(activity, "Please Select A Location", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    view?.findNavController()?.navigate(R.id.action_guide_setup2_to_guide_setup3)
                                }
                            }
                        } else {
                            Toast.makeText(activity, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
            }
        }
        return binding.root
    }
    
    private fun locationSpinner(){
        val b: AlertDialog.Builder = AlertDialog.Builder(activity)
        b.setTitle("Location")
        val location = arrayOf("Kuala Lumpur", "Putrajaya", "Negeri Sembilan", "Selangor", "Melacca", "Johor", "Pahang", "Perak", "Kedah", "Perlis", "Penang", "Terengannu", "Kelantan", "Sabah", "Sarawak")
        b.setItems(location) { dialog, which ->
            dialog.dismiss()
            when (which) {
                0 ->  updateLocation("Kuala Lumpur")
                1 ->  updateLocation("Putrajaya")
                2 ->  updateLocation("Negeri Sembilan")
                3 ->  updateLocation("Selangor")
                4 ->  updateLocation("Melacca")
                5 ->  updateLocation("Johor")
                6 ->  updateLocation("Pahang")
                7 ->  updateLocation("Perak")
                8 ->  updateLocation("Kedah")
                9 ->  updateLocation("Perlis")
                10 -> updateLocation("Penang")
                11 -> updateLocation("Terengannu")
                12 -> updateLocation("Kelantan")
                13 -> updateLocation("Sabah")
                14 -> updateLocation("Sarawak")
            }
        }
        b.show()
    }

    private fun updateLocation(newloc :String) {
        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key", "")

        if (newloc != "") {
            if (key != null) {
                rootRef.child("Guide").child(key).child("location").setValue(newloc)
                Toast.makeText(activity, "Location Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "No Location Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (doubleBackToExitPressedOnce) {
                    requireActivity().finish()
                }

                this.doubleBackToExitPressedOnce = true
                Toast.makeText(activity, "Click BACK Again to Exit", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)

                true
            } else false
        }
    }
}
