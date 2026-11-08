package edu.khokhlovvaleriy.appdummy.screens

sealed class EstadoPermiso {
    data object Concedido : EstadoPermiso()
    data object Pendiente : EstadoPermiso()
    data object Denegado : EstadoPermiso()
    data object DenegadoPermanentemente : EstadoPermiso()
}