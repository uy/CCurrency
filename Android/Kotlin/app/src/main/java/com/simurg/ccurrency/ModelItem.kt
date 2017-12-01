package com.simurg.ccurrency

import com.google.gson.annotations.SerializedName

/**
* Created by uyegen on 29.11.2017.
*/

data class ModelItem(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("rank") val rank: String,
    @SerializedName("price_usd") val price_usd: String,
    @SerializedName("price_btc") val price_btc: String,
    @SerializedName("24h_volume_usd") val volume_usd_24h: String,
    @SerializedName("market_cap_usd") val market_cap_usd: String,
    @SerializedName("available_supply") val available_supply: String,
    @SerializedName("total_supply") val total_supply: String,
    @SerializedName("percent_change_1h") val percent_change_1h: String,
    @SerializedName("percent_change_24h") val percent_change_24h: String,
    @SerializedName("percent_change_7d") val percent_change_7d: String,
    @SerializedName("last_updated") val last_updated: String
)
