package com.example.tourrent.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideRecoverBinding

/**
 * A simple [Fragment] subclass.
 */
class guide_recover : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentGuideRecoverBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_recover,
                container,
                false
        )

        binding.back.setOnClickListener { view?.findNavController()?.navigate(R.id.action_guide_recover_to_homePage) }

        return binding.root
    }
}
