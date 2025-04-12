package com.luisptapia.rftarea2modulovi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.luisptapia.rftarea2modulovi.application.RTTarea2ModuloVIApp
import com.luisptapia.rftarea2modulovi.data.TroopRepository
import com.luisptapia.rftarea2modulovi.databinding.ActivityMainBinding
import com.luisptapia.rftarea2modulovi.ui.fragments.TroopsListFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repository: TroopRepository

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        repository = (application as RTTarea2ModuloVIApp).repository
//
//
//        lifecycleScope.launch {
//            val troops = repository.getTroops()
//
//            println(troops)
//
//            println("---------------------------------")
//
//            val troop = repository.getTroopDetail("1")
//
//            println(troop)
//
//        }

        //Primera ejecución de la activity
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    TroopsListFragment()
                )
                .commit()
        }

    }
}