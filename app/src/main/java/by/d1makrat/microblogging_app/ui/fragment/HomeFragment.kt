package by.d1makrat.microblogging_app.ui.fragment

import by.d1makrat.microblogging_app.presenter.fragment.HomePresenter

class HomeFragment: PostsFragment(HomePresenter()) {

    override fun loadItems(offset: Int, limit: Int) {
        if (offset == 0) {
            adapter.addHeader()
        } else {
            adapter.addFooter()
        }
        (presenter as HomePresenter).getPostsByUser(offset, limit)
    }
}
