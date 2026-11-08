package edu.khokhlovvaleriy.appdummy

import edu.khokhlovvaleriy.appdummy.data.repository.LibrosRepository
import edu.khokhlovvaleriy.appdummy.screens.listado.LibrosUiState
import edu.khokhlovvaleriy.appdummy.screens.listado.LibrosViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@OptIn(ExperimentalCoroutinesApi::class)
class LibrosViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LibrosViewModel

    @Before
    fun setup() {
        viewModel = LibrosViewModel(LibrosRepository())
    }

    @Test
    fun `el estado inicial es Cargando`() {
        assertTrue(viewModel.uiState.value is LibrosUiState.Cargando)
    }

    @Test
    fun `cargarLibros produce estado Exito con datos`() = runTest {
        advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertTrue("El estado debe ser Exito", estado is LibrosUiState.Exito)
        assertTrue(
            "Debe haber al menos un libro",
            (estado as LibrosUiState.Exito).libros.isNotEmpty()
        )
    }

    @Test
    fun `la busqueda filtra los libros por titulo`() = runTest {
        advanceUntilIdle()

        viewModel.actualizarBusqueda("juego")

        val estado = viewModel.uiState.value as LibrosUiState.Exito
        assertTrue(estado.libros.isNotEmpty())
        assertTrue(estado.libros.all { it.titulo.contains("juego", ignoreCase = true) })
    }

    @Test
    fun `toggleFavorito invierte el estado de favorito`() = runTest {
        advanceUntilIdle()
        val libro = (viewModel.uiState.value as LibrosUiState.Exito).libros.first()

        viewModel.toggleFavorito(libro)
        advanceUntilIdle()

        val modificado = (viewModel.uiState.value as LibrosUiState.Exito)
            .libros.first { it.id == libro.id }
        assertNotEquals(
            "El favorito debe haber cambiado",
            libro.esFavorito,
            modificado.esFavorito
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(testDispatcher)
    override fun finished(description: Description) = Dispatchers.resetMain()
}