package com.example.shopapp.View

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shopapp.Model.EntryEvent
import com.example.shopapp.R
import com.example.shopapp.Repo.FakeRepositoryAndroid
import com.example.shopapp.ViewModel.LoginViewModel
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
class LoginActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun loginUser() {
        val testViewModel = LoginViewModel(FakeRepositoryAndroid())

        val scenario = launchActivity<LoginActivity>()
        scenario.onActivity {
            Lifecycle.State.CREATED
            it.viewModel = testViewModel
        }
        Espresso.onView(withId(R.id.login_email)).perform(replaceText("email"))
        Espresso.onView(withId(R.id.login_password)).perform(replaceText("password"))
        Espresso.onView(withId(R.id.login_button)).perform(click())

        Truth.assertThat(testViewModel.result.value).isEqualTo(EntryEvent.Success)
    }

    @Test
    fun goToRegisterActivity() {
        val testViewModel = LoginViewModel(FakeRepositoryAndroid())

        val scenario = launchActivity<LoginActivity>()
        scenario.onActivity {
            Lifecycle.State.CREATED
            it.viewModel = testViewModel
        }
        Espresso.onView(withId(R.id.login_to_register)).perform(click())

        intended(hasComponent(RegisterActivity::class.java.name))
    }

    @After
    fun after() {
        Intents.release()
    }

}