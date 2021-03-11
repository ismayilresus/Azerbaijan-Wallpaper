package com.thecheckpoint.azerbaijanwallpaper

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class FirebaseRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    var lastVisible: DocumentSnapshot? = null
    private val pageSize: Long = 6


    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
    fun queryWallpaper(): Task<QuerySnapshot> {
        if(lastVisible == null){
            //load first page
            return firebaseFirestore
                .collection("Wallpapers")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(9)
                .get()
        }else{
            //load next page
            return firebaseFirestore
                .collection("Wallpapers")
                .orderBy("date", Query.Direction.DESCENDING)
                .startAfter(lastVisible!!)
                .limit(pageSize)
                .get()
        }
    }
}