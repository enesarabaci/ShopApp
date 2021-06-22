package com.example.shopapp.Repo

import com.example.shopapp.Model.*
import com.example.shopapp.Resource

class FakeRepositoryAndroid : RepositoryInterface {

    private var favoriteProducts = mutableListOf<String>()
    private var cartProducts = mutableListOf(Product("title", "img", "100", "link"))
    private var cartProductLinks = mutableListOf<String>()
    private var orderProducts = mutableListOf<Order>()

    private var currentUser = true
    private var connection = true

    override suspend fun searchProduct(
        search: String,
        page: Int?,
        filtering: String?
    ): Resource<List<Product>> {
        return if (connection) {
            Resource.Success(listOf(Product("title", "img", "price", "link")))
        } else {
            Resource.Message("Connection Error!")
        }
    }

    override suspend fun getOpportunityProducts(): Resource<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteProducts(): Resource<List<Product>> {
        return if (connection) {
            Resource.Success(listOf(
                Product("firstProduct", "image1", "price1", "link1"),
                Product("secondProduct", "image2", "price2", "link2")
            ))
        } else {
            Resource.Message("Connection Error!")
        }
    }

    override suspend fun getFavoriteLinks(): Resource<List<String>> {
        return if (connection) {
            Resource.Success(favoriteProducts)
        } else {
            Resource.Message("Connection Error!")
        }
    }

    override suspend fun deleteFavoriteProduct(link: String) {
        favoriteProducts.remove(link)
    }

    override suspend fun addFavoriteProduct(link: String) {
        favoriteProducts.add(link)
    }

    override suspend fun deleteCartProduct(link: String) {
        cartProductLinks.remove(link)
    }

    override suspend fun addToCart(link: String): String {
        cartProducts.add(Product("", "", "", link))
        cartProductLinks.add(link)
        return "Ürün Sepete Eklendi."
    }

    override suspend fun updateCount(link: String, count: Int) {
        return
    }

    override suspend fun getCartProducts(): Resource<List<Product>> {
        return if (connection) {
            Resource.Success(cartProducts)
        } else {
            Resource.Message("Connection Error!")
        }
    }

    override suspend fun getProductDetail(link: String): Resource<Product> {
        return if (connection) {
            Resource.Success(
                Product("title", "img", "price", "link")
            )
        } else {
            Resource.Message("Connection Error!")
        }
    }

    override suspend fun sendComment(comment: String, link: String): String {
        return "Değerlendirme Gönderildi."
    }

    override suspend fun getComments(link: String): List<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserName(): String {
        TODO("Not yet implemented")
    }

    override fun getEmail(): String {
        return ""
    }

    override suspend fun createUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): EntryEvent {
        return if (connection) {
            EntryEvent.Success
        } else {
            EntryEvent.Error("Connection Error!")
        }
    }

    override suspend fun loginUser(email: String, password: String): EntryEvent {
        return if (connection) {
            EntryEvent.Success
        } else {
            EntryEvent.Error("Connection Error!")
        }
    }

    override suspend fun saveOrder(order: Order): String {
        return if (connection) {
            "Sipariş Kaydınız Oluşturuldu."
        } else {
            "Connection Error!"
        }
    }

    override suspend fun getOrders(): Resource<List<Order>> {
        return if (currentUser) {
            if (connection) {
                Resource.Success(orderProducts)
            }else {
                Resource.Message("Connection Error!")
            }
        }else {
            Resource.Message("Error!")
        }
    }

    override suspend fun saveAddress(address: Address): Address {
        TODO("Not yet implemented")
    }

    override suspend fun saveNumber(number: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getAddress(): Address {
        TODO("Not yet implemented")
    }

    override suspend fun getNumber(): String {
        TODO("Not yet implemented")
    }

}