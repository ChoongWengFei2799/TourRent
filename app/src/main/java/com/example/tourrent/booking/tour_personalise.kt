package com.example.tourrent.booking

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tourrent.Dclass.Booking
import com.example.tourrent.Dclass.Personalize
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentTourPersonaliseBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_tour_personalise.*
import java.text.SimpleDateFormat
import java.util.*

class tour_personalise : Fragment() {

    private val args: tour_personaliseArgs by navArgs()

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Booking")
    var myRef2 = database.getReference("Personalize")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentTourPersonaliseBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tour_personalise,
            container,
            false
        )

        val gkey = args.key
        val location = args.location

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.startdate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                binding.startdate.setText("$dayOfMonth/${monthOfYear+1}/$year")

            }, year, month, day)

            val cmin = Calendar.getInstance()
            cmin.add(Calendar.DATE, 1)

            dpd.datePicker.minDate = cmin.timeInMillis
            dpd.show()
        }

        binding.enddate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                binding.enddate.setText("$dayOfMonth/${monthOfYear+1}/$year")

            }, year, month, day)

            val cmin = Calendar.getInstance()
            cmin.add(Calendar.DATE, 1)

            dpd.datePicker.minDate = cmin.timeInMillis
            dpd.show()
        }

        binding.booking.setOnClickListener {
            if(binding.startdate.text.toString() != "" && binding.enddate.text.toString() != "" && binding.number.text.toString() != "") {
                val format = SimpleDateFormat("dd/MM/yyyy")
                val start: Date = format.parse(binding.startdate.text.toString())
                val end: Date = format.parse(binding.enddate.text.toString())
                if (start.before(end)){
                    val id = myRef.push().key

                    val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
                    val tkey = prefs.getString("Key","")
                    var type = ""
                    type = if(switch1.isChecked){
                        "VP"
                    } else{
                        "OP"
                    }

                    val book = Booking(tkey, gkey, type, binding.startdate.text.toString(), binding.enddate.text.toString(), location)

                    var interest = binding.interest.text.toString()
                    var fav = binding.fav.text.toString()
                    if (interest == ""){
                        interest = "None"
                    }
                    if (fav == ""){
                        fav = "None"
                    }

                    val per = Personalize(interest, fav, binding.number.text.toString())

                    if (id != null) {
                        myRef.child(id).setValue(book)
                        myRef2.child(id).setValue(per)
                        Toast.makeText(activity, "Booking Created", Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigate(R.id.action_tour_personalise_to_booking_list)
                    }
                }
                else{
                    Toast.makeText(activity, "Invalid End Date", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(activity, "Please Fill in Required Fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


}
