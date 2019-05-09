package com.fkammediacenter.psak.daoresponse

import com.google.gson.annotations.SerializedName

data class DonaturKotakResponse(
    @SerializedName("donaturlist")
    val donaturlist: List<DonaturKotakList>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)