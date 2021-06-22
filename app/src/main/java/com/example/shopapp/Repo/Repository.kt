package com.example.shopapp.Repo

import com.example.shopapp.Model.*
import com.example.shopapp.Resource
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataRequest: DataRequest,
    private val firebaseQuery: FirebaseQuery,
    private val auth: FirebaseAuth
) : RepositoryInterface {

    override suspend fun searchProduct(search: String, page: Int?, filtering: String?): Resource<List<Product>> {
        val result = checker { email ->
            dataRequest.searchProduct(search, page, filtering)
        }
        return result.data ?: Resource.Message(result.message ?: "Error!")
    }

    override suspend fun getOpportunityProducts(): Resource<List<Product>> {
        val result = checker { email ->
            dataRequest.getOpportunityProducts()
        }
        return result.data ?: Resource.Message(result.message ?: "Error!")
    }

    override suspend fun getFavoriteProducts(): Resource<List<Product>> {
        val result = checker { email ->
            firebaseQuery.getFavorites(email)
        }
        return result.data ?: Resource.Message(result.message ?: "Error!")
    }

    override suspend fun getFavoriteLinks(): Resource<List<String>> {
        val result = checker { email ->
            firebaseQuery.getFavoriteLinks(email)
        }
        return result.data ?: Resource.Message(result.message ?: "Error!")
    }

    override suspend fun deleteFavoriteProduct(link: String) {
        checker { email ->
            firebaseQuery.deleteFavoriteProduct(link, email)
        }
    }

    override suspend fun addFavoriteProduct(link: String) {
        checker { email ->
            firebaseQuery.addFavoriteProduct(link, email)
        }
    }

    override suspend fun deleteCartProduct(link: String) {
        checker { email ->
            firebaseQuery.deleteCartProduct(link, email)
        }
    }

    override suspend fun addToCart(link: String): String {
        val result = checker { email ->
            firebaseQuery.addToCart(link, email)
        }
        return result.data ?: result.message ?: "Error!"
    }

    override suspend fun updateCount(link: String, count: Int) {
        checker { email ->
            firebaseQuery.updateCount(link, count, email)
        }
    }

    override suspend fun getCartProducts(): Resource<List<Product>> {
        val result = checker { email ->
            firebaseQuery.getCartProducts(email)
        }
        return result.data ?: Resource.Message(result.message ?: "Error!")
    }

    override suspend fun getProductDetail(link: String): Resource<Product> {
        val result = checker { email ->
            dataRequest.getProductDetail(link)
        }
        return result.data ?: Resource.Message(result.message ?: "Error!")
    }

    override suspend fun sendComment(comment: String, link: String) : String {
        val result = checker { email ->
            firebaseQuery.sendComment(comment, link, email)
        }
        return result.data ?: result.message ?: "Error!"
    }

    override suspend fun getComments(link: String) : List<Comment> {
        val result = checker { email ->
            firebaseQuery.getComments(link)
        }
        return result.data ?: arrayListOf()
    }

    override suspend fun getUserName() : String {
        val result = checker { email ->
            firebaseQuery.getUserName(email)
        }
        return result.data ?: result.message ?: "Error!"
    }

    override fun getEmail() = firebaseQuery.getEmail()

    override suspend fun createUser(email: String, password: String, firstName: String, lastName: String) : EntryEvent {
        return try {
            firebaseQuery.createUser(email, password, firstName, lastName)
        }catch (e: Exception) {
            EntryEvent.Error(e.localizedMessage ?: "Error!")
        }
    }

    override suspend fun loginUser(email: String, password: String): EntryEvent {
        return try {
            firebaseQuery.loginUser(email, password)
        }catch (e: Exception) {
            EntryEvent.Error(e.localizedMessage ?: "Error!")
        }
    }

    override suspend fun saveOrder(order: Order) : String {
        val result = checker { email ->
            firebaseQuery.saveOrder(order, email)
        }
        return result.data ?: result.message ?: "Error!"
    }

    override suspend fun getOrders() : Resource<List<Order>> {
        val result = checker { email ->
            firebaseQuery.getOrders(email)
        }
        return result.data ?: Resource.Message(result.message ?: "Error!")
    }

    override suspend fun saveAddress(address: Address) : Address {
        val result = checker { email ->
            firebaseQuery.saveAddress(address, email)
        }
        return result.data ?: Address("", "", "", "")
    }

    override suspend fun saveNumber(number: String) : String {
        val result = checker { email ->
            firebaseQuery.saveNumber(number, email)
        }
        return result.data ?: ""
    }

    override suspend fun getAddress() : Address {
        val result = checker { email ->
            firebaseQuery.getAddress(email)
        }
        return result.data ?: Address("", "", "", "")
    }

    override suspend fun getNumber() : String {
        val result = checker { email ->
            firebaseQuery.getNumber(email)
        }
        return result.data ?: ""
    }

    suspend fun <T> checker(listener: suspend (String) -> (T)) : CheckerClass<T> {
        return auth.currentUser?.email?.let { email ->
            try {
                CheckerClass.Success(listener.invoke(email))
            }catch (e: Exception) {
                CheckerClass.Error(e.localizedMessage ?: "Error!")
            }
        } ?: CheckerClass.Error("Error!")
    }

}

sealed class CheckerClass<T>(val data:T? = null, val message:String? = null) {
    class Success<T>(data:T) : CheckerClass<T>(data = data)
    class Error<T>(message:String) : CheckerClass<T>(message = message)
}