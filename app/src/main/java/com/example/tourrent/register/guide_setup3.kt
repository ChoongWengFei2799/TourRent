package com.example.tourrent.register


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tourrent.MainActivity
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideSetup3Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class guide_setup3 : Fragment() {

    private var doubleBackToExitPressedOnce = false
    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideSetup3Binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_setup3,
                container,
                false
        )

        binding.next.setOnClickListener {
            if(binding.bio.text.toString() == ""){
                Toast.makeText(activity, "Please type Your Bio", Toast.LENGTH_SHORT).show()
            }
            else if(binding.editText.text.toString() == ""){
                Toast.makeText(activity, "Please type Your Price", Toast.LENGTH_SHORT).show()
            }
            else{
                val prefs =
                    requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
                val key = prefs.getString("Key","")
                if (key != null) {
                    rootRef.child("Guide").child(key).child("bio").setValue(binding.bio.text.toString())
                    rootRef.child("Guide").child(key).child("price").setValue(binding.editText.text.toString())

                    val editor = prefs.edit()
                    editor.putString("Mode", "G")
                    editor.apply()

                    val intent = Intent (activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    requireActivity().startActivity(intent)
                }
            }
        }
        return binding.root
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
