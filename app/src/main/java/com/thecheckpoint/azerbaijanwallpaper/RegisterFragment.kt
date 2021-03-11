package com.thecheckpoint.azerbaijanwallpaper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        if(firebaseAuth.currentUser == null){

        register_text.text = "Yeni Hesab yaradılır"
        firebaseAuth.signInAnonymously().addOnCompleteListener() {
            if(it.isSuccessful){
                register_text.text = "Hesab yaradıldı. Daxil olunur"
                navController!!.navigate((R.id.action_registerFragment_to_homeFragment))
            }else{
                register_text.text = "Xəta : ${it.exception!!.message}"
            }

        }

        } else{
            navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
        }
}}