package by.d1makrat.microblogging_app.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.d1makrat.microblogging_app.R
import by.d1makrat.microblogging_app.model.User
import by.d1makrat.microblogging_app.presenter.fragment.ProfilePresenter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment(), ProfilePresenter.View {

    private val profilePresenter = ProfilePresenter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        profilePresenter.attachView(this)

        val rootView = inflater!!.inflate(R.layout.fragment_profile, container, false)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        profilePresenter.loadUserInfo()
    }

    override fun onDestroyView() {
        profilePresenter.detachView()
        super.onDestroyView()
    }

    override fun showError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showUserInfo(user: User) {
        textView_mail.text = user.mail
        textView_name.text = user.name
        textView_surname.text = user.surname
        textView_gender.text = user.gender
        textView_age.text = user.age.toString()
    }
}
