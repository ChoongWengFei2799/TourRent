package com.example.tourrent.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.tourrent.MainActivity
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideProfileBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_guide_profile.*
import java.io.IOException

class guide_profile : Fragment() {

    private val storageRef = FirebaseStorage.getInstance().reference
    var rootRef = FirebaseDatabase.getInstance().reference
    private lateinit var  viewModel: guide_profile_view_model
    private val PICK_IMAGE = 1
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideProfileBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_profile,
                container,
                false
        )

        viewModel = ViewModelProvider(this).get(guide_profile_view_model::class.java)

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key", "")
        if (key != null) {
            setProfilePic(key)

            viewModel.setInfo(key)

            viewModel.price.observe(viewLifecycleOwner, Observer { price ->
                binding.sprice.text = "RM$price/Day"
            })
            viewModel.name.observe(viewLifecycleOwner, Observer { name ->
                binding.textView33.text = "$name"
            })
            viewModel.location.observe(viewLifecycleOwner, Observer { location ->
                binding.location.text = "$location"
            })
            viewModel.bio.observe(viewLifecycleOwner, Observer { bio ->
                binding.sedittext.text = "$bio"
            })
            viewModel.tags.observe(viewLifecycleOwner, Observer { tags ->
                updateTag(tags)
            })

            binding.editpic.setOnClickListener { selectPhoto() }
            binding.editlocation.setOnClickListener { locationSpinner() }
            binding.editlanguage.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_guide_profile_to_guide_edit_language)
            }
            binding.edittag.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_guide_profile_to_guide_edit_tags)
            }
            binding.editprice.setOnClickListener {
                binding.editprice.visibility = View.INVISIBLE
                binding.saveprice.visibility = View.VISIBLE
                binding.sprice.visibility = View.INVISIBLE

                binding.price.visibility = View.VISIBLE
                binding.price.isEnabled = true
                binding.price.requestFocus()
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.price, InputMethodManager.SHOW_IMPLICIT)
                binding.price.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        saveprice.callOnClick()
                    }
                    false
                }
                binding.saveprice.setOnClickListener {
                    if(price.text.toString() != ""){
                        rootRef.child("Guide").child(key).child("price").setValue(price.text.toString())
                        Toast.makeText(activity, "Price Updated Successfully", Toast.LENGTH_SHORT).show()
                        val newPrice = binding.price.text
                        binding.sprice.text = "RM $newPrice/Day"
                        binding.price.text.clear()
                    }
                    else{
                        Toast.makeText(activity, "Price Update Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    binding.price.isEnabled = false
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                    binding.editprice.visibility = View.VISIBLE
                    binding.saveprice.visibility = View.INVISIBLE
                    binding.price.visibility = View.INVISIBLE
                    binding.sprice.visibility = View.VISIBLE
                }
            }
            binding.editbio.setOnClickListener {
                binding.editbio.visibility = View.INVISIBLE
                binding.savebio.visibility = View.VISIBLE
                binding.sedittext.visibility = View.INVISIBLE

                binding.edittext.visibility = View.VISIBLE
                binding.edittext.isEnabled = true
                binding.edittext.requestFocus()
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.edittext, InputMethodManager.SHOW_IMPLICIT)

                binding.savebio.setOnClickListener {
                    if(edittext.text.toString() != "" ){
                        rootRef.child("Guide").child(key).child("bio").setValue(edittext.text.toString())
                        Toast.makeText(activity, "Bio Updated Successfully", Toast.LENGTH_SHORT).show()
                        val newBio = binding.edittext.text
                        binding.sedittext.text = "$newBio"
                        binding.edittext.text.clear()
                    }
                    else{
                        Toast.makeText(activity, "Bio Update Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    binding.edittext.isEnabled = false
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                    binding.editbio.visibility = View.VISIBLE
                    binding.savebio.visibility = View.INVISIBLE
                    binding.edittext.visibility = View.INVISIBLE
                    binding.sedittext.visibility = View.VISIBLE
                }
            }
            binding.pass.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_guide_profile_to_tourist_edit_password)
            }
            binding.hist.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_guide_profile_to_booking_history)
            }
            binding.Logout.setOnClickListener {
                val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("Mode", "")
                editor.apply()

                editor.putString("Key", "")
                editor.apply()
                val intent = Intent(activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                requireActivity().startActivity(intent)
            }
        }
        return binding.root
    }

    private fun setProfilePic(key:String) {
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child(key).downloadUrl.addOnSuccessListener {
            if (usericon != null) {
                Glide.with(requireContext()).load(it).into(usericon)
            }
        }
    }

    private fun updateTag(tags: String){
        var lantext = ""

        if(tags.contains("A")){
            lantext += "English, "
        }
        if(tags.contains("B")){
            lantext += "Malay, "
        }
        if(tags.contains("C")){
            lantext += "Chinese, "
        }
        if(tags.contains("D")){
            lantext += "Hindi, "
        }
        if(tags.contains("E")){
            lantext += "Spanish, "
        }
        if(tags.contains("F")){
            lantext += "French, "
        }
        if(tags.contains("G")){
            lantext += "Arabic, "
        }
        if(tags.contains("H")){
            lantext += "Russian, "
        }
        if(tags.contains("I")){
            lantext += "Thai, "
        }
        if(tags.contains("J")){
            lantext += "Japanese, "
        }
        lang.text = lantext.dropLast(2)

        var tagtext = ""
        if(tags.contains("1")){
            tagtext += "Hiking, "
        }
        if(tags.contains("2")){
            tagtext += "Cycling, "
        }
        if(tags.contains("3")){
            tagtext += "Horse-Riding, "
        }
        if(tags.contains("4")){
            tagtext += "Spelunking, "
        }
        if(tags.contains("5")){
            tagtext += "Sailing, "
        }
        if(tags.contains("6")){
            tagtext += "Diving, "
        }
        if(tagtext == ""){
            tagtext += "None"
            special.text = tagtext
        }
        else{
            special.text = tagtext.dropLast(2)
        }
    }

    private fun locationSpinner(){
        val b: AlertDialog.Builder = AlertDialog.Builder(activity)
        b.setTitle("Location")
        val location = arrayOf("Kuala Lumpur", "Putrajaya", "Negeri Sembilan", "Selangor", "Melacca", "Johor", "Pahang", "Perak", "Kedah", "Perlis", "Penang", "Terengannu", "Kelantan", "Sabah", "Sarawak")
        b.setItems(location) { dialog, which ->
            dialog.dismiss()
            when (which) {
                0 ->  updateLocation("Kuala Lumpur")
                1 ->  updateLocation("Putrajaya")
                2 ->  updateLocation("Negeri Sembilan")
                3 ->  updateLocation("Selangor")
                4 ->  updateLocation("Melacca")
                5 ->  updateLocation("Johor")
                6 ->  updateLocation("Pahang")
                7 ->  updateLocation("Perak")
                8 ->  updateLocation("Kedah")
                9 ->  updateLocation("Perlis")
                10 -> updateLocation("Penang")
                11 -> updateLocation("Terengannu")
                12 -> updateLocation("Kelantan")
                13 -> updateLocation("Sabah")
                14 -> updateLocation("Sarawak")
            }
        }
        b.show()
    }

    private fun updateLocation(newloc :String){
        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val key = prefs.getString("Key","")
        if(newloc != "") {
            if (key != null) {
                rootRef.child("Guide").child(key).child("location").setValue(newloc)
                Toast.makeText(activity, "Location Updated Successfully", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(activity, "No Location Selected", Toast.LENGTH_SHORT).show()
        }

        location.text = newloc
    }

    private fun selectPhoto(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            uri = data.data as Uri
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, uri!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    usericon.setImageBitmap(bitmap)
                    uploadImage()
                } else {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    usericon.setImageBitmap(bitmap)
                    uploadImage()
                }
            } catch (e: IOException) {
                Toast.makeText(activity, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(){
        if (uri == null) {
            Toast.makeText(activity, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
        else{
            val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
            val key = prefs.getString("Key", "")
            val ref = storageRef.child("$key")
            Toast.makeText(activity, "Uploading", Toast.LENGTH_SHORT).show()
            ref.putFile(uri!!).addOnSuccessListener {
                Toast.makeText(activity, "Upload Success", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(activity, "Upload Fail", Toast.LENGTH_SHORT).show()
                }

        }
    }
}
