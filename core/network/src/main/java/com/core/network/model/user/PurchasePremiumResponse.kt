package com.core.network.model.user

data class PurchasePremiumResponse(
    val success: Boolean,
    val data: PurchasePremiumData?
)

data class PurchasePremiumData(
    val message: String
)