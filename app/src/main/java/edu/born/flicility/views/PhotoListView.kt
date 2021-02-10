package edu.born.flicility.views

import edu.born.flicility.model.Photo

interface PhotoListView : BaseView {
    fun update(data: List<Photo>)
}