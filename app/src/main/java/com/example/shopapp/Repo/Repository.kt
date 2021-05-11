package com.example.shopapp.Repo

import com.example.shopapp.Model.DataRequest
import com.example.shopapp.Model.FirebaseQuery
import com.example.shopapp.Model.Product
import com.example.shopapp.Resource
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataRequest: DataRequest,
    private val firebaseQuery: FirebaseQuery
) {

    suspend fun searchProduct(search: String): Resource<List<Product>> =
        dataRequest.searchProduct(search)

    suspend fun getOpportunityProducts(): Resource<List<Product>> =
        dataRequest.getOpportunityProducts()

    suspend fun getFavoriteProducts(): Resource<List<Product>> =
        firebaseQuery.getFavorites()

    suspend fun getFavoriteLinks(): Resource<List<String>> =
        firebaseQuery.getFavoriteLinks()

    suspend fun deleteFavoriteProduct(link: String) = firebaseQuery.deleteFavoriteProduct(link)

    suspend fun addFavoriteProduct(link: String) = firebaseQuery.addFavoriteProduct(link)

    suspend fun deleteCartProduct(link: String) = firebaseQuery.deleteCartProduct(link)

    suspend fun addToCart(link: String): String =
        firebaseQuery.addToCart(link)

    suspend fun updateCount(link: String, count: Int) = firebaseQuery.updateCount(link, count)

    suspend fun getCartProducts(): Resource<List<Product>> =
        firebaseQuery.getCartProducts()

    suspend fun getProductDetail(link: String): Resource<Product> =
        dataRequest.getProductDetail(link)

    suspend fun sendComment(comment: String, link: String) =
        firebaseQuery.sendComment(comment, link)

    suspend fun getComments(link: String) = firebaseQuery.getComments(link)

    suspend fun getUserName() = firebaseQuery.getUserName()

    fun getEmail() = firebaseQuery.getEmail()

}