package com.luisptapia.rftarea2modulovi.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
            }finally {
                binding.pbLoading.visibility = View.GONE
            }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}