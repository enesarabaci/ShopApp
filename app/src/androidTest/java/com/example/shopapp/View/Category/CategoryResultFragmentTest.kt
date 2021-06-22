package com.example.shopapp.View.Category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shopapp.Adapter.ProductsRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.R
import com.example.shopapp.Repo.FakeRepositoryAndroid
import com.example.shopapp.View.ProductDetailActivity
import com.example.shopapp.ViewModel.CategoryResultViewModel
import com.example.shopapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class CategoryResultFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun clickCategoryItemAndLaunchesDetailActivity() {
        val selectedProduct = Product("title", "img", "price", "link")
        val testViewModel = CategoryResultViewModel(FakeRepositoryAndroid())

        launchFragmentInHiltContainer<CategoryResultFragment> {
            Lifecycle.State.CREATED
            (this as CategoryResultFragment).recyclerAdapter.list = listOf(selectedProduct)
            viewModel = testViewModel

        }

        Espresso.onView(withId(R.id.fragment_category_result_rv)).perform(
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