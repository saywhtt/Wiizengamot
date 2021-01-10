package edu.born.flicility.adapters

interface BaseAdapter<T> {
    fun insertAll(items: List<T>)
    fun deleteAll()
}