package com.thecheckpoint.azerbaijanwallpaper

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.adView


class HomeFragment : Fragment(), (WallpaperModel) -> Unit {

    private val firebaseRepository = FirebaseRepository()
    private var navController: NavController? = null


    private var wallpapersList: List<WallpaperModel> = ArrayList()
    private val wallpapersListAdapter: WallpapersListAdapter = WallpapersListAdapter(wallpapersList,this)

    private var isLoading: Boolean = true

    private val wallpaperViewModel: WallpaperViewModel by viewModels()
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        MobileAds.initialize(this@HomeFragment.context)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this@HomeFragment.context)
        mInterstitialAd.adUnitId = "ca-app-pub-4468388749483480/2828904783"
        mInterstitialAd.loadAd(AdRequest.Builder().build())


        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.title = "Azerbaijan Wallpapers"

        navController = Navigation.findNavController(view)

        // User login check
        if(firebaseRepository.getUser() == null){
            //not log in
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment3)
        }

        wallpaper_list_view.setHasFixedSize(true)
        wallpaper_list_view.layoutManager = GridLayoutManager(context, 3)
        wallpaper_list_view.adapter = wallpapersListAdapter

        wallpaper_list_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!isLoading){
                        wallpaperViewModel.loadWallpapersData()
                        isLoading = true
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        wallpaperViewModel.getWallpapersList().observe(viewLifecycleOwner, Observer {
            wallpapersList = it
            wallpapersListAdapter.wallpapersList = wallpapersList
            wallpapersListAdapter.notifyDataSetChanged()

            isLoading = false
        })
    }

    override fun invoke(Wallpaper: WallpaperModel) {
        //clicked wallpaper
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(Wallpaper.image)
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.")
        }
        navController!!.navigate(action)
    }

}