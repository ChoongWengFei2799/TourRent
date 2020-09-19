package com.example.tourrent.search

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTouristSearchBinding
import kotlinx.android.synthetic.main.fragment_tourist_search.*

/**
 * A simple [Fragment] subclass.
 */
class tourist_search : Fragment() {

    private var tags = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTouristSearchBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tourist_search,
                container,
                false
        )

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.button.setOnClickListener {
            locationSpinner()
        }

        binding.search.setOnClickListener {
            calculateTag()
            val location = binding.location.text.toString()
            val action = tourist_searchDirections.actionTouristSearchToSearchList(location, tags)
            view?.findNavController()?.navigate(action)
        }

        return binding.root
    }

    private fun calculateTag(){
        tags = ""

        if(hiking.isChecked){
            tags += "1"
        }
        if(cycle.isChecked){
            tags += "2"
        }
        if(horse.isChecked){
            tags += "3"
        }
        if(spelunking.isChecked){
            tags += "4"
        }
        if(sail.isChecked){
            tags += "5"
        }
        if(dive.isChecked){
            tags += "6"
        }

        if(eng.isChecked){
            tags += "A"
        }
        if(bm.isChecked){
            tags += "B"
        }
        if(cn.isChecked){
            tags += "C"
        }
        if(hindi.isChecked){
            tags += "D"
        }
        if(spanish.isChecked){
            tags += "E"
        }
        if(checkBox21.isChecked){
            tags += "F"
        }
        if(arabic.isChecked){
            tags += "G"
        }
        if(russian.isChecked){
            tags += "H"
        }
        if(thai.isChecked){
            tags += "I"
        }
        if(japan.isChecked){
            tags += "J"
        }
    }

    private fun locationSpinner(){
        val b: AlertDialog.Builder = AlertDialog.Builder(activity)
        b.setTitle("Location")
        val location = arrayOf("All", "Kuala Lumpur", "Putrajaya", "Negeri Sembilan", "Selangor", "Melacca", "Johor", "Pahang", "Perak", "Kedah", "Perlis", "Penang", "Terengannu", "Kelantan", "Sabah", "Sarawak")
        b.setItems(location) { dialog, which ->
            dialog.dismiss()
            when (which) {
                0 ->  updateLocation("All")
                1 ->  updateLocation("Kuala Lumpur")
                2 ->  updateLocation("Putrajaya")
                3 ->  updateLocation("Negeri Sembilan")
                4 ->  updateLocation("Selangor")
                5 ->  updateLocation("Melacca")
                6 ->  updateLocation("Johor")
                7 ->  updateLocation("Pahang")
                8 ->  updateLocation("Perak")
                9 ->  updateLocation("Kedah")
                10 -> updateLocation("Perlis")
                11 -> updateLocation("Penang")
                12 -> updateLocation("Terengannu")
                13 -> updateLocation("Kelantan")
                14 -> updateLocation("Sabah")
                15 -> updateLocation("Sarawak")
            }
        }
        b.show()
    }

    private fun updateLocation(newloc: String){
        location.text = newloc
    }
}
