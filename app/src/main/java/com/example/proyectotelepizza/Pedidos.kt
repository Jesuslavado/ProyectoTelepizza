package com.example.proyectotelepizza

data class Pedidos(
    val id: String = "",
    val correoUsuario: String = "",
    val total: Float = 0f,
    val productos: List<Producto> = listOf()
)

