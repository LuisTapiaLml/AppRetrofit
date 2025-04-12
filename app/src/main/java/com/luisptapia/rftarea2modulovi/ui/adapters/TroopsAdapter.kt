package com.luisptapia.rftarea2modulovi.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisptapia.rftarea2modulovi.data.remote.model.TroopDto
import com.luisptapia.rftarea2modulovi.databinding.TroopElementBinding

class TroopsAdapter(
    private val troops: List<TroopDto>,
    private val onTroopClick: (TroopDto) -> Unit
): RecyclerView.Adapter<TroopViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TroopViewHolder {
        val binding = TroopElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TroopViewHolder(binding)
    }

    override fun getItemCount(): Int = troops.size

    override fun onBindViewHolder(holder: TroopViewHolder, position: Int) {
        val troop = troops[position]

        holder.bind(troop)

        //Para el click
        holder.itemView.setOnClickListener {
            onTroopClick(troop)
        }
    }
}