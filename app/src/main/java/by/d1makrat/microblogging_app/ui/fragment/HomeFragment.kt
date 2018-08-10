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
import by.d1makrat.microblogging_app.presenter.fragment.HomePresenter
import kotlinx.android.synthetic.main.list.view.*

class HomeFragment: Fragment(), HomePresenter.View {

    private var offset = 0
    private val LIMIT = 3


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

                if (!adapter.isLoading()) {
                    val visibleItemCount = recyclerView!!.layoutManager.childCount
                    val totalItemCount = recyclerView.layoutManager.itemCount
                    val firstVisibleItemPosition =  (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (firstVisibleItemPosition + visibleItemCount >= totalItemCount && totalItemCount > 0) {
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
            addHeaderInList()
        }
        else{
            addFooterInList()
        }
        homePresenter.getPostsByUser(offset, limit)
    }

    override fun addHeaderInList() {
        adapter.addHeader()
    }

    override fun addFooterInList() {
        adapter.addFooter()
    }

    override fun showEmptyList() {
        adapter.removeAllHeadersAndFooters()
        adapter.addEmptyHeader()
    }

    override fun showError(message: String?) {

        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun loadedSuccessfully() {
        adapter.notifyDataSetChanged()
    }

    override fun removeAllHeadersAndFooters(){
        adapter.removeAllHeadersAndFooters()
    }
}
