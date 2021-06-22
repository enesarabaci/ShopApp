package com.example.shopapp.Model

import com.example.shopapp.Resource
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FirebaseQuery @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val dataRequest: DataRequest
) {

    suspend fun getFavoriteLinks(email: String): Resource<List<String>> {
        val favorites = ArrayList<String>()

        val querySnapshot = db.collection("users").document(email).collection("favorites")
            .get().await()
        querySnapshot.forEach {
            favorites.add(it.get("link") as String)
        }
        return Resource.Success(favorites.toList())
    }

    suspend fun getFavorites(email: String): Resource<List<Product>> {
        val resource = getFavoriteLinks(email)
        if (resource is Resource.Success) {
            return dataRequest.getProducts(ArrayList(resource.data))
        } else {
            return Resource.Message(resource.message!!)
        }
    }

    suspend fun deleteFavoriteProduct(link: String, email: String) {
        val querySnapshot = db.collection("users")
            .document(email)
            .collection("favorites")
            .whereEqualTo("link", link)
            .get().await()

        querySnapshot.forEach {
            it.reference.delete()
        }
    }

    suspend fun addFavoriteProduct(link: String, email: String) {
        db.collection("users")
            .document(email)
            .collection("favorites")
            .add(hashMapOf("link" to link))
            .await()
    }

    suspend fun updateCount(link: String, count: Int, email: String) {
        val querySnapshot = db.collection("users")
            .document(email)
            .collection("cart")
            .whereEqualTo("link", link)
            .get().await()
        val documentId = querySnapshot.documents.first().id
        db.collection("users")
            .document(email)
            .collection("cart")
            .document(documentId)
            .update("count", count)
    }

    suspend fun deleteCartProduct(link: String, email: String) {
        val querySnapshot = db.collection("users")
            .document(email)
            .collection("cart")
            .whereEqualTo("link", link)
            .get().await()

        querySnapshot.forEach {
            it.reference.delete()
        }
    }

    suspend fun addToCart(link: String, email: String): String {
        val ref = db.collection("users").document(email).collection("cart")
        var count = 0
        var id: String? = null

        val querySnapshot = ref.get().await()
        querySnapshot.documents.forEach {
            if (it.get("link") == link) {
                count = (it.get("count") as Number).toInt()
                id = it.id
            }
        }
        ref.document(id ?: UUID.randomUUID().toString())
            .set(hashMapOf("count" to count + 1, "link" to link))
            .await()
        return "Ürün Sepete Eklendi."
    }

    suspend fun getCartProducts(email: String): Resource<List<Product>> {
        val products = ArrayList<String>()
        val counts = ArrayList<Int>()

        val querySnapshot = db.collection("users")
            .document(email)
            .collection("cart")
            .get()
            .await()
        querySnapshot.documents.forEach {
            products.add(it.get("link") as String)
            counts.add((it.get("count") as Number).toInt())
        }
        return dataRequest.getCartProducts(products, counts)
    }

    suspend fun sendComment(comment: String, link: String, email: String): String {
        val userName = getUserName(email)
        val data = hashMapOf<String, Any>(
            "comment" to comment,
            "time" to Timestamp.now(),
            "user" to userName,
            "link" to link
        )

        db.collection("comments")
            .add(data)
        return "Değerlendirme Gönderildi."
    }

    suspend fun getUserName(email: String) : String {
        val user = db.collection("users")
            .document(email)
            .get()
            .await()

        val userName = "${user.get("firstName")} ${user.get("lastName")}"
        return userName
    }

    suspend fun getComments(link: String): List<Comment> {
        val comments = ArrayList<Comment>()
        val data = db.collection("comments")
            .whereEqualTo("link", link)
            .get()
            .await()
        data.documents.forEach {
            it.apply {
                comments.add(
                    Comment(
                        get("user") as String,
                        get("comment") as String,
                        get("time") as Timestamp,
                    )
                )
            }
        }
        return comments
    }

    fun getEmail() : String = auth.currentUser?.email ?: ""

    suspend fun createUser(email: String, password: String, firstName: String, lastName: String) : EntryEvent {
        auth.createUserWithEmailAndPassword(email, password).await()

        val hm = HashMap<String, String>()
        hm.put("firstName", firstName)
        hm.put("lastName", lastName)
        db.collection("users")
            .document(email)
            .set(hm)
            .await()

        return EntryEvent.Success
    }

    suspend fun loginUser(email: String, password: String) : EntryEvent {
        auth.signInWithEmailAndPassword(email, password).await()
        return EntryEvent.Success
    }

    suspend fun saveOrder(order: Order, email: String) : String {
        val data = HashMap<String, Any>()
        order.apply {
            data.put("city", address.city)
            data.put("district", address.district)
            data.put("neighborhood", address.neighborhood)
            data.put("address", address.address)
            data.put("cardNumber", cardNumber)
            data.put("month", month)
            data.put("year", year)
            data.put("cvv", cvv)
            data.put("price", price)
            data.put("products", products)
            data.put("status", "Yolda")
            data.put("date", Timestamp.now())
        }

        db.collection("users")
            .document(email)
            .collection("orders")
            .add(data)
            .await()

        val query = db.collection("users")
            .document(email)
            .collection("cart")

        order.products.forEach {
            val delete = query.whereEqualTo("link", it)
                .get()
                .await()

            delete.documents.forEach {
                it.reference.delete()
            }
        }

        return "Sipariş Kaydınız Oluşturuldu."
    }

    suspend fun getOrders(email: String) : Resource<List<Order>> {
        val query = db.collection("users")
            .document(email)
            .collection("orders")
            .get()
            .await()

        val result = ArrayList<Order>()

        query.documents.forEach {
            result.add(Order(
                Address(it.get("city") as String,
                    it.get("district") as String,
                    it.get("neighborhood") as String,
                    it.get("address") as String
                ),
                it.get("cardNumber") as String,
                it.get("month") as String,
                it.get("year") as String,
                it.get("cvv") as String,
                (it.get("price") as Number).toInt(),
                it.get("products") as List<String>,
                it.get("date") as Timestamp,
                it.get("status") as String
            ))
        }
        return Resource.Success(result)
    }

    suspend fun saveNumber(number: String, email: String) : String {
        db.collection("users")
            .document(email)
            .update("number", number)
            .await()
        return getNumber(email)
    }

    suspend fun saveAddress(address: Address, email: String) : Address {
        val addressMap = hashMapOf("city" to address.city, "district" to address.district, "neighborhood" to address.neighborhood, "address" to address.address)
        db.collection("users")
            .document(email)
            .update("address", addressMap)
            .await()
        return getAddress(email)
    }

    suspend fun getAddress(email: String) : Address {
        val ds = db.collection("users")
            .document(email)
            .get()
            .await()
        var result: HashMap<String, String>? = null
        if (ds.get("address") != null) {
            result = ds.get("address") as HashMap<String, String>
        }
        return Address(
            result?.get("city") ?: "",
            result?.get("district") ?: "",
            result?.get("neighborhood") ?: "",
            result?.get("address") ?: ""
        )
    }

    suspend fun getNumber(email: String) : String {
        val ds = db.collection("users")
            .document(email)
            .get()
            .await()
        val result = ds.get("number")
        return result as String
    }

}