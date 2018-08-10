package by.d1makrat.microblogging_app.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.d1makrat.microblogging_app.R
import by.d1makrat.microblogging_app.R.layout.fragment_add
import by.d1makrat.microblogging_app.presenter.fragment.AddPresenter
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment: Fragment(), AddPresenter.View {

    private val addPresenter = AddPresenter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        addPresenter.attachView(this)

        val rootview = inflater!!.inflate(fragment_add, container, false)
        rootview.button_add_post.setOnClickListener{
            addPresenter.addPost(editText_message.text.toString())
        }
        rootview.editText_message.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                rootview.button_add_post.isEnabled = true
            }
        })

        return rootview
    }

    override fun onDestroyView() {
        addPresenter.detachView()
        super.onDestroyView()
    }

    override fun showError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showOverLoadError() {
        Toast.makeText(activity, R.string.overload_error, Toast.LENGTH_LONG).show()
    }

    override fun successfullyAdded() {
        Toast.makeText(activity, R.string.successfully_added, Toast.LENGTH_LONG).show()
    }
}
