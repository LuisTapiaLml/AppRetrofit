package com.luisptapia.rftarea2modulovi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.luisptapia.rftarea2modulovi.application.RTTarea2ModuloVIApp
import com.luisptapia.rftarea2modulovi.data.TroopRepository
import com.luisptapia.rftarea2modulovi.databinding.ActivityMainBinding
import com.luisptapia.rftarea2modulovi.ui.fragments.TroopsListFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var splashScreen =  installSplashScreen()


        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        splashScreen.setKeepOnScreenCondition{true}

        val intent = Intent(this, HomeScreenActivity::class.java)

        startActivity(intent)

        finish()


    }
}