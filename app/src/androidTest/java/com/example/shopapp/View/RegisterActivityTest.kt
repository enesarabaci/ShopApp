package com.example.shopapp.View

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.example.shopapp.Model.EntryEvent
import com.example.shopapp.R
import com.example.shopapp.Repo.FakeRepositoryAndroid
import com.example.shopapp.ViewModel.RegisterViewModel
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class RegisterActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun registerUser() {
        val testViewModel = RegisterViewModel(FakeRepositoryAndroid())

        val scenario = launchActivity<RegisterActivity>()
        scenario.onActivity {
            Lifecycle.State.CREATED
            it.viewModel = testViewModel
        }
        Espresso.onView(ViewMatchers.withId(R.id.register_first_name)).perform(ViewActions.replaceText("firstName"))
        Espresso.onView(ViewMatchers.withId(R.id.register_last_name)).perform(ViewActions.replaceText("lastName"))
        Espresso.onView(ViewMatchers.withId(R.id.register_email)).perform(ViewActions.replaceText("email"))
        Espresso.onView(ViewMatchers.withId(R.id.register_password_first)).perform(ViewActions.replaceText("password"))
        Espresso.onView(ViewMatchers.withId(R.id.register_password_second)).perform(ViewActions.replaceText("password"))
        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click())

        Truth.assertThat(testViewModel.result.value).isEqualTo(EntryEvent.Success)
    }

    @After
    fun after() {
        Intents.release()
    }

}