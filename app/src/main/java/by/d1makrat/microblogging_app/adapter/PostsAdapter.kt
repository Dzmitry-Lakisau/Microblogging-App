package by.d1makrat.microblogging_app.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import by.d1makrat.microblogging_app.R
import by.d1makrat.microblogging_app.model.Post
import by.d1makrat.microblogging_app.presenter.fragment.HomePresenter

class PostsAdapter(val presenter: HomePresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val HEADER = 0
    private val ITEM = 1
    private val FOOTER = 2
    private val EMPTY_HEADER = 3

    private var isFooterAdded = false
    private var isHeaderAdded = false
    private var isEmptyHeaderAdded = false

    override fun getItemViewType(position: Int): Int {
        Log.e(this.toString(), position.toString() + " " + isFooterAdded.toString())
        if (position == 0 && isHeaderAdded){
            return HEADER
        }
        else if (position == itemCount-1 && isFooterAdded){
            return FOOTER
        }
        else if (position == 0 && isEmptyHeaderAdded){
            return EMPTY_HEADER
        }
        else return ITEM
    }

    override fun getItemCount(): Int {
        return presenter.getItemCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.e(this.toString(), "onCreateViewHolder for " + viewType.toString())

        return when(viewType){
            HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
            FOOTER -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false))
            EMPTY_HEADER -> EmptyHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_empty_header, parent, false))
            else -> PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(this.toString(), "bindviewholder " + position.toString())

        when(getItemViewType(position)){
            HEADER ->{

            }
            ITEM ->{
                presenter.onBindViewAtPosition(holder as PostViewHolder, position)
            }
            FOOTER ->{

            }
            EMPTY_HEADER ->{

            }
        }
    }

    fun addItem(post: Post){
        presenter.addItem(post)
        notifyItemInserted(itemCount-1)
    }

    fun addHeader(){
        Log.e(this.toString(), "header is added")
        isHeaderAdded = true
        presenter.addItem(null)
        notifyItemInserted(0)
    }

    fun addFooter(){
        Log.e(this.toString(), "footer is added")
        isFooterAdded = true
        presenter.addItem(null)
        notifyItemInserted(itemCount-1)
//        notifyDataSetChanged()
    }

    fun addEmptyHeader(){
        isEmptyHeaderAdded = true
        presenter.addItem(null)
        notifyItemInserted(itemCount-1)
    }

    fun removeAllHeadersAndFooters(){
        removeHeader()
        removeFooter()
        removeEmptyHeader()
    }

    private fun removeHeader(){
        if (isHeaderAdded){
            val position = 0
            presenter.removeItem(position)
            notifyItemRemoved(position)
            isHeaderAdded = false
            Log.e(this.toString(), "header is removed")
        }
    }

    private fun removeFooter(){
        if (isFooterAdded){
            val position = itemCount - 1
            presenter.removeItem(position)
            notifyItemRemoved(position)
            isFooterAdded = false
            Log.e(this.toString(), "footer is removed")
            Log.e(this.toString(), presenter.getItemCount().toString())
        }
    }

    private fun removeEmptyHeader(){
        if (isEmptyHeaderAdded){
            val position = 0
            presenter.removeItem(position)
            notifyItemRemoved(position)
            isEmptyHeaderAdded = false
        }
    }

    fun isLoading(): Boolean{
        return isHeaderAdded || isFooterAdded
    }
}
