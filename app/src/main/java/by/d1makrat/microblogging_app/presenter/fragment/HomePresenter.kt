package by.d1makrat.microblogging_app.presenter.fragment

import android.util.Log
import by.d1makrat.microblogging_app.model.Post
import by.d1makrat.microblogging_app.workers.FirebaseAuthenticationWorker
import by.d1makrat.microblogging_app.workers.FirebaseRealtimeDatabaseWorker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomePresenter {

    var offset: Int = 0
    var limit: Int = 1

    private var view: HomePresenter.View? = null
    private var items: MutableList<Post?> = ArrayList()

    fun detachView() {
        view = null
    }

    fun attachView(view: HomePresenter.View) {
        this.view = view
    }

    fun getPostsByUser(offset: Int, limit: Int){

        this.offset = offset
        this.limit = limit

        val uid = FirebaseAuthenticationWorker().getCurrentUserUid()
        val firebaseRealtimeDatabaseWorker = FirebaseRealtimeDatabaseWorker()

        firebaseRealtimeDatabaseWorker.getPostsByUser(uid!!, UserEventListener())
    }

    private fun getPostById(id: String) {

        val firebaseRealtimeDatabaseWorker = FirebaseRealtimeDatabaseWorker()
        firebaseRealtimeDatabaseWorker.getPostById(id, PostEventListener())
    }

    fun getItemCount(): Int {
        return items.count()
    }

    fun onBindViewAtPosition(listRowView: ListRowView, position: Int){
        val post = items[position]

        listRowView.setBody(post!!.body)
        listRowView.setDate(post.unixtime)
        listRowView.setNameAndSurname(post.userName, post.userSurname)
    }


    inner class UserEventListener: ValueEventListener{
        override fun onCancelled(databaseError: DatabaseError) {
            view?.showError(databaseError.message)
            view?.showEmptyList()
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val postNumbersList = dataSnapshot.children.asSequence().toList().reversed().filterIndexed{index, _ -> index >= offset && index < offset + limit}

            if (postNumbersList.isEmpty()){
                view?.showEmptyList()
            }
            else {
                postNumbersList.forEach{it -> getPostById(it.key!!)}
            }
        }
    }

    inner class PostEventListener: ValueEventListener{
        override fun onCancelled(databaseError: DatabaseError) {
            view?.showError(databaseError.message)
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val body = dataSnapshot.child("body").value.toString()
            val unixtime = dataSnapshot.child("unixtime").value.toString().toLong()
            val userId = dataSnapshot.child("user").value.toString()
            val name = dataSnapshot.child("name").value.toString()
            val surname = dataSnapshot.child("surname").value.toString()

            view?.removeAllHeadersAndFooters()
            addItem(Post(body, unixtime, userId, name, surname))
            view?.loadedSuccessfully()
        }
    }

    fun addItem(post: Post?){
        items.add(post)
        Log.e(this.toString(), items.lastIndex.toString() + " " + items.last().toString())
    }

    fun removeItem(position: Int){
        items.removeAt(position)
    }

    interface ListRowView {
        fun setBody(body: String)
        fun setDate(unixtime: Long)
        fun setNameAndSurname(name: String, surname: String)
    }

    interface View {
        fun addHeaderInList()
        fun addFooterInList()
        fun loadedSuccessfully()
        fun removeAllHeadersAndFooters()
        fun showEmptyList()
        fun showError(message: String?)
    }
}
