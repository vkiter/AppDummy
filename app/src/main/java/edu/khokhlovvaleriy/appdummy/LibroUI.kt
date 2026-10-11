package edu.khokhlovvaleriy.appdummy.model


data class LibroUI(
    val id: Int = 0,
    val titulo: String = "",
    val autor: String = "",
    val year: Int? = 1900,
    val isbn: String = "",
    val cover: String = "",
    val esFavorito: Boolean = false,
    val leido: Boolean = false
)


val librosEjemplo = listOf(
    LibroUI(1, "Proyecto Hail Mary", "Andy Weir", 2021, "9788418037016",
        "https://covers.openlibrary.org/b/isbn/9788418037016-L.jpg", esFavorito = true, leido = false),
    LibroUI(2, "Juego de tronos", "George R.R. Martin", 1996, "9780307951182",
        "https://covers.openlibrary.org/b/isbn/9780307951182-L.jpg", esFavorito = true, leido = true),
    LibroUI(3, "Festín de cuervos", "George R.R. Martin", 2005, "9780307951212"),
    LibroUI(4, "Cementerio de Animales", "Stephen King", 1983, "9788401499845", leido = true),
    LibroUI(5, "El juego de Ender", "Orson Scott Card", 1985, "9788498720068", leido = true)
)