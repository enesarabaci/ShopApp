package com.example.shopapp.View.CartOrder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.R
import com.example.shopapp.Repo.FakeRepositoryAndroid
import com.example.shopapp.ViewModel.OrderViewModel
import com.example.shopapp.launchFragmentInHiltContainer
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class OrderFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun confirmOrder() {
        val testViewModel = OrderViewModel(FakeRepositoryAndroid())

        launchFragmentInHiltContainer<OrderFragment> {
            Lifecycle.State.CREATED
            (this as OrderFragment).viewModel = testViewModel
        }
        Espresso.onView(withId(R.id.fragment_order_city)).perform(replaceText("city"))
        Espresso.onView(withId(R.id.fragment_order_district)).perform(replaceText("district"))
        Espresso.onView(withId(R.id.fragment_order_neighborhood)).perform(replaceText("neighborhood"))
        Espresso.onView(withId(R.id.fragment_order_address)).perform(replaceText("address"))
        Espresso.onView(withId(R.id.fragment_order_card_number)).perform(replaceText("number"))
        Espresso.onView(withId(R.id.fragment_order_month)).perform(replaceText("month"))
        Espresso.onView(withId(R.id.fragment_order_year)).perform(replaceText("year"))
        Espresso.onView(withId(R.id.fragment_order_cvv)).perform(replaceText("cvv"))
        Espresso.onView(withId(R.id.fragment_order_confirm)).perform(click())

        Truth.assertThat(testViewModel.result.value.error).isFalse()
        Truth.assertThat(testViewModel.result.value).isNotEqualTo(QueryEvent.Empty)
        Truth.assertThat(testViewModel.result.value).isNotEqualTo(QueryEvent.Loading)
    }

}