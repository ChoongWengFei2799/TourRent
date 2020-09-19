package com.example.tourrent.tourist

import android.content.Context
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
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class tourist_home : Fragment() {

    private var doubleBackToExitPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTouristHomeBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tourist_home,
                container,
                false
        )

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val mode = prefs.getString("Mode","")
        if(mode == "G"){
            binding.search.visibility = View.INVISIBLE
        }
        else{
            binding.search.visibility = View.VISIBLE
            binding.search.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_tourist_home_to_tourist_search)
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
