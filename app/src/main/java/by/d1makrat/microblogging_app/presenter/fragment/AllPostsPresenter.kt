package by.d1makrat.microblogging_app.presenter.fragment

import by.d1makrat.microblogging_app.*
import by.d1makrat.microblogging_app.model.Post
import by.d1makrat.microblogging_app.workers.FirebaseRealtimeDatabaseWorker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AllPostsPresenter: PostsPresenter() {

    fun getPosts(offset: Int, limit: Int){

        this.offset = offset
        this.limit = limit

        val firebaseRealtimeDatabaseWorker = FirebaseRealtimeDatabaseWorker()

        firebaseRealtimeDatabaseWorker.getPosts(PostsListEventListener())
    }

    inner class PostsListEventListener: ValueEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            view?.showError(databaseError.message)
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val postsList = dataSnapshot.children.asSequence().toList().reversed().filterIndexed{ index, _ -> index >= offset && index < offset + limit}

            if (postsList.isEmpty()){
                view?.allIsLoaded()
            }
            else {
                postsList.forEach{ it ->
                    val body =  (it.value as HashMap<*, *>)[BODY_KEY].toString()
                    val unixTime =  (it.value as HashMap<*, *>)[UNIX_TIME_KEY].toString().toLong()
                    val userId =  (it.value as HashMap<*, *>)[USER_KEY].toString()
                    val name = (it.value as HashMap<*, *>)[NAME_KEY].toString()
                    val surname =  (it.value as HashMap<*, *>)[SURNAME_KEY].toString()

                    view?.loadedSuccessfully(Post(body, unixTime, userId, name, surname))
                }
            }
        }
    }
}
