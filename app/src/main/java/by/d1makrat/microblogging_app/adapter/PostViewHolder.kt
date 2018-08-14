package by.d1makrat.microblogging_app.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import by.d1makrat.microblogging_app.FORMATTING_DATE_PATTERN
import by.d1makrat.microblogging_app.presenter.fragment.PostsPresenter
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PostsPresenter.ListRowView {

    private val bodyView = itemView.textView_post_body
    private val dateView = itemView.textView_post_date
    private val nameAndSurnameView = itemView.textView_name_and_surname

    override fun setBody(body: String) {
        bodyView.text = body
    }

    override fun setDate(unixTime: Long) {
        val date = java.util.Date(TimeUnit.SECONDS.toMillis(unixTime))
        val simpleDateFormat = SimpleDateFormat(FORMATTING_DATE_PATTERN, Locale.ENGLISH)
        dateView.text = simpleDateFormat.format(date)
    }

    override fun setNameAndSurname(name: String, surname: String){
        nameAndSurnameView.text = "$name $surname"
    }
}
