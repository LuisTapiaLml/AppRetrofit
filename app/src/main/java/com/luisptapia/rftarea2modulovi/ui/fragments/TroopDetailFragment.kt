package com.luisptapia.rftarea2modulovi.ui.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.luisptapia.rftarea2modulovi.R
import com.luisptapia.rftarea2modulovi.application.RTTarea2ModuloVIApp
import com.luisptapia.rftarea2modulovi.data.TroopRepository
import com.luisptapia.rftarea2modulovi.databinding.FragmentTroopDetailBinding
import com.luisptapia.rftarea2modulovi.ui.adapters.LevelTroopAdapter
import com.luisptapia.rftarea2modulovi.ui.adapters.TroopsAdapter
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TroopDetailFragment : Fragment() {

    private var _binding: FragmentTroopDetailBinding? = null
    private val binding get() = _binding!!
    private var exoPlayer: ExoPlayer? = null

    private lateinit var repository: TroopRepository

    private var troopid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            troopid = args.getString("ARG_TROOPID")
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTroopDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as RTTarea2ModuloVIApp).repository


        lifecycleScope.launch {
            try {

                var troop = repository.getTroopDetail(troopid)

                binding.apply {

                    tvName.text = troop.name

                    tvTypeTroop.text = troop.tipo_tropa
                    tvTarget.text = troop.estadisticas.objetivo_preferido
                    tvDamage.text = troop.estadisticas.tipo_dano
                    tvSpace.text = troop.estadisticas.espacio_vivienda
                    tvSpeed.text = troop.estadisticas.velocidad_movimiento
                    tvAttackSpeed.text = troop.estadisticas.velocidad_ataque
                    tvRange.text = troop.estadisticas.rango

                    Glide.with(requireActivity())
                        .load(troop.image)
                        .into(ivTroopImage)

                    rvLevelsContainer.apply {
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                        adapter = LevelTroopAdapter(troop.niveles)
                    }
                }

                exoPlayer = ExoPlayer.Builder(requireContext()).build().also { exo ->
                    binding.exoView.player = exo
                    val videoUri =troop.video.toUri()
                    exo.setMediaItem(MediaItem.fromUri(videoUri))
                    exo.prepare()
                    exo.playWhenReady = true
                }

            }catch (e:Exception){
                Toast.makeText(requireContext(),getText(R.string.message_detail_error),Toast.LENGTH_LONG).show()
            }finally {
                binding.pbLoading.visibility = View.GONE
            }
        }

    }


    companion object {

        @JvmStatic
        fun newInstance(troopid: String) =
            TroopDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("ARG_TROOPID", troopid)

                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        exoPlayer?.release()
        exoPlayer = null
    }

}