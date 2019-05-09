package com.fkammediacenter.psak.daoresponse

import com.google.gson.annotations.SerializedName

data class DonaturTetapResponse(
    @SerializedName("donaturlist")
    val donaturlist: List<DonaturTetaplist>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)