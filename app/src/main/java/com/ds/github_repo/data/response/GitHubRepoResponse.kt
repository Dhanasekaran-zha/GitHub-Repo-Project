package com.ds.github_repo.data.response

import com.google.gson.annotations.SerializedName

data class GitHubRepoResponse(

	@field:SerializedName("GitHubRepoResponse")
	var gitHubRepoResponse: List<GitHubRepoResponseItem>? = null
)

data class GitHubRepoResponseItem(

	@field:SerializedName("url")
	val url: String? = null
)
