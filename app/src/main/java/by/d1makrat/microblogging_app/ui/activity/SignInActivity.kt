package by.d1makrat.microblogging_app.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import by.d1makrat.microblogging_app.R
import by.d1makrat.microblogging_app.presenter.activity.SignInPresenter
import by.d1makrat.microblogging_app.workers.FirebaseAuthenticationWorker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity: Activity(), SignInPresenter.View {

    private val signInPresenter = SignInPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)

        signInPresenter.attachView(this)

        if(FirebaseAuthenticationWorker().isLogged())
            startMainScreen()

        button_register_now.setOnClickListener{
            startSignUpScreen()
        }

        button_sign_in.setOnClickListener{
            signInPresenter.signInUser(editText_mail.text.toString(), editText_password.text.toString())
        }
    }

    override fun onDestroy() {
        signInPresenter.detachView()
        super.onDestroy()
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun startMainScreen(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun startSignUpScreen(){
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
}
