package by.d1makrat.microblogging_app.ui.fragment

import by.d1makrat.microblogging_app.presenter.fragment.PostsPresenter
import by.d1makrat.microblogging_app.presenter.fragment.AllPostsPresenter

class AllPostsFragment: PostsFragment(AllPostsPresenter()), PostsPresenter.View {

    override fun loadItems(offset: Int, limit: Int) {
        if (offset == 0) {
            adapter.addHeader()
        } else {
            adapter.addFooter()
        }
        (presenter as AllPostsPresenter).getPosts(offset, limit)
    }
}
