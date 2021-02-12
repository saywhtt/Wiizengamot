package edu.born.flicility.adapters

interface BaseAdapter<T> {
    fun update(items: List<T>)
    fun updateWithStartPosition(items: List<T>, position: Int)
    fun deleteAll()
}