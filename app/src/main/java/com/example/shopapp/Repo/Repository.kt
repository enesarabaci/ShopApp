package com.example.shopapp.Repo

import com.example.shopapp.Model.*
import com.example.shopapp.Resource
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataRequest: DataRequest,
    private val firebaseQuery: FirebaseQuery
) {

    fun searchProduct(search: String, page: Int? = null, filtering: String? = null): Resource<List<Product>> =
        dataRequest.searchProduct(search, page, filtering)

    fun getOpportunityProducts(): Resource<List<Product>> =
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

    fun getProductDetail(link: String): Resource<Product> =
        dataRequest.getProductDetail(link)

    suspend fun sendComment(comment: String, link: String) =
        firebaseQuery.sendComment(comment, link)

    suspend fun getComments(link: String) = firebaseQuery.getComments(link)

    suspend fun getUserName() = firebaseQuery.getUserName()

    fun getEmail() = firebaseQuery.getEmail()

    suspend fun saveOrder(order: Order) = firebaseQuery.saveOrder(
        order
    )

    suspend fun getOrders() = firebaseQuery.getOrders()

    suspend fun saveAddress(address: Address) = firebaseQuery.saveAddress(address)

    suspend fun saveNumber(number: String) = firebaseQuery.saveNumber(number)

    suspend fun getAddress() = firebaseQuery.getAddress()

    suspend fun getNumber() = firebaseQuery.getNumber()
}