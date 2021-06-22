package com.example.shopapp.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopapp.MainCoroutineRule
import com.example.shopapp.Model.EntryEvent
import com.example.shopapp.Repo.FakeRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        viewModel = RegisterViewModel(FakeRepository())
    }

    @Test
    fun `register user with empty email`() {
        viewModel.register("firstName", "lastName", "", "password", "password")

        val result = viewModel.result.value
        Truth.assertThat(result.error).isNotNull()
    }

    @Test
    fun `register user with empty firstName`() {
        viewModel.register("", "lastName", "email", "password", "password")

        val result = viewModel.result.value
        Truth.assertThat(result.error).isNotNull()
    }

    @Test
    fun `register user with empty lastName`() {
        viewModel.register("firstName", "", "email", "password", "password")

        val result = viewModel.result.value
        Truth.assertThat(result.error).isNotNull()
    }

    @Test
    fun `register user with empty password`() {
        viewModel.register("firstName", "lastName", "email", "", "")

        val result = viewModel.result.value
        Truth.assertThat(result.error).isNotNull()
    }

    @Test
    fun `register user with different passwords`() {
        viewModel.register("firstName", "lastName", "email", "password1", "password2")

        val result = viewModel.result.value
        Truth.assertThat(result.error).isNotEmpty()
    }

    @Test
    fun `register user with not empty inputs and same passwords`() {
        viewModel.register("firstName", "lastName", "email", "password", "password")

        val result = viewModel.result.value
        Truth.assertThat(result).isEqualTo(EntryEvent.Success)
    }

}