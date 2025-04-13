package com.luisptapia.rftarea2modulovi.data.remote.model

import com.google.gson.annotations.SerializedName

data class TroopDto(

    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("tipo_tropa")
    var tipo_tropa: String,
    @SerializedName("estadisticas")
    var estadisticas : EstadisticasTropa,
    @SerializedName("niveles")
    var niveles : List<NivelTropaDto>
)


data class EstadisticasTropa(
    @SerializedName("objetivo_preferido")
    var objetivo_preferido: String,
    @SerializedName("tipo_dano")
    var tipo_dano : String,
    @SerializedName("espacio_vivienda")
    var espacio_vivienda: String,

    @SerializedName("velocidad_movimiento")
    var velocidad_movimiento: String,

    @SerializedName("velocidad_ataque")
    var velocidad_ataque: String,

    @SerializedName("rango")
    var rango: String,

)


data class NivelTropaDto(
    @SerializedName("imagen")
    var imagen : String,

    @SerializedName("niveles")
    var niveles:List<String>
)


