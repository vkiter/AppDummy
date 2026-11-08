package edu.khokhlovvaleriy.appdummy.data.repository

import edu.khokhlovvaleriy.appdummy.data.model.Libro
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class LibrosRepository {


    private val _libros = MutableStateFlow(
        listOf(
            Libro(
                id = 1,
                titulo = "Proyecto Hail Mary",
                autor = "Andy Weir",
                year = 2021,
                isbn = "9788418037016",
                cover = "https://covers.openlibrary.org/b/isbn/9788418037016-L.jpg",
                esFavorito = true,
                leido = false
            ),
            Libro(
                id = 2,
                titulo = "Juego de tronos",
                autor = "George R.R. Martin",
                year = 1996,
                isbn = "9780307951182",
                cover = "https://covers.openlibrary.org/b/isbn/9780307951182-L.jpg",
                esFavorito = true,
                leido = true
            ),
            Libro(3, "Festín de cuervos", "George R.R. Martin", 2005, "9780307951212"),
            Libro(4, "Cementerio de Animales", "Stephen King", 1983, "9788401499845", leido = true),
            Libro(5, "El juego de Ender", "Orson Scott Card", 1985, "9788498720068", leido = true)
        )
    )
    val libros: StateFlow<List<Libro>> = _libros.asStateFlow()


    suspend fun getLibros(): List<Libro> {
        delay(800)   // simula la latencia de red
        return _libros.value
    }

    suspend fun getAutores(): List<String> {
        delay(300)
        return _libros.value.map { it.autor }.distinct().sorted()
    }

    suspend fun getLibroPorId(id: Int): Libro? {
        delay(300)
        return _libros.value.find { it.id == id }
    }

    fun toggleFavorito(id: Int) {
        _libros.update { lista ->
            lista.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
        }
    }

    fun toggleLeido(id: Int) {
        _libros.update { lista ->
            lista.map { if (it.id == id) it.copy(leido = !it.leido) else it }
        }
    }

    companion object {
        val instancia = LibrosRepository()
    }
}