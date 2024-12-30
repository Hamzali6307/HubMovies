package com.hamy.hubmovies.common

interface Mapper<F, T> {
    fun fromMap(from: F): T
}