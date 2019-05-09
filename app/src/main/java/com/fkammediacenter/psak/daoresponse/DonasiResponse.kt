package com.fkammediacenter.psak.daoresponse

import com.google.gson.annotations.SerializedName

data class DonasiResponse(
    @SerializedName("alldonasi")
    val alldonasi: List<Alldonasi>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)