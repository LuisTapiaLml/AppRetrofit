package com.luisptapia.rftarea2modulovi.application

import android.app.Application
import com.luisptapia.rftarea2modulovi.data.TroopRepository
import com.luisptapia.rftarea2modulovi.data.remote.RetrofitHelper

class RTTarea2ModuloVIApp: Application() {

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        TroopRepository(retrofit)
    }

}