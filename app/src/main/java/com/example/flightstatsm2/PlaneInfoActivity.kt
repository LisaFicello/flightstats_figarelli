package com.example.flightstatsm2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_flight_list.*

class PlaneInfoActivity  : AppCompatActivity() {
    private lateinit var viewModel: PlaneInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plane_info)
        Log.e("Ecran4", "Screen 4 is loaded")

        val isMobile = detail_container == null

        viewModel = ViewModelProvider(this).get(PlaneInfoViewModel::class.java)

        viewModel.getSelectedFlightNameLiveData().observe(this, {
            //switch fragment
            val newFragment: FlightDetailFragment = FlightDetailFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            if (isMobile) {
                transaction.add(R.id.activityContainer, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
            else{
                transaction.add(R.id.detail_container, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
        })
    }
}
