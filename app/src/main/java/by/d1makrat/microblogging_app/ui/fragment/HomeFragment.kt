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
import by.d1makrat.microblogging_app.presenter.fragment.HomePresenter
import kotlinx.android.synthetic.main.list.view.*

class HomeFragment: Fragment(), HomePresenter.View {

    private var offset = 0
    private val LIMIT = 5

    private val homePresenter =  HomePresenter()
    val adapter = PostsAdapter(homePresenter)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        homePresenter.attachView(this)

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
        homePresenter.detachView()
        super.onDestroyView()
    }

    private fun loadItems(offset: Int, limit: Int){
        if (offset == 0){
            adapter.addHeader()
        }
        else{
            adapter.addFooter()
        }
        homePresenter.getPostsByUser(offset, limit)
    }

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
