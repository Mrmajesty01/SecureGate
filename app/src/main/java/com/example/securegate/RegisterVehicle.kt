package com.example.securegate

import android.R.attr.bitmap
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.securegate.databinding.ActivityRegisterVehicleBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.zxing.WriterException
import java.io.ByteArrayOutputStream


class RegisterVehicle : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var binding: ActivityRegisterVehicleBinding
    private lateinit var storageRef: StorageReference
    var boolean:Boolean = true
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)



        storageRef = FirebaseStorage.getInstance().reference.child("CarImages")
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.cvRvRv.setOnClickListener {

            if(boolean.equals(false)) {
                relaunch.launch("image/*")
            }

            else {

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                } catch (e: ActivityNotFoundException) {

                }
            }


        }

        binding.btnRv.setOnClickListener {

            upload()
            btn()
        }


    }

    private var relaunch = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.ivRv.setImageURI(it)
    }

    private fun upload() {
        try {
        storageRef = storageRef.child(System.currentTimeMillis().toString())
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->

                        val Map = hashMapOf(
                            "OwnerName" to binding.ownerName.text.toString(),
                            "Owner Id" to binding.ownerId.text.toString(),
                            "Car Name" to binding.carNameRv.text.toString(),
                            "Car Model" to binding.carMdelRv.text.toString(),
                            "Car Color" to binding.carColorRv.text.toString(),
                            "Plate Number" to binding.plateNoRv.text.toString(),
                            "URI" to uri.toString()
                        )
                        firebaseFirestore.collection("CarDetails").add(Map)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Uploaded Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        firestoreTask.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                binding.ivRv.setImageURI(null)
                                binding.ownerName.setText("")
                                binding.ownerId.setText("")
                                binding.carNameRv.setText("")
                                binding.carMdelRv.setText("")
                                binding.carColorRv.setText("")
                                binding.plateNoRv.setText("")

                            }

                    }
                } else {

                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
                }
            }
        }
        catch (e: WriterException){

        }
    }



    private fun btn() {

        try {
            binding.btnRv.setOnClickListener(View.OnClickListener {

                try {
                    if (!TextUtils.isEmpty(binding.ownerName.text.toString()) && !TextUtils.isEmpty(
                            binding.ownerId.text.toString()
                        ) &&
                        !TextUtils.isEmpty(binding.carNameRv.text.toString()) && !TextUtils.isEmpty(
                            binding.carMdelRv.text.toString()
                        ) &&
                        !TextUtils.isEmpty(binding.carColorRv.text.toString()) && !TextUtils.isEmpty(
                            binding.plateNoRv.text.toString()
                        )
                    ) {

                        binding.ivRv.isDrawingCacheEnabled = true
                        binding.ivRv.buildDrawingCache()
                        val bitmap = (binding.ivRv.drawable as BitmapDrawable).bitmap
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        val urlTask: Task<Uri> =
                            storageRef.putBytes(data).continueWithTask { task ->
                                if (!task.isSuccessful()) {
                                    Toast.makeText(
                                        this,
                                        task.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                storageRef.getDownloadUrl()
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful()) {
                                    val downloadUri: Uri = task.getResult()
                                    val uri = downloadUri.toString()

                                    val details = hashMapOf(
                                        "ownerName" to binding.ownerName.text.toString(),
                                        "ownerId" to binding.ownerId.text.toString(),
                                        "carName" to binding.carNameRv.text.toString(),
                                        "carModel" to binding.carMdelRv.text.toString(),
                                        "carColor" to binding.carColorRv.text.toString(),
                                        "plateNumber" to binding.plateNoRv.text.toString(),
                                        "uri" to uri

                                    )

                                    firebaseFirestore.collection("Car Details").add(details)
                                        .addOnSuccessListener {
                                            intent = Intent(this, GenerateQrCode::class.java)
                                            intent.putExtra(
                                                "plateNumber",
                                                binding.plateNoRv.text.toString()
                                            )
                                            startActivity(intent)
                                            binding.ivRv.invalidate()
                                            binding.ivRv.setImageBitmap(null)
                                            binding.ownerName.setText("")
                                            binding.ownerId.setText("")
                                            binding.carNameRv.setText("")
                                            binding.carMdelRv.setText("")
                                            binding.carColorRv.setText("")
                                            binding.plateNoRv.setText("")
                                        }.addOnFailureListener { e ->
                                            Toast.makeText(
                                                this,
                                                e.message.toString(),
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    Toast.makeText(
                                        this,
                                        "Details Added Successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Details Not Added Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }


                    } else {
                        Toast.makeText(this, "Please input all fields", Toast.LENGTH_SHORT).show();

                    }
                }
                catch (e: WriterException){

                }
            }
            )
        }
        catch (e : WriterException){

        }
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                val imageBitmap: Bitmap = data?.extras?.get("data") as Bitmap
                binding.ivRv.setImageBitmap(imageBitmap)
            }
        }


    }



