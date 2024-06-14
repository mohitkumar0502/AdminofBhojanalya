package com.example.adminofbhojanalya.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminofbhojanalya.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val CustomerNames: MutableList<String>,
    private val moneyStatus: MutableList<Boolean>
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding =
            DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int = CustomerNames.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                CustomerName.text = CustomerNames[position]
                if (moneyStatus[position] == true) {
                    NotReceived.text = "Received"
                } else {
                    NotReceived.text = "NotReceived"
                }

                val colorMap = mapOf(
                    true to Color.GREEN, false to Color.RED,
                )
                NotReceived.setTextColor(colorMap[moneyStatus[position]] ?: Color.BLACK)
                StatusColor.backgroundTintList =
                    ColorStateList.valueOf(colorMap[moneyStatus[position]] ?: Color.BLACK)
            }
        }

    }

}