package com.example.shopapp.Repo

import com.example.shopapp.Model.*
import com.example.shopapp.Resource

interface RepositoryInterface {

    suspend fun searchProduct(search: String, page: Int? = null, filtering: String? = null): Resource<List<Product>>

    suspend fun getOpportunityProducts(): Resource<List<Product>>

    suspend fun getFavoriteProducts(): Resource<List<Product>>

    suspend fun getFavoriteLinks(): Resource<List<String>>

    suspend fun deleteFavoriteProduct(link: String)

    suspend fun addFavoriteProduct(link: String)

    suspend fun deleteCartProduct(link: String)

    suspend fun addToCart(link: String): String

    suspend fun updateCount(link: String, count: Int)

    suspend fun getCartProducts(): Resource<List<Product>>

    suspend fun getProductDetail(link: String): Resource<Product>

    suspend fun sendComment(comment: String, link: String) : String

    suspend fun getComments(link: String) : List<Comment>

    suspend fun getUserName() : String

    fun getEmail() : String

    suspend fun createUser(email: String, password: String, firstName: String, lastName: String) : EntryEvent

    suspend fun loginUser(email: String, password: String) : EntryEvent

    suspend fun saveOrder(order: Order) : String

    suspend fun getOrders() : Resource<List<Order>>

    suspend fun saveAddress(address: Address) : Address

    suspend fun saveNumber(number: String) : String

    suspend fun getAddress() : Address

    suspend fun getNumber() : String

}