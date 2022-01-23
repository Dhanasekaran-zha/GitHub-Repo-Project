package com.ds.github_repo.base


interface ResponseHandler<in T> {

    fun onResponse(responseParser: T)

    fun onFailure(message: String)


}
