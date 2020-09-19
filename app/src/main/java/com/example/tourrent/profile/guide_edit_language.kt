package com.example.tourrent.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideEditLanguageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_guide_edit_language.*

/**
 * A simple [Fragment] subclass.
 */
class guide_edit_language : Fragment() {

    var rootRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideEditLanguageBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_edit_language,
                container,
                false
        )
        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key","")

        if (key != null) {
            calculateButton(key)

            binding.English.setOnClickListener {
                addLanguage(key, "A")
                binding.English.visibility = View.INVISIBLE
                binding.Noenglish.visibility = View.VISIBLE
            }
            binding.Malay.setOnClickListener {
                addLanguage(key, "B")
                binding.Malay.visibility = View.INVISIBLE
                binding.Nomalay.visibility = View.VISIBLE
            }
            binding.Chinese.setOnClickListener {
                addLanguage(key, "C")
                binding.Chinese.visibility = View.INVISIBLE
                binding.Nochinese.visibility = View.VISIBLE
            }
            binding.Hindi.setOnClickListener {
                addLanguage(key, "D")
                binding.Hindi.visibility = View.INVISIBLE
                binding.Nohindi.visibility = View.VISIBLE
            }
            binding.Spanish.setOnClickListener {
                addLanguage(key, "E")
                binding.Spanish.visibility = View.INVISIBLE
                binding.Nospanish.visibility = View.VISIBLE
            }
            binding.French.setOnClickListener {
                addLanguage(key, "F")
                binding.French.visibility = View.INVISIBLE
                binding.Nofrench.visibility = View.VISIBLE
            }
            binding.Arabic.setOnClickListener {
                addLanguage(key, "G")
                binding.Arabic.visibility = View.INVISIBLE
                binding.Noarabic.visibility = View.VISIBLE
            }
            binding.Russian.setOnClickListener {
                addLanguage(key, "H")
                binding.Russian.visibility = View.INVISIBLE
                binding.Norussian.visibility = View.VISIBLE
            }
            binding.Thai.setOnClickListener {
                addLanguage(key, "I")
                binding.Thai.visibility = View.INVISIBLE
                binding.Nothai.visibility = View.VISIBLE
            }
            binding.Jap.setOnClickListener {
                addLanguage(key, "J")
                binding.Jap.visibility = View.INVISIBLE
                binding.Nojap.visibility = View.VISIBLE
            }


            binding.Noenglish.setOnClickListener {
                removeLanguage(key, "A")
                binding.English.visibility = View.VISIBLE
                binding.Noenglish.visibility = View.INVISIBLE
            }
            binding.Nomalay.setOnClickListener {
                removeLanguage(key, "B")
                binding.Malay.visibility = View.VISIBLE
                binding.Nomalay.visibility = View.INVISIBLE
            }
            binding.Nochinese.setOnClickListener {
                removeLanguage(key, "C")
                binding.Chinese.visibility = View.VISIBLE
                binding.Nochinese.visibility = View.INVISIBLE
            }
            binding.Nohindi.setOnClickListener {
                removeLanguage(key, "D")
                binding.Hindi.visibility = View.VISIBLE
                binding.Nohindi.visibility = View.INVISIBLE
            }
            binding.Nospanish.setOnClickListener {
                removeLanguage(key, "E")
                binding.Spanish.visibility = View.VISIBLE
                binding.Nospanish.visibility = View.INVISIBLE
            }
            binding.Nofrench.setOnClickListener {
                removeLanguage(key, "F")
                binding.French.visibility = View.VISIBLE
                binding.Nofrench.visibility = View.INVISIBLE
            }
            binding.Noarabic.setOnClickListener {
                removeLanguage(key, "G")
                binding.Arabic.visibility = View.VISIBLE
                binding.Noarabic.visibility = View.INVISIBLE
            }
            binding.Norussian.setOnClickListener {
                removeLanguage(key, "H")
                binding.Russian.visibility = View.VISIBLE
                binding.Norussian.visibility = View.INVISIBLE
            }
            binding.Nothai.setOnClickListener {
                removeLanguage(key, "I")
                binding.Thai.visibility = View.VISIBLE
                binding.Nothai.visibility = View.INVISIBLE
            }
            binding.Nojap.setOnClickListener {
                removeLanguage(key, "J")
                binding.Jap.visibility = View.VISIBLE
                binding.Nojap.visibility = View.INVISIBLE
            }


            binding.back.setOnClickListener {
                rootRef.child("Guide").child(key).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val guide = dataSnapshot.getValue(Guide::class.java)
                            if (guide != null) {
                                val tags = guide.tags.toString()
                                if (tags.contains("A") || tags.contains("B") || tags.contains("C") || tags.contains(
                                        "D"
                                    ) || tags.contains("E") || tags.contains("F") || tags.contains("G") || tags.contains(
                                        "H"
                                    ) || tags.contains("I") || tags.contains("J")
                                ) {
                                    requireActivity().onBackPressed()
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "Please Select A language",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(activity, "User Doesn't Exist", Toast.LENGTH_SHORT)
                                .show()
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

    private fun addLanguage(Key: String, Code: String){
        rootRef.child("Guide").child(Key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val guide = dataSnapshot.getValue(Guide::class.java)
                    if (guide != null) {
                        var newtag = guide.tags.toString() + Code
                        newtag = newtag.toCharArray().sorted().joinToString("")
                        updateLanguage(Key, newtag)
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

    private fun removeLanguage(Key: String, Code: String){
        rootRef.child("Guide").child(Key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val guide = dataSnapshot.getValue(Guide::class.java)
                    if (guide != null) {
                        var newtag = guide.tags.toString()
                        newtag = newtag.replace(Code, "")
                        updateLanguage(Key, newtag)
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

    private fun updateLanguage(Key: String, newtag: String){
        rootRef.child("Guide").child(Key).child("tags").setValue(newtag)
    }

    private fun calculateButton(key: String){
        rootRef.child("Guide").child(key).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val guide = dataSnapshot.getValue(Guide::class.java)
                    if (guide != null) {
                        setButton(guide.tags.toString())
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

    private fun setButton(tags: String){
        if(tags.contains("A")){
            English.visibility = View.INVISIBLE
            Noenglish.visibility = View.VISIBLE
        }
        else{
            English.visibility = View.VISIBLE
            Noenglish.visibility = View.INVISIBLE
        }
        if(tags.contains("B")){
            Malay.visibility = View.INVISIBLE
            Nomalay.visibility = View.VISIBLE
        }
        else{
            Malay.visibility = View.VISIBLE
            Nomalay.visibility = View.INVISIBLE
        }
        if(tags.contains("C")){
            Chinese.visibility = View.INVISIBLE
            Nochinese.visibility = View.VISIBLE
        }
        else{
            Chinese.visibility = View.VISIBLE
            Nochinese.visibility = View.INVISIBLE
        }
        if(tags.contains("D")){
            Hindi.visibility = View.INVISIBLE
            Nohindi.visibility = View.VISIBLE
        }
        else{
            Hindi.visibility = View.VISIBLE
            Nohindi.visibility = View.INVISIBLE
        }
        if(tags.contains("E")){
            Spanish.visibility = View.INVISIBLE
            Nospanish.visibility = View.VISIBLE
        }
        else{
            Spanish.visibility = View.VISIBLE
            Nospanish.visibility = View.INVISIBLE
        }
        if(tags.contains("F")){
            French.visibility = View.INVISIBLE
            Nofrench.visibility = View.VISIBLE
        }
        else{
            French.visibility = View.VISIBLE
            Nofrench.visibility = View.INVISIBLE
        }
        if(tags.contains("G")){
            Arabic.visibility = View.INVISIBLE
            Noarabic.visibility = View.VISIBLE
        }
        else{
            Arabic.visibility = View.VISIBLE
            Noarabic.visibility = View.INVISIBLE
        }
        if(tags.contains("H")){
            Russian.visibility = View.INVISIBLE
            Norussian.visibility = View.VISIBLE
        }
        else{
            Russian.visibility = View.VISIBLE
            Norussian.visibility = View.INVISIBLE
        }
        if(tags.contains("I")){
            Thai.visibility = View.INVISIBLE
            Nothai.visibility = View.VISIBLE
        }
        else{
            Thai.visibility = View.VISIBLE
            Nothai.visibility = View.INVISIBLE
        }
        if(tags.contains("J")){
            Jap.visibility = View.INVISIBLE
            Nojap.visibility = View.VISIBLE
        }
        else{
            Jap.visibility = View.VISIBLE
            Nojap.visibility = View.INVISIBLE
        }
    }
}
