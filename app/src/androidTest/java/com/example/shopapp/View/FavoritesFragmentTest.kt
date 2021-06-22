package com.example.shopapp.View

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shopapp.Adapter.FavoritesRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.Repo.FakeRepositoryAndroid
import com.example.shopapp.ViewModel.FavoritesViewModel
import com.example.shopapp.launchFragmentInHiltContainer
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class FavoritesFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun getFavoriteProducts() {
        val testViewModel = FavoritesViewModel(FakeRepositoryAndroid())

        launchFragmentInHiltContainer<FavoritesFragment> {
            Lifecycle.State.CREATED
            (this as FavoritesFragment).viewModel = testViewModel
            viewModel.getFavorites()
        }
        Truth.assertThat(testViewModel.data.value.error).isFalse()
        Truth.assertThat(testViewModel.data.value).isNotEqualTo(QueryEvent.Empty)
        Truth.assertThat(testViewModel.data.value).isNotEqualTo(QueryEvent.Loading)
    }

    @Test
    fun clickFavoriteItemAndLaunchesDetailActivity() {
        val selectedProduct = Product("title", "img", "price", "link")
        launchFragmentInHiltContainer<FavoritesFragment> {
            Lifecycle.State.CREATED
            (this as FavoritesFragment).recyclerAdapter.list = listOf(selectedProduct)
        }
        Espresso.onView(withId(R.id.fragment_favorites_rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FavoritesRecyclerAdapter.ViewHolder>(
                0, click()
            )
        )
        intended(hasComponent(ProductDetailActivity::class.java.name))
    }

    @Test
    fun searchFavoriteProducts() {
        val testViewModel = FavoritesViewModel(FakeRepositoryAndroid())
        val firstProduct = Product("firstProduct", "image1", "price1", "link1")
        val secondProduct = Product("secondProduct", "image2", "price2", "link2")

        launchFragmentInHiltContainer<FavoritesFragment> {
            (this as FavoritesFragment).viewModel = testViewModel
            Lifecycle.State.CREATED
            recyclerAdapter.list = listOf(firstProduct, secondProduct)
        }

        Espresso.onView(withId(R.id.fragment_favorites_search_text)).perform(replaceText("firstProduct"))
        Thread.sleep(2000)
        Truth.assertThat(testViewModel.search).isEqualTo("firstProduct")
    }

    @After
    fun after() {
        Intents.release()
    }

}