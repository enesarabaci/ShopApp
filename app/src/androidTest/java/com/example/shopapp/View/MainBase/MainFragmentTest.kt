package com.example.shopapp.View.MainBase

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.example.shopapp.R
import com.example.shopapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testNavigationFromMainToSearch() {
        val navController = Mockito.mock(NavController::class.java)
        var testExtras: FragmentNavigator.Extras? = null

        launchFragmentInHiltContainer<MainFragment>() {
            Navigation.setViewNavController(requireView(), navController)
            Lifecycle.State.CREATED
            testExtras = (this as MainFragment).extras
        }

        Espresso.onView(ViewMatchers.withId(R.id.fragment_main_base_search)).perform(ViewActions.click())
        Mockito.verify(navController).navigate(R.id.action_mainFragment_to_searchFragment,
            null,
            null,
            testExtras
        )
    }
}