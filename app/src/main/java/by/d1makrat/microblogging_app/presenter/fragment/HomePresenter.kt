package by.d1makrat.microblogging_app.presenter.fragment

import by.d1makrat.microblogging_app.model.Post
import by.d1makrat.microblogging_app.workers.FirebaseAuthenticationWorker
import by.d1makrat.microblogging_app.workers.FirebaseRealtimeDatabaseWorker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomePresenter: PostsPresenter() {

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

    inner class UserEventListener: ValueEventListener{
        override fun onCancelled(databaseError: DatabaseError) {
            view?.showError(databaseError.message)
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val postNumbersList = dataSnapshot.children.asSequence().toList().reversed().filterIndexed{index, _ -> index >= offset && index < offset + limit}

            if (postNumbersList.isEmpty()){
                view?.allIsLoaded()
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

            view?.loadedSuccessfully(Post(body, unixtime, userId, name, surname))
        }
    }
}
