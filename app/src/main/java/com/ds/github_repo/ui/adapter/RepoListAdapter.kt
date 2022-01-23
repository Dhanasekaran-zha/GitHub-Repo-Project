package com.ds.github_repo.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ds.github_repo.R
import com.ds.github_repo.data.response.GitHubURLResponse
import com.ds.github_repo.utils.ColorUtil
import com.ds.github_repo.utils.TextUtil
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_repo_list.view.*
import org.json.JSONObject

class RepoListAdapter(val context: Context, data: OrderedRealmCollection<GitHubURLResponse?>?) :
    RealmRecyclerViewAdapter<GitHubURLResponse, RepoListAdapter.RepoListViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_repo_list, parent, false)
        return RepoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoListViewHolder, position: Int) {
        holder.bindDataToView(data!![position])
    }

    inner class RepoListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var repoName = view.repoName
        var repoDesc=view.repoDesc
        var repoOwner=view.repoOwner
        var repoImg=view.repoImg
        var language=view.language
        var starred=view.starred
        var languageColor=view.languageColorTxt
        var gitHubColor=JSONObject(ColorUtil.loadJSONFromAsset(context))


        fun bindDataToView(item: GitHubURLResponse) {
            repoName.text = item.name
            repoOwner.text=item.owner?.login
            repoDesc.text=item.description
            language.text=item.language
            starred.text=TextUtil.convertLikes(item.watchersCount!!) //To view the count in short formats eg:1000 as 1k
            languageColor.setBackgroundColor(Color.parseColor(gitHubColor.optString(item.language,"#02f88c")))  //To get the color code based on the language
            Glide.with(context).load(item.owner?.avatarUrl).into(repoImg)
        }
    }
}