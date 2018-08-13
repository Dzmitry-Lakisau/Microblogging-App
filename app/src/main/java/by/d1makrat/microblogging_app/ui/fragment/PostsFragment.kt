package by.d1makrat.microblogging_app.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.d1makrat.microblogging_app.R
import by.d1makrat.microblogging_app.adapter.PostsAdapter
import by.d1makrat.microblogging_app.model.Post
import by.d1makrat.microblogging_app.presenter.fragment.PostsPresenter
import kotlinx.android.synthetic.main.list.view.*

abstract class PostsFragment(var presenter: PostsPresenter): Fragment(), PostsPresenter.View {

    private var offset = 0
    private val LIMIT = 5

    var adapter = PostsAdapter(presenter)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        presenter.attachView(this)

        val rootView = inflater!!.inflate(R.layout.fragment_home, container, false)
        rootView.rv.adapter = adapter
        rootView.rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rootView.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = recyclerView!!.layoutManager.childCount
                val totalItemCount = recyclerView.layoutManager.itemCount
                val firstVisibleItemPosition =  (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (firstVisibleItemPosition + visibleItemCount >= totalItemCount) {
                    if (!adapter.isLoading() && !adapter.allIsLoaded) {
                        offset += LIMIT
                        loadItems(offset, LIMIT)
                    }
                }
            }
        })

        loadItems(offset, LIMIT)

        return rootView
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    abstract fun loadItems(offset: Int, limit: Int)

    override fun showError(message: String?) {
        adapter.removeAllHeadersAndFooters()

        if (adapter.isEmpty()) {
            adapter.addEmptyHeader()
        }

        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun allIsLoaded(){
        adapter.removeAllHeadersAndFooters()

        if (adapter.isEmpty()) {
            adapter.addEmptyHeader()
        }

        adapter.allIsLoaded = true

        Toast.makeText(activity, R.string.nothing_to_load, Toast.LENGTH_LONG).show()
    }

    override fun loadedSuccessfully(post: Post) {
        adapter.removeAllHeadersAndFooters()
        adapter.addItem(post)
    }
}
