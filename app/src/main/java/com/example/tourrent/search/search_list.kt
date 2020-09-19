package com.example.tourrent.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourrent.Dclass.Guide
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentSearchListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search_list.*

class search_list : Fragment() {

    private val args: search_listArgs by navArgs()
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSearchListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search_list,
            container,
            false
        )

        linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = linearLayoutManager

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val location = args.location
        val ftags = args.tags

        if(location == "All") {
            val ref = FirebaseDatabase.getInstance().reference.child("Guide")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(p0: DataSnapshot) {
                    val guideList = ArrayList<Guide>()
                    val key = ArrayList<String>()
                    p0.children.forEach {
                        var check = true
                        val guide = it.getValue(Guide::class.java)
                        ftags.forEach {search ->
                            if (!guide!!.tags!!.contains(search)) {
                                check = false
                            }
                        }
                        if(check) {
                            guideList.add(guide!!)
                            key.add(it.key!!)
                        }
                    }

                    if(guideList.isNullOrEmpty()){
                        binding.warning.visibility = View.VISIBLE
                    }
                    else {
                        binding.warning.visibility = View.GONE
                        binding.recyclerView.adapter = SearchRecycleAdapter(guideList, key)
                    }
                }
            })
        }
        else {
            val ref = FirebaseDatabase.getInstance().reference.child("Guide").orderByChild("location").equalTo(location)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(p0: DataSnapshot) {
                    val guideList = ArrayList<Guide>()
                    val key = ArrayList<String>()
                    p0.children.forEach {
                        var check = true
                        val guide = it.getValue(Guide::class.java)
                        ftags.forEach {search ->
                            if (!guide!!.tags!!.contains(search)) {
                                check = false
                            }
                        }
                        if(check) {
                            guideList.add(guide!!)
                            key.add(it.key!!)
                        }
                    }

                    if(guideList.isNullOrEmpty()){
                        binding.warning.visibility = View.VISIBLE
                    }
                    else {
                        binding.warning.visibility = View.GONE
                        binding.recyclerView.adapter = SearchRecycleAdapter(guideList, key)
                    }
                }
            })
        }

        return binding.root
    }
}