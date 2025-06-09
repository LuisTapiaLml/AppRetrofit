package com.luisptapia.rftarea2modulovi.ui.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.luisptapia.rftarea2modulovi.R
import com.luisptapia.rftarea2modulovi.application.RTTarea2ModuloVIApp
import com.luisptapia.rftarea2modulovi.data.TroopRepository
import com.luisptapia.rftarea2modulovi.databinding.FragmentTroopsListBinding
import com.luisptapia.rftarea2modulovi.ui.adapters.TroopsAdapter
import kotlinx.coroutines.launch


class TroopsListFragment : Fragment() {


    private var _binding: FragmentTroopsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TroopRepository

    private var mediaPlayer: MediaPlayer? =null

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTroopsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as RTTarea2ModuloVIApp).repository

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.coc_audio)

        mediaPlayer?.start()
        firebaseAuth = FirebaseAuth.getInstance()

        lifecycleScope.launch {

            try {
                val troops = repository.getTroops()


                binding.rvTroops.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = TroopsAdapter(troops){selectedTroop ->

                        selectedTroop.id?.let{ id ->

                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.fragment_container,
                                    TroopDetailFragment.newInstance(id)
                                )
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }

            }catch (e:Exception){
                Toast.makeText(requireContext(),getText(R.string.message_list_error),Toast.LENGTH_LONG).show()
                Log.d("LOGS_APP",e.message.toString())
            }finally {
                binding.pbLoading.visibility = View.GONE
            }

        }

        binding.btLogout.setOnClickListener {
            firebaseAuth.signOut()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    LoginFragment()
                )
                .commit()

            Toast.makeText(requireContext(),"Sesión cerrada exitosamente",Toast.LENGTH_LONG).show()

        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TroopsListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer = null
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

}