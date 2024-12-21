package com.core.network.model.user

data class PurchasePremiumRequest(
    val cvv: String,
    val card_number: String,
    val expiration_date: String,
    val card_holder: String
)
