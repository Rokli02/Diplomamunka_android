package me.uni.hiker.api.model

data class HealthcheckResponse (val status: String, val version: String)

data class Page<DataType>(
    val data: List<DataType>,
    val totalCount: Int,
)