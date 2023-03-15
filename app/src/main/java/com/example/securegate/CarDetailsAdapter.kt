package com.example.securegate

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AbsListView.RecyclerListener
import androidx.recyclerview.widget.RecyclerView
import com.example.securegate.databinding.DetailsitemsBinding
import com.squareup.picasso.Picasso

class CarDetailsAdapter(private var carList: ArrayList<carDetails>) : RecyclerView.Adapter<CarDetailsAdapter.carDetailsViewHolder>() {

    inner class carDetailsViewHolder(var binding: DetailsitemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): carDetailsViewHolder {

        val binding = DetailsitemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  carDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(holder: carDetailsViewHolder, position: Int) {
        holder.binding.carNameId.text = carList[position].carName
        holder.binding.carModelDetails.text = carList[position].carModel
        holder.binding.ownersIdDetails.text = carList[position].ownerId
        holder.binding.ownersNameDetails.text = carList[position].ownerName
        holder.binding.plateNumberDetails.text = carList[position].plateNumber
        var uri : String? = carList[position].uri
        Picasso.get().load(uri).into(holder.binding.ivDetails)


    }
}