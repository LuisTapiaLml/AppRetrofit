package com.luisptapia.rftarea2modulovi.data

import com.luisptapia.rftarea2modulovi.data.remote.TroopApi
import com.luisptapia.rftarea2modulovi.data.remote.model.TroopDto
import retrofit2.Retrofit

class TroopRepository(
    private val retrofit: Retrofit
) {

    private val troopApi = retrofit.create(TroopApi::class.java)

    suspend fun getTroops(): List<TroopDto> = troopApi.getTroops()

    suspend fun getTroopDetail(id: String?): TroopDto = troopApi.getTroopById(id)

}