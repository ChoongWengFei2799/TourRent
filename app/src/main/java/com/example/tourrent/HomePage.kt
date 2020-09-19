package com.example.tourrent

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tourrent.databinding.FragmentHomePageBinding

class HomePage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomePageBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home_page,
            container,
            false
        )

        binding.back.setOnClickListener { view?.findNavController()?.navigate(R.id.action_homePage_to_tourist_profile) }

        binding.login.setOnClickListener { if(binding.tourist.isChecked) { view?.findNavController()?.navigate(R.id.action_homePage_to_tourist_login) }
                                            else{ view?.findNavController()?.navigate(R.id.action_homePage_to_guide_login) } }

        binding.register.setOnClickListener{if(binding.tourist.isChecked) {view?.findNavController()?.navigate(R.id.action_homePage_to_tourist_register)}
        else                          {view?.findNavController()?.navigate(R.id.action_homePage_to_guide_register)}}

        return binding.root
    }
}