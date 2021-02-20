package edu.born.flicility.presenters

import edu.born.flicility.views.BaseView

interface SubscribePresenter<BV : BaseView> {
    var view: BV?
    fun subscribe(view: BV)
    fun unsubscribe()
}