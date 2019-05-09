package com.fkammediacenter.psak.daoresponse

import com.google.gson.annotations.SerializedName

data class FundraiserResponse(
    @SerializedName("datafundraiser")
    val datafundraiser: List<Datafundraiser>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)