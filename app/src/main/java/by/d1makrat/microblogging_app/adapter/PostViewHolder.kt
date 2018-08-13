package by.d1makrat.microblogging_app.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import by.d1makrat.microblogging_app.presenter.fragment.PostsPresenter
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PostsPresenter.ListRowView {

    val bodyView = itemView.textView_post_body
    val dateView = itemView.textView_post_date
    val nameAndSurnameView = itemView.textView_name_and_surname

    override fun setBody(body: String) {
        bodyView.text = body
    }

    override fun setDate(unixtime: Long) {
        val date = java.util.Date(TimeUnit.SECONDS.toMillis(unixtime))
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy, HH:mm:ss, EEEE", Locale.ENGLISH)
        dateView.text = simpleDateFormat.format(date)
    }

    override fun setNameAndSurname(name: String, surname: String){
        nameAndSurnameView.text = "$name $surname"
    }
}
