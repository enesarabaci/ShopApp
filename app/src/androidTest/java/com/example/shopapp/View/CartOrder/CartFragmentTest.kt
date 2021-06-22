package com.example.shopapp.View.CartOrder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shopapp.Adapter.OrdersRecyclerAdapter
import com.example.shopapp.Model.Product
import com.example.shopapp.R
import com.example.shopapp.View.ProductDetailActivity
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
class CartFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun clickApproveButtonThenNavigatesOrderFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val selectedProduct = Product("title", "img", "100", "link")

        launchFragmentInHiltContainer<CartFragment> {
            navController.setGraph(R.navigation.nav_graph_cart_order)
            Navigation.setViewNavController(requireView(), navController)
            (this as CartFragment).recyclerAdapter.list = listOf(selectedProduct)
            totalPrice = 100
        }

        Espresso.onView(withId(R.id.fragment_cart_confirm)).perform(click())
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.orderFragment)
    }

    @Test
    fun clickOrderItemAndLaunchesDetailActivity() {
        val selectedProduct = Product("title", "img", "100", "link")

        launchFragmentInHiltContainer<CartFragment> {
            (this as CartFragment).recyclerAdapter.list = listOf(selectedProduct)
        }

        Espresso.onView(withId(R.id.fragment_cart_rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<OrdersRecyclerAdapter.ViewHolder>(
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