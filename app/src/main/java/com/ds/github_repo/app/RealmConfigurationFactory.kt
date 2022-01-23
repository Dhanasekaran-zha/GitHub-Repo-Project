package com.ds.github_repo.app

import com.ds.github_repo.data.module.RealmModuleDB
import io.realm.RealmConfiguration

object RealmConfigurationFactory {
    fun getConfiguration(): RealmConfiguration? {
        return RealmConfiguration.Builder().name(AppConstants.APP_REALM_STORAGE_NAME).schemaVersion(1)
            .modules(RealmModuleDB())
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()
    }
}