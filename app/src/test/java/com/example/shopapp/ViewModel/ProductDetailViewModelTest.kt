package com.example.shopapp.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopapp.MainCoroutineRule
import com.example.shopapp.Model.QueryEvent
import com.example.shopapp.Repo.FakeRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ProductDetailViewModel

    @Before
    fun setup() {
        viewModel = ProductDetailViewModel(FakeRepository())
    }

    @Test
    fun `get data with null product link`() {
        viewModel.getData(null)

        val result = viewModel.data.value
        Truth.assertThat(result.error).isTrue()
    }

    @Test
    fun `get data with not null product link`() {
        viewModel.getData("link")

        val result = viewModel.data.value
        Truth.assertThat(result.error).isFalse()
    }

}