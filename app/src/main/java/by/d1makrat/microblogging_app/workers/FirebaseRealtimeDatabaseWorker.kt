package by.d1makrat.microblogging_app.workers

import by.d1makrat.microblogging_app.*
import by.d1makrat.microblogging_app.model.User
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Single
import java.io.IOException

class FirebaseRealtimeDatabaseWorker {

    private var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun createNewUser(uid: String, user: User): Completable {
        try {
            val userReference: DatabaseReference = mDatabase.getReference(USERS_KEY).child(uid)
            userReference.child(MAIL_KEY).setValue(user.mail)
            userReference.child(NAME_KEY).setValue(user.name)
            userReference.child(SURNAME_KEY).setValue(user.surname)
            userReference.child(GENDER_KEY).setValue(user.gender)
            userReference.child(AGE_KEY).setValue(user.age)
        }
        catch (e: IOException){
            return Completable.error(e)
        }
        return Completable.complete()
    }

    fun getCurrentUserInfo(uid: String): Single<User> {
        return Single.create<User> { singleEmitter ->
                try {
                    val userReference: DatabaseReference = mDatabase.getReference(USERS_KEY).child(uid)
                    userReference.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            throw Exception(databaseError.message)
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val mail = dataSnapshot.child(MAIL_KEY).value.toString()
                            val name = dataSnapshot.child(NAME_KEY).value.toString()
                            val surname = dataSnapshot.child(SURNAME_KEY).value.toString()
                            val gender = dataSnapshot.child(GENDER_KEY).value.toString()
                            val age = dataSnapshot.child(AGE_KEY).value.toString().toInt()

                            singleEmitter.onSuccess(User(uid, mail, name, surname, gender, age))
                        }
                    })
                }
                catch (e: Exception){
                    singleEmitter.onError(e)
                }
        }
    }

    fun addPost(user: User, body: String, currentTime: Long): Completable {
        try {
            var postNumber: Long

            val query = mDatabase.getReference(POSTS_KEY)
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    throw Exception(databaseError.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    postNumber = dataSnapshot.childrenCount.inc()

                    var postReference: DatabaseReference = query.child(postNumber.toString())
                    postReference.child(BODY_KEY).setValue(body)
                    postReference.child(UNIX_TIME_KEY).setValue(currentTime)
                    postReference.child(USERS_KEY).setValue(user.uid)
                    postReference.child(NAME_KEY).setValue(user.name)
                    postReference.child(SURNAME_KEY).setValue(user.surname)

                    postReference = mDatabase.getReference(USERS_KEY).child(user.uid)
                    postReference.child(POSTS_KEY).child(postNumber.toString()).setValue(true)
                }
            })
        }
        catch (e: IOException){
            return Completable.error(e)
        }
        return Completable.complete()
    }

    fun getPostsByUser(uid: String, listener: ValueEventListener){
        mDatabase.getReference(USERS_KEY).child(uid).child(POSTS_KEY).orderByKey().addListenerForSingleValueEvent(listener)
    }

    fun getPostById(id: String, listener: ValueEventListener){
        mDatabase.getReference(POSTS_KEY).child(id).addListenerForSingleValueEvent(listener)
    }

    fun getPosts(listener: ValueEventListener){
        mDatabase.getReference(POSTS_KEY).orderByKey().addListenerForSingleValueEvent(listener)
    }
}
