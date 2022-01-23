package com.ds.github_repo.data.module

import com.ds.github_repo.data.response.GitHubURLResponse
import com.ds.github_repo.data.response.License
import com.ds.github_repo.data.response.Owner
import io.realm.annotations.RealmModule

@RealmModule(library = true,classes = [GitHubURLResponse::class,
        Owner::class,
        License::class])
class RealmModuleDB {
}