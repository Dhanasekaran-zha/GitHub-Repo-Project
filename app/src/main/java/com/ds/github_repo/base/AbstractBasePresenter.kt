package com.ds.github_repo.base

import com.ds.github_repo.data.repo.AdminRepo


open class AbstractBasePresenter<in V : BaseView> : BasePresenter<V>, ResponseHandler<Any> {


    private var view: V? = null
    protected var adminRepo: AdminRepo? = null

    override fun setView(view: V) {
        this.view = view

    }

    override fun destroyView() {
        view = null

    }

    override fun destroy() {

    }

    override fun onResponse(responseParser: Any) {

    }

    override fun onFailure(message: String) {

    }

}
