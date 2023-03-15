package com.example.securegate

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.securegate.databinding.ActivityViewAndUpdateBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import javax.annotation.Nullable

class viewAndUpdate : AppCompatActivity() {
    private lateinit var binding : ActivityViewAndUpdateBinding
    var string: String? = null
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAndUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        var bundle:Bundle? = intent.extras
        if (bundle!=null) {
           string = bundle.getString("String")
        }
        else
        {

        }

        firestore.collection("Car Details").whereEqualTo("plateNumber",string).addSnapshotListener(object :
            EventListener<QuerySnapshot?> {

            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                for (item in value?.getDocuments()!!) {


                    var name: String? = item.getString("ownerName")
                    var id: String? = item.getString("ownerId")
                    var carName: String? = item.getString("carName")
                    var carModel: String? = item.getString("carModel")
                    var carColor: String? = item.getString("carColor")
                    var plateNumber: String? = item.getString("plateNumber")
                    var uri: String? = item.getString("uri")

                    binding.ownerNameVu.setText(name)
                    binding.ownerIdVu.setText(id)
                    binding.carNameVu.setText(carName)
                    binding.carModelVu.setText(carModel)
                    binding.carColorVu.setText(carColor)
                    binding.plateNoVu.setText(plateNumber)
                    Picasso.get().load(uri).into(binding.ivVu)

                }
            }


        })




    }
}