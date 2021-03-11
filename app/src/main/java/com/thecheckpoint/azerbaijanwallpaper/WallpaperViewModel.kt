package com.thecheckpoint.azerbaijanwallpaper

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class WallpaperViewModel: ViewModel(){
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
    private val wallpapersList: MutableLiveData<List<WallpaperModel>> by lazy {
        MutableLiveData<List<WallpaperModel>>().also{
            loadWallpapersData()
        }
    }


    fun getWallpapersList(): LiveData<List<WallpaperModel>>{
        return wallpapersList
    }

    fun loadWallpapersData(){
        //query data
        firebaseRepository.queryWallpaper().addOnCompleteListener {
            if(it.isSuccessful){
                val result = it.result
                if(result!!.isEmpty){
                    //no more to load
                } else{
                    //result ready to load
                    if(wallpapersList.value == null){

                        //loading first page
                        wallpapersList.value = result.toObjects(WallpaperModel::class.java)

                    }else{
                        // next page
                        wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(WallpaperModel::class.java))
                    }

                    val lastItem: DocumentSnapshot = result.documents[result.size() - 1]
                    firebaseRepository.lastVisible = lastItem
                }
            }else{
                //error
                Log.d("VIEW_MODEL_LOG", "Error: ${it.exception!!.message}")
            }
        }
    }
}
