package by.d1makrat.microblogging_app.presenter

open class BasicPresenter {

    protected var view: BasicPresenter.View? = null

    fun detachView(){
        view = null
    }

    fun attachView(view: BasicPresenter.View){
        this.view = view
    }

    interface View {

        fun showError(message: String?)
    }
}
