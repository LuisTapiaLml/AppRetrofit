package com.luisptapia.rftarea2modulovi.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisptapia.rftarea2modulovi.data.remote.model.NivelTropaDto
import com.luisptapia.rftarea2modulovi.databinding.LevelElementBinding


class LevelTroopAdapter(
    private val levels: List<NivelTropaDto> ,
) : RecyclerView.Adapter<LevelTroopViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelTroopViewHolder {
        val binding = LevelElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LevelTroopViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return  levels.size
    }

    override fun onBindViewHolder(holder: LevelTroopViewHolder, position: Int) {
        val level = levels[position]

        holder.bind(level)
    }


}