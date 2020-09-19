package com.example.tourrent.register

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.tourrent.R
import com.example.tourrent.databinding.FragmentGuideSetup1Binding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_guide_setup1.*
import java.io.IOException

class guide_setup1 : Fragment() {

    private var doubleBackToExitPressedOnce = false
    private val storageRef = FirebaseStorage.getInstance().reference
    private val PICK_IMAGE = 1
    private var uri: Uri? = null
    private var key: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuideSetup1Binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guide_setup1,
                container,
                false
        )

        val prefs = requireActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE)
        key = prefs.getString("Key","")

        binding.upload.setOnClickListener {
            selectPhoto()
        }

        binding.next.setOnClickListener {
            if(uri == null) {
                Toast.makeText(activity, "No Image Uploaded", Toast.LENGTH_SHORT).show()
            }
            else{
                val storageRef = FirebaseStorage.getInstance().reference
                storageRef.child("$key").downloadUrl.addOnSuccessListener {
                    view?.findNavController()?.navigate(R.id.action_guide_setup1_to_guide_setup2)
                }.addOnFailureListener {
                    Toast.makeText(activity, "Uploading Image", Toast.LENGTH_LONG).show()
                }
            }
        }
        return binding.root
    }

    private fun selectPhoto(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            uri = data.data as Uri
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, uri!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    image.visibility = View.VISIBLE
                    image.setImageBitmap(bitmap)
                    uploadImage()
                } else {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                        image.visibility = View.VISIBLE
                        image.setImageBitmap(bitmap)
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
