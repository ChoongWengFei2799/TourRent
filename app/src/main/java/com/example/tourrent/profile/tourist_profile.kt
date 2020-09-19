package com.example.tourrent.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristProfileBinding

class tourist_profile : Fragment() {

    private lateinit var  viewModel: tourist_profile_view_model

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentTouristProfileBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tourist_profile,
                container,
                false
        )

        viewModel = ViewModelProvider(this).get(tourist_profile_view_model::class.java)

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val mode = prefs.getString("Mode","")
        if(mode == "T") {
            binding.name.visibility = View.VISIBLE
            binding.email.visibility = View.VISIBLE
            binding.pass.visibility = View.VISIBLE
            binding.logout.visibility = View.VISIBLE
            binding.vHistory.visibility = View.VISIBLE

            binding.imageView14.visibility = View.GONE
            binding.textView31.visibility = View.GONE
            binding.login.visibility = View.GONE

            val key = prefs.getString("Key", "")
            if (key != null) {
                viewModel.setInfo(key)

                viewModel.email.observe(viewLifecycleOwner, Observer { email ->
                    binding.email.text = "$email"
                })

                viewModel.name.observe(viewLifecycleOwner, Observer { name ->
                    binding.name.text = "$name"
                })
            }

            binding.pass.setOnClickListener { view?.findNavController()?.navigate(R.id.action_tourist_profile_to_tourist_edit_password) }

            binding.vHistory.setOnClickListener { view?.findNavController()?.navigate(R.id.action_tourist_profile_to_booking_history) }

            binding.logout.setOnClickListener {
                val editor = prefs.edit()
                editor.putString("Key", "")
                editor.apply()

                editor.putString("Mode", "")
                editor.apply()

                view?.findNavController()?.navigate(R.id.action_tourist_profile_to_tourist_home)
            }
        }
        else{
            binding.name.visibility = View.GONE
            binding.email.visibility = View.GONE
            binding.pass.visibility = View.GONE
            binding.logout.visibility = View.GONE
            binding.vHistory.visibility = View.GONE

            binding.imageView14.visibility = View.VISIBLE
            binding.textView31.visibility = View.VISIBLE
            binding.login.visibility = View.VISIBLE

            binding.login.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_tourist_profile_to_HomePage)
            }
        }
        return binding.root
    }
}
