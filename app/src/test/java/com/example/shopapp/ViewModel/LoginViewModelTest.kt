package com.example.shopapp.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopapp.MainCoroutineRule
import com.example.shopapp.Model.EntryEvent
import com.example.shopapp.Repo.FakeRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(FakeRepository())
    }

    @Test
    fun `login user with empty email`() {
        viewModel.login("", "password")

        val result = viewModel.result.value
        Truth.assertThat(result.error).isNotNull()
    }

    @Test
    fun `login user with empty password`() {
        viewModel.login("email", "")

        val result = viewModel.result.value
        Truth.assertThat(result.error).isNotNull()
    }

    @Test
    fun `login user with not empty inputs`() {
        viewModel.login("email", "password")

        val result = viewModel.result.value
        Truth.assertThat(result).isEqualTo(EntryEvent.Success)
    }

}