package com.example.crypton.domain.repository

import com.example.crypton.domain.model.CompanyListing
import com.example.crypton.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}