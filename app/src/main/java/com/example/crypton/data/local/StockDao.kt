package com.example.crypton.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
    @Insert(
        onConflict = OnConflictStrategy
            .REPLACE
    )
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )
    @Query(
        "DELETE FROM CompanyListingEntity"
    )
    suspend fun clearCompanyListings()
    @Query(
        """
            SELECT *
            FROM CompanyListingEntity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
            symbol LIKE '%' || UPPER(:query) || '%'
        """
    )
    suspend fun searchCompanyListings(
        query: String
    ): List<CompanyListingEntity>
}