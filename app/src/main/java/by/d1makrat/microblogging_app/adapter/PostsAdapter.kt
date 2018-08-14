package by.d1makrat.microblogging_app.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import by.d1makrat.microblogging_app.R
import by.d1makrat.microblogging_app.model.Post
import by.d1makrat.microblogging_app.presenter.fragment.PostsPresenter

class PostsAdapter(val presenter: PostsPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val HEADER = 0
    private val ITEM = 1
    private val FOOTER = 2
    private val EMPTY_HEADER = 3

    var allIsLoaded = false

    private var isFooterAdded = false
    private var isHeaderAdded = false
    private var isEmptyHeaderAdded = false

    private var items: MutableList<Post?> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && isHeaderAdded){
            HEADER
        }
        else if (position == itemCount-1 && isFooterAdded){
            FOOTER
        }
        else if (position == 0 && isEmptyHeaderAdded){
            EMPTY_HEADER
        }
        else ITEM
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
            FOOTER -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false))
            EMPTY_HEADER -> EmptyHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_empty_header, parent, false))
            else -> PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            HEADER ->{

            }
            ITEM ->{
                presenter.onBindViewAtPosition(holder as PostViewHolder, items[position])
            }
            FOOTER ->{

            }
            EMPTY_HEADER ->{

            }
        }
    }

    fun addItem(post: Post?){
        items.add(post)
        notifyItemInserted(itemCount-1)
    }

    private fun removeItem(position: Int){
        items.removeAt(position)
    }

    fun addHeader(){
        isHeaderAdded = true
        addItem(null)
    }

    fun addFooter(){
        isFooterAdded = true
        addItem(null)
    }

    fun addEmptyHeader(){
        isEmptyHeaderAdded = true
        addItem(null)
    }

    fun removeAllHeadersAndFooters(){
        removeHeader()
        removeFooter()
        removeEmptyHeader()
    }

    private fun removeHeader(){
        if (isHeaderAdded){
            val position = 0
            removeItem(position)
            notifyItemRemoved(position)
            isHeaderAdded = false
        }
    }

    private fun removeFooter(){
        if (isFooterAdded){
            val position = itemCount - 1
            removeItem(position)
            notifyItemRemoved(position)
            isFooterAdded = false
        }
    }

    private fun removeEmptyHeader(){
        if (isEmptyHeaderAdded){
            val position = 0
            removeItem(position)
            notifyItemRemoved(position)
            isEmptyHeaderAdded = false
        }
    }

    fun isLoading(): Boolean{
        return isHeaderAdded || isFooterAdded
    }

    fun isEmpty(): Boolean {
        return getItemViewType(0) != ITEM || items.size == 0
    }
}
