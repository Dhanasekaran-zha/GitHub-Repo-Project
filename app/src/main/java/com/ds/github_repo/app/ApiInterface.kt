package com.ds.github_repo.app

import com.ds.github_repo.data.response.GitHubRepoResponseItem
import com.ds.github_repo.data.response.GitHubURLResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {

    @GET(ApiUrl.GET_ALL_PUBLIC_REPO)
    suspend fun getRepoList( ): Response<ArrayList<GitHubRepoResponseItem>>

    @GET
    suspend fun getRepoDetail(@Url url: String) : Response<GitHubURLResponse>
}