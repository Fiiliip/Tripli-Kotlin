package com.example.tripli.data.model

data class FilterData(
    val search: String? = null,
    val owner: String? = null,
    val place: String? = null,
    val category: String? = null,
    val maxPrice: Int? = null,
    val minDays: Int? = null,
    val maxDays: Int? = null,
    val rating: Float? = null
) {
    fun toQueryMap(): Map<String, String> {
        return buildMap {
            search?.let { put("search", it) }
            owner?.let { put("owner", it) }
            place?.let { put("place", it) }
            category?.let { put("category", it) }
            maxPrice?.let { put("maxPrice", it.toString()) }
            minDays?.let { put("minDays", it.toString()) }
            maxDays?.let { put("maxDays", it.toString()) }
            rating?.let { put("rating", it.toString()) }
        }
    }
}
