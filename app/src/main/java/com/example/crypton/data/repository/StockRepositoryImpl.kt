package com.example.crypton.data.repository

import com.example.crypton.data.local.StockDatabase
import com.example.crypton.data.mapper.toCompanyListing
import com.example.crypton.data.remote.StockApi
import com.example.crypton.domain.model.CompanyListing
import com.example.crypton.domain.repository.StockRepository
import com.example.crypton.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    val db: StockDatabase
    ): StockRepository {
    private val dao = db.dao
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(
                Resource.Loading(
                    isLoading = true
                )
            )
            val localListings = dao.searchCompanyListings(query)
            emit(
                Resource.Success(
                    data = localListings.map {
                        it.toCompanyListing()
                    }
                )
            )
            val isDbEmpty = localListings.isEmpty() && query.isEmpty()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache) {
                emit(
                    Resource.Loading(
                        isLoading = false
                    )
                )
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                response.byteStream()
            }
            catch(e: IOException) {
                e.printStackTrace()
                emit(
                    Resource.Error(
                        message = "Couldn't load data!"
                    )
                )
            }
            catch(e: HttpException) {
                e.printStackTrace()
                emit(
                    Resource.Error(
                        message = "Couldn't load data!"
                    )
                )
            }
        }
    }
}