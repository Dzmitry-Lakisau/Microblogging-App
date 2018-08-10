package by.d1makrat.microblogging_app.workers

import android.util.Log
import by.d1makrat.microblogging_app.model.User
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Single
import java.io.IOException

class FirebaseRealtimeDatabaseWorker {

    private var mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun createNewUser(uid: String, user: User): Completable {
        try {
            val userReference: DatabaseReference = mDatabase.getReference("users").child(uid)
            userReference.child("mail").setValue(user.mail)
            userReference.child("name").setValue(user.name)
            userReference.child("surname").setValue(user.surname)
            userReference.child("gender").setValue(user.gender)
            userReference.child("age").setValue(user.age)
        }
        catch (e: IOException){
            return Completable.error(e)
        }
        return Completable.complete()
    }

    fun getCurrentUserInfo(uid: String): Single<User> {
        return Single.create<User> { singleEmitter ->
                try {
                    val userReference: DatabaseReference = mDatabase.getReference("users").child(uid)
                    userReference.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            throw Exception(databaseError.message)
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val mail = dataSnapshot.child("mail").value.toString()
                            val name = dataSnapshot.child("name").value.toString()
                            val surname = dataSnapshot.child("surname").value.toString()
                            val gender = dataSnapshot.child("gender").value.toString()
                            val age = dataSnapshot.child("age").value.toString().toInt()

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

            Log.d(this.toString(), Thread.currentThread().name)

            val query = mDatabase.getReference("posts")
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    throw Exception(databaseError.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    postNumber = dataSnapshot.childrenCount.inc()

                    var postReference: DatabaseReference = query.child(postNumber.toString())
                    postReference.child("body").setValue(body)
                    postReference.child("unixtime").setValue(currentTime)
                    postReference.child("user").setValue(user.uid)
                    postReference.child("name").setValue(user.name)
                    postReference.child("surname").setValue(user.surname)

                    postReference = mDatabase.getReference("users").child(user.uid)
                    postReference.child("posts").child(postNumber.toString()).setValue(true)

                    Log.d(this.toString(),"in onDataChange " + Thread.currentThread().name + System.currentTimeMillis().toString())
                }
            })

            Log.d(this.toString(), "after onDataChange " + Thread.currentThread().name + System.currentTimeMillis().toString())
        }
        catch (e: IOException){
            return Completable.error(e)
        }
        Log.d(this.toString(), "before complete " + Thread.currentThread().name + System.currentTimeMillis().toString())
        return Completable.complete()
    }

    fun getPostsByUser(uid: String, listener: ValueEventListener){
        mDatabase.getReference("users").child(uid).child("posts").orderByKey().addListenerForSingleValueEvent(listener)
    }

    fun getPostById(id: String, listener: ValueEventListener){
        mDatabase.getReference("posts").child(id).addListenerForSingleValueEvent(listener)
    }
}
