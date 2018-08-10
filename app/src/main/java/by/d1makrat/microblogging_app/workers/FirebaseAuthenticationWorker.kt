package by.d1makrat.microblogging_app.workers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Single


class FirebaseAuthenticationWorker {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUpNewUser(mail: String, password: String): Single<String> {
        return Single.create<String>{singleEmitter ->
            mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    //Registration OK
                   singleEmitter.onSuccess(mAuth.currentUser!!.uid)
                } else {
                    //Registration error
                    singleEmitter.onError(task.exception!!)
                }
            }
        }
    }

    fun signInUser(mail: String, password: String): Completable {
        return Completable.create{emitter ->
            mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                   emitter.onComplete()
                }
                else {
                    emitter.onError(task.exception!!)
                }
            }
        }
    }

    fun getCurrentUserUid(): String? {
       return mAuth.currentUser?.uid
    }

    fun isLogged(): Boolean{
        return mAuth.currentUser != null
    }

    fun signOut(){
        mAuth.signOut()
    }
}
