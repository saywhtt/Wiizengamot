package edu.born.flicility.presenters

import edu.born.flicility.views.BaseView


abstract class BasePresenter<V : BaseView> {
    protected var view: V? = null

    open fun subscribe(view: V) {
        this.view = view
    }

    open fun unsubscribe() {
        this.view = null
    }
}
