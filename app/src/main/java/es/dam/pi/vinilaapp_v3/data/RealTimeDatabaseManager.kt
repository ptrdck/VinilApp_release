package es.dam.pi.vinilaapp_v3.data

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.dam.pi.vinilaapp_v3.ui.model.UserData

class RealTimeDatabaseManager {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference()

    fun createUser(user: UserData) {
        database.child("users").child(user.userId).setValue(user)
    }

    fun updateUser(user: UserData) {
        database.child("users").child(user.userId).setValue(user)
    }

    fun deleteUser(userId: String) {
        database.child("users").child(userId).removeValue()
    }

    suspend fun readUser(userId: String): UserData? = suspendCancellableCoroutine { continuation ->
        val userRef = database.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserData::class.java)
                continuation.resume(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                continuation.resumeWithException(databaseError.toException())
            }
        })
    }
}