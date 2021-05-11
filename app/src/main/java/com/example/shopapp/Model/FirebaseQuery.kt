package com.example.shopapp.Model

import com.example.shopapp.Resource
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FirebaseQuery @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val dataRequest: DataRequest
) {

    suspend fun getFavoriteLinks(): Resource<List<String>> {
        auth.currentUser?.email?.let { email ->
            val favorites = ArrayList<String>()
            return try {
                val querySnapshot = db.collection("users").document(email).collection("favorites")
                    .get().await()
                querySnapshot.forEach {
                    favorites.add(it.get("link") as String)
                }
                Resource.Success(favorites.toList())
            } catch (e: Exception) {
                Resource.Message(e.localizedMessage ?: "Error!")
            }
        } ?: kotlin.run {
            return Resource.Message("Error!")
        }
    }

    suspend fun getFavorites(): Resource<List<Product>> {
        val resource = getFavoriteLinks()
        if (resource is Resource.Success) {
            return dataRequest.getProducts(ArrayList(resource.data))
        } else {
            return Resource.Message(resource.message!!)
        }
    }

    suspend fun deleteFavoriteProduct(link: String) {
        auth.currentUser?.email?.let { email ->
            val querySnapshot = db.collection("users")
                .document(email)
                .collection("favorites")
                .whereEqualTo("link", link)
                .get().await()

            querySnapshot.forEach {
                it.reference.delete()
            }
        }
    }

    suspend fun addFavoriteProduct(link: String) {
        auth.currentUser?.email?.let { email ->
            db.collection("users")
                .document(email)
                .collection("favorites")
                .add(hashMapOf("link" to link))
                .await()
        }
    }

    suspend fun updateCount(link: String, count: Int) {
        auth.currentUser?.email?.let { email ->
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
    }

    suspend fun deleteCartProduct(link: String) {
        auth.currentUser?.email?.let { email ->
            val querySnapshot = db.collection("users")
                .document(email)
                .collection("cart")
                .whereEqualTo("link", link)
                .get().await()

            querySnapshot.forEach {
                it.reference.delete()
            }
        }
    }

    suspend fun addToCart(link: String): String {
        auth.currentUser?.email?.let { email ->
            val ref = db.collection("users").document(email).collection("cart")
            var count = 0
            var id: String? = null

            return try {
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
                "Ürün Sepete Eklendi."
            } catch (e: Exception) {
                e.localizedMessage ?: "Error!"
            }
        } ?: kotlin.run {
            return "Error!"
        }
    }

    suspend fun getCartProducts(): Resource<List<Product>> {
        auth.currentUser?.email?.let { email ->
            val products = ArrayList<String>()
            val counts = ArrayList<Int>()
            return try {
                val querySnapshot = db.collection("users")
                    .document(email)
                    .collection("cart")
                    .get()
                    .await()
                querySnapshot.documents.forEach {
                    products.add(it.get("link") as String)
                    counts.add((it.get("count") as Number).toInt())
                }
                dataRequest.getCartProducts(products, counts)
            } catch (e: Exception) {
                Resource.Message(e.localizedMessage ?: "Error!")
            }
        } ?: kotlin.run {
            return Resource.Message("Error!")
        }
    }

    suspend fun sendComment(comment: String, link: String): String {
        auth.currentUser?.email?.let { email ->
            return try {
                val userName = getUserName()

                val data = hashMapOf<String, Any>(
                    "comment" to comment,
                    "time" to Timestamp.now(),
                    "user" to userName,
                    "link" to link
                )

                db.collection("comments")
                    .add(data)
                "Değerlendirme Gönderildi."
            } catch (e: Exception) {
                e.localizedMessage ?: "Error!"
            }
        } ?: return "Error!"
    }

    suspend fun getUserName() : String {
        auth.currentUser?.email?.let { email ->
            val user = db.collection("users")
                .document(email)
                .get()
                .await()

            val userName = "${user.get("firstName")} ${user.get("lastName")}"
            return userName
        } ?: return ""
    }

    suspend fun getComments(link: String): List<Comment> {
        val comments = ArrayList<Comment>()
        auth.currentUser?.let {
            return try {
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
                comments
            } catch (e: Exception) {
                comments
            }
        } ?: return comments
    }

    fun getEmail() : String = auth.currentUser?.email ?: ""

}