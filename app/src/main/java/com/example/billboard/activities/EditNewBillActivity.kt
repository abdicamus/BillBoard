package com.example.billboard.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.billboard.R
import com.example.billboard.databinding.ActivityNewAdBinding
import com.example.billboard.dialogs.SpinnerDialogHelper

class EditNewBillActivity : AppCompatActivity() {

    val spinnerDialogHelper = SpinnerDialogHelper()
    lateinit var binding: ActivityNewAdBinding
    lateinit var countryTitle: String
    lateinit var launcher: ActivityResultLauncher<Intent>

    companion object {
        private const val CAMERA_PERMISSION_CODE = 999
        private const val CAMERA_REQUEST = 111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAdBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCamera.setOnClickListener {
            cameraPermission()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
//                val thumbNail: Bitmap = it.data!!.extras!!.getString("data") as Bitmap
                binding.imageView.setImageResource(R.drawable.ic_launcher_background)
            }
        }

    }

    private fun cameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            launcher.launch(cameraIntent)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                launcher.launch(intent)
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == CAMERA_REQUEST) {
//                val thumbNail: Bitmap = data!!.extras!!.getString("data") as Bitmap
//                binding.imageView.setImageBitmap(thumbNail)
//            }
//        }
//    }

}