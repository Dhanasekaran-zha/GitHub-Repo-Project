package com.ds.github_repo.ui.dashboard

import com.ds.github_repo.app.AppController
import com.ds.github_repo.base.AbstractBasePresenter

class DashBoardPresenter : AbstractBasePresenter<DashBoardVIew>() {

    private var dashBoardVIew:DashBoardVIew?=null

    override fun setView(view: DashBoardVIew) {
        super.setView(view)
        this.dashBoardVIew=view
        adminRepo=AppController.getInstanse()?.getAdminRepo()
    }

    /*To initiate the network call function in AdminRepo */
    fun getRepoList(){
        dashBoardVIew?.showLoading()
        adminRepo?.getRepoList(this)
    }

    /*Success Callback of Network call received from AdminRepo */
    override fun onResponse(responseParser: Any) {
        super.onResponse(responseParser)
        dashBoardVIew?.hideLoading()
    }

    /*Failure Callback of Network call received from AdminRepo */
    override fun onFailure(message: String) {
        dashBoardVIew?.hideLoading()
        dashBoardVIew?.showError(message)
    }
}