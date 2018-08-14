package by.d1makrat.microblogging_app.presenter.fragment

import by.d1makrat.microblogging_app.model.Post

open class PostsPresenter {
    var offset: Int = 0
    var limit: Int = 1

    var view: PostsPresenter.View? = null

    fun detachView() {
        view = null
    }

    fun attachView(view: PostsPresenter.View) {
        this.view = view
    }

    fun onBindViewAtPosition(listRowView: ListRowView, post: Post?){
        listRowView.setBody(post!!.body)
        listRowView.setDate(post.unixTime)
        listRowView.setNameAndSurname(post.userName, post.userSurname)
    }

    interface ListRowView {
        fun setBody(body: String)
        fun setDate(unixTime: Long)
        fun setNameAndSurname(name: String, surname: String)
    }

    interface View {
        fun loadedSuccessfully(post: Post)
        fun allIsLoaded()
        fun showError(message: String?)
    }
}
