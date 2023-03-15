package com.example.securegate

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.securegate.databinding.ActivityGenerateQrCodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatReader
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.IOException

class GenerateQrCode : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateQrCodeBinding
    private lateinit var printBT: PrintBluetooth
    private var string: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGenerateQrCodeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        printBT = PrintBluetooth()
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            string = bundle.getString("plateNumber")
            }
        else
        {
            Toast.makeText(this,"bundle is empty",Toast.LENGTH_SHORT).show()
        }

        binding.printBtn.setOnClickListener {

            if (!TextUtils.isEmpty(binding.printerId.text.toString())) {
                PrintBluetooth.printer_id = binding.printerId.text.toString()
                var qrBitmap: Bitmap = printQrCode(string.toString())
                try {
                    printBT.findBT()
                    printBT.openBT()
                    printBT.printQrCode(qrBitmap)
                    printBT.closeBT()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else {

                Toast.makeText(this,"please input printer Id",Toast.LENGTH_SHORT).show()
            }
        }

        val writer = QRCodeWriter()

        try {
           val bitMatrix = writer.encode(string,BarcodeFormat.QR_CODE,512,512)
           val width = bitMatrix.width
           val height = bitMatrix.height
           val bmp = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
           for(x in 0 until width)
           {
               for (y in 0 until height)
               {
                   bmp.setPixel(x,y, if (bitMatrix[x,y]) Color.BLACK else Color.WHITE )

               }
           }
            binding.qrcode.setImageBitmap(bmp)
        } catch (e: WriterException) {

        }

    }

    private fun printQrCode(qrCode: String) : Bitmap {

            var multiFormaterWriter = MultiFormatWriter()
            var bitMatrix: BitMatrix = multiFormaterWriter.encode(qrCode, BarcodeFormat.QR_CODE, 300, 300)
            var barCodeEncode = BarcodeEncoder()
            var bitmap: Bitmap = barCodeEncode.createBitmap(bitMatrix)
            return bitmap

        }
    }
