package by.d1makrat.microblogging_app.presenter.activity

import by.d1makrat.microblogging_app.model.User
import by.d1makrat.microblogging_app.workers.FirebaseAuthenticationWorker
import by.d1makrat.microblogging_app.workers.FirebaseRealtimeDatabaseWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class SignUpPresenter {

    private var view: View? = null

    fun detachView(){
        view = null
    }

    fun attachView(view: View){
        this.view = view
    }

    fun signUpNewUser(mail: String, password: String, name: String, surname: String, gender: String, age: String) {

        if (view?.isAllFieldsAreFilled()!!) {

            try {
                val user = User("", mail, name, surname, gender, age.toInt())

                val firebaseAuthenticationWorker = FirebaseAuthenticationWorker()
                val firebaseRealtimeDatabaseWorker = FirebaseRealtimeDatabaseWorker()

                firebaseAuthenticationWorker.signUpNewUser(user.mail, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMapCompletable { it -> firebaseRealtimeDatabaseWorker.createNewUser(it, user) }
                        .subscribe(
                                {
                                    view?.registeredSuccessfully()
                                },
                                {
                                    error -> view?.showError(error.message)
                                }
                        )
            }
            catch (e: NumberFormatException){
                view?.showWrongNumberMessage()
            }
            catch (e: IOException){
                view?.showError(e.message)
            }
        }
        else view?.showNotFilledFieldsMessage()
    }

    interface View {
        fun isAllFieldsAreFilled(): Boolean
        fun showError(message: String?)
        fun showNotFilledFieldsMessage()
        fun registeredSuccessfully()
        fun showWrongNumberMessage()
    }
}
