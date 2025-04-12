package com.luisptapia.rftarea2modulovi.data.remote

import com.luisptapia.rftarea2modulovi.data.remote.model.TroopDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TroopApi {


    @GET("troops")
    suspend fun getTroops(): List<TroopDto>


    @GET("troops/{id}")
    suspend fun getTroopById(
        @Path("id")
        id: String?/*,
        @Query("name")
        name: String?*/
    ): TroopDto


}