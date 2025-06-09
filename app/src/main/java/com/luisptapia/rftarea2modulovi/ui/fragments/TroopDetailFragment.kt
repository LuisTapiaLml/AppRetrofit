package com.luisptapia.rftarea2modulovi.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.luisptapia.rftarea2modulovi.R
import com.luisptapia.rftarea2modulovi.application.RTTarea2ModuloVIApp
import com.luisptapia.rftarea2modulovi.data.TroopRepository
import com.luisptapia.rftarea2modulovi.data.remote.model.TroopDto
import com.luisptapia.rftarea2modulovi.databinding.FragmentTroopDetailBinding
import com.luisptapia.rftarea2modulovi.ui.adapters.LevelTroopAdapter
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TroopDetailFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentTroopDetailBinding? = null
    private val binding get() = _binding!!
    private var exoPlayer: ExoPlayer? = null

    private lateinit var repository: TroopRepository
    private lateinit var googleMaps: GoogleMap

    private lateinit var troop:TroopDto

    private var troopid: String? = null

    private var currentLocation = 0

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

                troop = repository.getTroopDetail(troopid)

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

                val mapFragment = SupportMapFragment.newInstance()
                childFragmentManager.beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit()

                mapFragment.getMapAsync(this@TroopDetailFragment)


                binding.btNextLocation.setOnClickListener {

                    currentLocation += 1

                    if (currentLocation > troop.ubicaciones.size -1){
                        currentLocation = 0
                    }

                    var location = troop.ubicaciones[currentLocation]

                    val coordinates = LatLng(location.x, location.y)

                    googleMaps.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(coordinates, 5f ),
                        2_000,
                        null
                    )


                }

                binding.btPreviousLocation.setOnClickListener {

                    currentLocation -=1

                    if (currentLocation < 0){
                        currentLocation = troop.ubicaciones.size -1
                    }

                    var location = troop.ubicaciones[currentLocation]

                    val coordinates = LatLng(location.x, location.y)

                    googleMaps.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(coordinates, 5f ),
                        2_000,
                        null
                    )


                }

            }catch (e:Exception){
                Toast.makeText(requireContext(),getText(R.string.message_detail_error),Toast.LENGTH_LONG).show()

                Log.d("APP_LOGS", e.message.toString())
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


    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        exoPlayer?.release()
        exoPlayer = null
    }


    override fun onMapReady(map: GoogleMap) {

        googleMaps = map
        
        if (troop != null ) {


            val ubicaciones = troop.ubicaciones ?: emptyList()

            for (cord in ubicaciones){
                val coordinates = LatLng(cord.x, cord.y)

                val marker = MarkerOptions()
                    .position(coordinates)
                    .title(cord.nombre)
                    .snippet(cord.descripcion)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_icon))

                googleMaps.addMarker(marker)
            }

        }

        val firstLocation = troop.ubicaciones[0]
        val coordinates = LatLng(firstLocation.x, firstLocation.y)

        googleMaps.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 5f ),
            2000,
            null
        )

    }



    suspend fun getBitmapFromUrl(
        url: String,
        width: Int = 100,  // Default size
        height: Int = 100
    ): BitmapDescriptor? {
        return try {
            val bitmap = Glide.with(this)
                .asBitmap()
                .load(url)
                .apply(RequestOptions().override(width, height))
                .submit()
                .get() // This runs on IO thread due to coroutine

            BitmapDescriptorFactory.fromBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("MapUtils", "Error loading image: ${e.message}")
            null // Return null or fallback icon
        }
    }

}

