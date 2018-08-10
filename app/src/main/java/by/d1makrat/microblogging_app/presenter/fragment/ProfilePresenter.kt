package by.d1makrat.microblogging_app.presenter.fragment

import by.d1makrat.microblogging_app.model.User
import by.d1makrat.microblogging_app.workers.FirebaseAuthenticationWorker
import by.d1makrat.microblogging_app.workers.FirebaseRealtimeDatabaseWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfilePresenter {

    private var view: ProfilePresenter.View? = null

    fun detachView() {
        view = null
    }

    fun attachView(view: ProfilePresenter.View) {
        this.view = view
    }

    fun loadUserInfo(){
        val firebaseRealtimeDatabaseWorker = FirebaseRealtimeDatabaseWorker()

        val uid = FirebaseAuthenticationWorker().getCurrentUserUid()
        firebaseRealtimeDatabaseWorker.getCurrentUserInfo(uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                        data -> view?.showUserInfo(data)
                        },
                        {
                            error -> view?.showError(error.message)
                        }
                )

    }

    interface View {
        fun showError(message: String?)
        fun showUserInfo(user: User)
    }
}
