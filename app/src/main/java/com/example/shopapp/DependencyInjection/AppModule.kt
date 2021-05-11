package com.example.shopapp.DependencyInjection

import com.example.shopapp.Model.DataRequest
import com.example.shopapp.Model.FirebaseQuery
import com.example.shopapp.Repo.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun getFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun getDataRequest() = DataRequest()

    @Singleton
    @Provides
    fun getFirebaseQuery(auth: FirebaseAuth, db: FirebaseFirestore, dataRequest: DataRequest) =
        FirebaseQuery(auth, db, dataRequest)

    @Singleton
    @Provides
    fun getRepo(dataRequest: DataRequest, firebaseQuery: FirebaseQuery) =
        Repository(dataRequest, firebaseQuery)

}