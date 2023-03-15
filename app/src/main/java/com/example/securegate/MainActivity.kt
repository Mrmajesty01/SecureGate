package com.example.securegate

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.securegate.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
                }
            }

        binding.cvRv.setOnClickListener{
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
           intent =  Intent(this,RegisterVehicle::class.java)
            startActivity(intent)
        }

        binding.cvRdv.setOnClickListener{
            intent =  Intent(this,ListOfRegisteredVehicles::class.java)
            startActivity(intent)
        }

        binding.cvSqrc.setOnClickListener{
            intent = Intent(this,ScanQRCode::class.java)
            startActivity(intent)
        }


    }
}