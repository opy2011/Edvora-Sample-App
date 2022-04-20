package com.codewithharry.edvorasample

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.codewithharry.edvorasample.fragments.NearestFragment
import com.codewithharry.edvorasample.fragments.PastFragment
import com.codewithharry.edvorasample.fragments.UpcomingFragment
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var nearestTabItem: TabItem? = null
    private var upcomingTabItem: TabItem? = null
    private var pastTabItem: TabItem? = null
    private var progressBar: ProgressBar? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.view_pager)
        nearestTabItem = findViewById(R.id.nearest_item)
        upcomingTabItem = findViewById(R.id.upcoming_item)
        pastTabItem = findViewById(R.id.past_item)
        progressBar = findViewById(R.id.progressBarMain)

        tabLayout?.setupWithViewPager(viewPager)

        val vpAdapter: VPAdapter = VPAdapter(supportFragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vpAdapter.addFragment(NearestFragment(),"Nearest")
        vpAdapter.addFragment(UpcomingFragment(),"Upcoming")
        vpAdapter.addFragment(PastFragment(),"Past")
        viewPager?.adapter = vpAdapter

        lifecycleScope.launchWhenCreated {
            val response = try {
                RideRetrofitInstance.retrofit.getRides()
            } catch (e: IOException){
                Log.e(TAG,"IOException, you might not have internet connection")
                return@launchWhenCreated
            } catch (e: HttpException){
                Log.e(TAG,"HttpException, unexpected response")
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null){
                MainActivity.API_STATUS = true
                progressBar?.visibility = View.INVISIBLE
                val rideList: List<Ride> = response.body()!!
                sortRides(rideList)
                NearestFragment.adapter?.setRideList(NearestFragment.nearestRidesList)
                UpcomingFragment.adapter?.setRideList(UpcomingFragment.upcomingRidesList )

            } else {
                Log.e(TAG,"Response not successful")
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    @RequiresApi(Build.VERSION_CODES.N)
    fun sortRides(rides: List<Ride>?) {
        if (rides != null) {

            val sdf = SimpleDateFormat("MM/dd/yyyy HH::mm a")
            val currentDate = sdf.parse("04/20/2022 01:00 AM")

            UpcomingFragment.upcomingRidesList = mutableListOf()
            PastFragment.pastRides = mutableListOf()

            for(item in rides) {
                val nearest: Int = findClosest(item.station_path,item.station_path.size,40)
                item.distanceFromUser = if(nearest > 40) {
                    nearest - 40
                } else 40-nearest

                val strDate = sdf.parse(item.date)

                if (strDate > currentDate) {
                    UpcomingFragment.upcomingRidesList?.add(item)
                } else if (strDate < currentDate){
                    PastFragment.pastRides?.add(item)
                }
            }
            NearestFragment.nearestRidesList = rides.sortedWith(compareBy { it.distanceFromUser })
        }
    }

    private fun findClosest(arr: ArrayList<Int>, n: Int, target: Int): Int {
        // Corner cases
        if (target <= arr[0]) return arr[0]
        if (target >= arr[n - 1]) return arr[n - 1]

        // Doing binary search
        var i = 0
        var j = n
        var mid = 0
        while (i < j) {
            mid = (i + j) / 2
            if (arr[mid] == target) return arr[mid]

            /* If target is less than array element,
            then search in left */if (target < arr[mid]) {

                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > arr[mid - 1]) return getClosest(
                    arr[mid - 1],
                    arr[mid], target
                )

                /* Repeat for left half */j = mid
            } else {
                if (mid < n - 1 && target < arr[mid + 1]) return getClosest(
                    arr[mid],
                    arr[mid + 1], target
                )
                // update i
                i = mid + 1
            }
        }
        return arr[mid]
    }

    private fun getClosest(val1: Int, val2: Int, target: Int): Int {
        return if (target - val1 >= val2 - target) val2 else val1
    }

    companion object {
        var API_STATUS: Boolean = false
    }
}