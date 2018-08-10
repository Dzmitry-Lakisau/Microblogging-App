package by.d1makrat.microblogging_app.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import by.d1makrat.microblogging_app.R
import by.d1makrat.microblogging_app.model.Gender
import by.d1makrat.microblogging_app.presenter.activity.SignUpPresenter
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity: Activity(), SignUpPresenter.View {

    private val signUpPresenter = SignUpPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)

        signUpPresenter.attachView(this)

        spinner_gender.adapter = ArrayAdapter<Gender>(this, android.R.layout.simple_spinner_item, Gender.values())

        button_sign_up.setOnClickListener {
            signUpPresenter.signUpNewUser(editText_mail.text.toString(), editText_password.text.toString(), editText_name.text.toString(),
                    editText_surname.text.toString(), spinner_gender.selectedItem.toString(), editText_age.text.toString())
        }
    }

    override fun onDestroy() {
        signUpPresenter.detachView()
        super.onDestroy()
    }

    override fun isAllFieldsAreFilled(): Boolean {
        return !TextUtils.isEmpty(editText_mail.text.toString()) && !TextUtils.isEmpty(editText_password.text.toString()) &&
                !TextUtils.isEmpty(editText_name.text.toString()) && !TextUtils.isEmpty(editText_surname.text.toString()) &&
                !TextUtils.isEmpty(editText_age.text.toString())
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showNotFilledFieldsMessage() {
        Toast.makeText(this, getString(R.string.not_all_fieds_are_filled), Toast.LENGTH_LONG).show()
    }

    override fun registeredSuccessfully() {
        Toast.makeText(this, getString(R.string.registered_successfully), Toast.LENGTH_LONG).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showWrongNumberMessage() {
        Toast.makeText(this, getString(R.string.wrong_number_input), Toast.LENGTH_LONG).show()
    }
}
