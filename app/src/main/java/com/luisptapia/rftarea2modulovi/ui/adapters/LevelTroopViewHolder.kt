package com.luisptapia.rftarea2modulovi.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luisptapia.rftarea2modulovi.R
import com.luisptapia.rftarea2modulovi.data.remote.model.NivelTropaDto
import com.luisptapia.rftarea2modulovi.data.remote.model.TroopDto
import com.luisptapia.rftarea2modulovi.databinding.LevelElementBinding
import com.luisptapia.rftarea2modulovi.databinding.TroopElementBinding

class LevelTroopViewHolder(
    private val binding: LevelElementBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(level:NivelTropaDto ){

        var firstLevel = level.niveles[0]
        var lastLevel = level.niveles.last()


        if( level.niveles.size == 1 ){
            binding.tvLevel.text = firstLevel
        }else{
            binding.tvLevel.text = binding.root.context.getString(R.string.levels_text, firstLevel, lastLevel)

        }

        Glide.with(binding.root.context)
            .load(level.imagen)
            .into(binding.ivTroopLevel)

    }

}