package com.example.shopapp.View.MainBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shopapp.Adapter.ProductsRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.Repo.FakeRepositoryAndroid
import com.example.shopapp.View.ProductDetailActivity
import com.example.shopapp.ViewModel.SearchViewModel
import com.example.shopapp.launchFragmentInHiltContainer
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class SearchFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun searchProduct() {
        val testViewModel = SearchViewModel(FakeRepositoryAndroid())

        launchFragmentInHiltContainer<SearchFragment> {
            (this as SearchFragment).viewModel = testViewModel
            Lifecycle.State.CREATED
        }

        Espresso.onView(withId(R.id.fragment_search_text)).perform(replaceText("search"))
        Espresso.onView(withId(R.id.fragment_search_text)).perform(pressImeActionButton())

        Truth.assertThat(testViewModel.data.value.error).isFalse()
        Truth.assertThat(testViewModel.data.value).isNotEqualTo(QueryEvent.Empty)
        Truth.assertThat(testViewModel.data.value).isNotEqualTo(QueryEvent.Loading)
    }

    @Test
    fun clickItemAndLaunchesDetailActivity() {
        val selectedProduct = Product("title", "img", "price", "link")
        launchFragmentInHiltContainer<SearchFragment> {
            Lifecycle.State.CREATED
            (this as SearchFragment).recyclerAdapter.list = listOf(selectedProduct)
        }

        Espresso.onView(withId(R.id.fragment_search_rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductsRecyclerAdapter.ViewHolder>(
                0, click()
            )
        )
        intended(hasComponent(ProductDetailActivity::class.java.name))
    }

    @After
    fun after() {
        Intents.release()
    }

}