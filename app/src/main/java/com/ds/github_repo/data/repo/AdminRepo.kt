package com.ds.github_repo.data.repo

import com.ds.github_repo.app.ApiInterface
import com.ds.github_repo.base.ResponseHandler
import com.ds.github_repo.data.response.GitHubRepoResponseItem
import com.ds.github_repo.data.response.GitHubURLResponse
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class AdminRepo(private val apiInterface: ApiInterface, private val realmConfiguration: RealmConfiguration?) {

    /**
     * getRepoList() is to make network call to get all the public repo list
     * @param handler: to throw back the network callbacks
     **/

    fun getRepoList(handler: ResponseHandler<Any>) {
        GlobalScope.launch {
            val result = apiInterface.getRepoList()
            if (result.isSuccessful && result.code() == 200) {
                val urlList = result.body()
                val dataList = arrayListOf<GitHubURLResponse>()
                val tempList = arrayListOf<GitHubRepoResponseItem>()
                for (i in 0..30) {
                    tempList.add(urlList!![i])
                }
                tempList.map { data ->
                    async {
                        apiInterface.getRepoDetail(data.url!!)
                    }
                }.map {
                    val detRes = it.await()
                    if (detRes.isSuccessful && detRes.code() == 200)
                        dataList.add(detRes.body()!!)
                }
                handler.onResponse("Success")
                insertDataToDB(dataList)
            } else
                handler.onFailure("Something went wrong.\nTry again later")
        }

    }

    /**
     * insertDataToDB() is to store the dataList obtained from network call into local Realm DB
     * @param dataList: Public Repo List (Response from network call)
     **/

    private fun insertDataToDB(dataList: ArrayList<GitHubURLResponse>) {
        val realm=Realm.getInstance(realmConfiguration)
        realm?.executeTransaction {
            it.where(GitHubURLResponse::class.java).findAll().deleteAllFromRealm()
            it.insertOrUpdate(dataList)
        }
    }
}