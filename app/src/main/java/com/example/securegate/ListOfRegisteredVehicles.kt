package com.example.securegate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.securegate.databinding.ActivityListOfRegisteredVehiclesBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListOfRegisteredVehicles : AppCompatActivity() {
    private lateinit var binding: ActivityListOfRegisteredVehiclesBinding
    lateinit var firebaseFirestore:FirebaseFirestore
    private var carList = ArrayList<carDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfRegisteredVehiclesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()

        carList = arrayListOf()
        binding.rvLorc.setHasFixedSize(true)
        binding.rvLorc.layoutManager = LinearLayoutManager(this)

        firebaseFirestore.collection("Car Details").get().addOnSuccessListener {
         if (!it.isEmpty)
         {
             for (data in it.documents)
             {

                 val details : carDetails? = data.toObject(carDetails::class.java)
                 if (details !=null)
                 {
                     carList.add(details)
                 }
                 binding.rvLorc.adapter = CarDetailsAdapter(carList)
             }
         }

        }


    }
}