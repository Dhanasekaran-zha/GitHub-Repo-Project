package com.ds.github_repo.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ds.github_repo.R
import com.ds.github_repo.app.AppController
import com.ds.github_repo.app.RealmConfigurationFactory
import com.ds.github_repo.base.BaseActivity
import com.ds.github_repo.data.response.GitHubURLResponse
import com.ds.github_repo.ui.adapter.RepoListAdapter
import com.ds.github_repo.utils.ConnectivityReceiver
import com.ds.github_repo.utils.NetworkUtils
import io.realm.Realm
import io.realm.RealmQuery
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashBoardActivity : BaseActivity(), DashBoardVIew, View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private val presenter: DashBoardPresenter = DashBoardPresenter()
    private var mRepoListAdapter: RepoListAdapter? = null
    private val rem = Realm.getInstance(RealmConfigurationFactory.getConfiguration()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        presenter.setView(this)

        refreshLayout.setOnRefreshListener(this)
        searchIcon.setOnClickListener(this)
        backIcon.setOnClickListener(this)
        retryBtn.setOnClickListener(this)

        AppController.getInstanse()?.setConnectivityListener(this)

        setRecyclerView(rem!!.where(GitHubURLResponse::class.java))
        getRepoList()

        /*Textwatcher for quick results on the search*/
        searchEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                setRecyclerView(
                    rem.where(GitHubURLResponse::class.java).contains("name", s.toString())
                )
            }
        })
    }

    /*Checks the network connectivity and calls the getRepoList() function in presenter*/
    private fun getRepoList() {
        if (NetworkUtils.getConnectivityStatus(this) != NetworkUtils.NETWORK_STATUS_NOT_CONNECTED) {
            presenter.getRepoList()
        } else if (mRepoListAdapter?.data?.size == 0 || mRepoListAdapter?.data?.size == null) {
            noNetworkLayout.visibility = View.VISIBLE
        } else {
            setRecyclerView(rem!!.where(GitHubURLResponse::class.java))
        }
    }


    /*To enable search which visibles the search layout*/
    private fun startSearch() {
        titleText.visibility = View.GONE
        searchEdt.visibility = View.VISIBLE
        backIcon.visibility = View.VISIBLE
    }

    /*To disable search which hides the search layout*/
    private fun stopSearch() {
        titleText.visibility = View.VISIBLE
        searchEdt.visibility = View.GONE
        backIcon.visibility = View.GONE
        setRecyclerView(rem!!.where(GitHubURLResponse::class.java))
    }

    /**
     * To set the repoList data into the recycler view
     *@param query: Database Query to access the repoList from the local Realm DB
     **/
    private fun setRecyclerView(query: RealmQuery<GitHubURLResponse>) {
        recView.layoutManager = LinearLayoutManager(this)
        mRepoListAdapter = RepoListAdapter(
            this,
            query.findAll()
        )
        recView.adapter = mRepoListAdapter
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            searchIcon.id -> {
                startSearch()
            }
            backIcon.id -> {
                stopSearch()
            }
            retryBtn.id -> {
                getRepoList()
            }
        }
    }

    override fun showLoading() {
        refreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        refreshLayout.isRefreshing = false
    }

    override fun showRetry() {}

    override fun hideRetry() {}

    override fun showError(message: String) {
        runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
    }

    override fun context(): Context {
        return this
    }

    override fun onRefresh() {
        stopSearch()
        getRepoList()
        refreshLayout.isRefreshing = false
    }

    /*Network Connection Callback received from Connectivity Receiver (Broadcast Receiver)*/
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            networkLayout.visibility = View.VISIBLE
        } else {
            networkLayout.visibility = View.GONE
            noNetworkLayout.visibility = View.GONE
            getRepoList()
        }
    }
}