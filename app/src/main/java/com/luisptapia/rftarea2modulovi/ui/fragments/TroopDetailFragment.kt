package com.luisptapia.rftarea2modulovi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.luisptapia.rftarea2modulovi.R
import com.luisptapia.rftarea2modulovi.application.RTTarea2ModuloVIApp
import com.luisptapia.rftarea2modulovi.data.TroopRepository
import com.luisptapia.rftarea2modulovi.databinding.FragmentTroopDetailBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TroopDetailFragment : Fragment() {

    private var _binding: FragmentTroopDetailBinding? = null
    private val binding get() = _binding!!

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

                    Glide.with(requireActivity())
                        .load(troop.image)
                        .into(ivTroopImage)
                }



            }catch (e:Exception){

            }finally {

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
}