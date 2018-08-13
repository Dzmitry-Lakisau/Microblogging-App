package by.d1makrat.microblogging_app.presenter.activity

import by.d1makrat.microblogging_app.workers.FirebaseAuthenticationWorker
import by.d1makrat.microblogging_app.workers.FirebaseRealtimeDatabaseWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter {

    private var view: View? = null

    fun detachView() {
        view = null
    }

    fun attachView(view: View) {
        this.view = view
    }


    fun getUserInfo(){
        val firebaseRealtimeDatabaseWorker = FirebaseRealtimeDatabaseWorker()

        val uid = FirebaseAuthenticationWorker().getCurrentUserUid()
        firebaseRealtimeDatabaseWorker.getCurrentUserInfo(uid!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            data -> view?.showUserInfoInHeader(data.name, data.surname, data.mail)
                        },
                        {}
                )

    }


    interface View {
        fun showUserInfoInHeader(userName: String, userSurname: String, userMail: String)
    }
}
