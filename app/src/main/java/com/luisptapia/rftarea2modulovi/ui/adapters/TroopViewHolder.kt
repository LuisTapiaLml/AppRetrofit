package com.luisptapia.rftarea2modulovi.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luisptapia.rftarea2modulovi.data.remote.model.TroopDto
import com.luisptapia.rftarea2modulovi.databinding.TroopElementBinding

class TroopViewHolder(
    private val binding: TroopElementBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(troop: TroopDto){

        binding.tvName.text = troop.name

        Glide.with(binding.root.context)
            .load(troop.image)
            .into(binding.ivImage)

    }

}