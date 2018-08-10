package by.d1makrat.microblogging_app.presenter.activity

import by.d1makrat.microblogging_app.workers.FirebaseAuthenticationWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignInPresenter {

    private var view: View? = null

    fun detachView(){
        view = null
    }

    fun attachView(view: View){
        this.view = view
    }

    fun signInUser(mail: String, password: String) {

        val firebaseAuthenticationWorker = FirebaseAuthenticationWorker()
        firebaseAuthenticationWorker.signInUser(mail, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.startMainScreen()
                        },
                        {
                            error -> view?.showError(error.message)
                        }
                )
    }

    interface View {
        fun showError(message: String?)
        fun startMainScreen()
    }
}
