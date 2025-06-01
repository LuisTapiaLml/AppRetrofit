package com.luisptapia.rftarea2modulovi

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luisptapia.rftarea2modulovi.data.TroopRepository
import com.luisptapia.rftarea2modulovi.databinding.ActivityHomeScreenBinding
import com.luisptapia.rftarea2modulovi.databinding.ActivityMainBinding
import com.luisptapia.rftarea2modulovi.ui.fragments.TroopsListFragment

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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