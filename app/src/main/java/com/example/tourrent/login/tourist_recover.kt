package com.example.tourrent.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristRecoverBinding

/**
 * A simple [Fragment] subclass.
 */
class tourist_recover : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentTouristRecoverBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tourist_recover,
                container,
                false
        )

        binding.back.setOnClickListener { view?.findNavController()?.navigate(R.id.action_tourist_recover_to_homePage) }

        return binding.root
    }
}
