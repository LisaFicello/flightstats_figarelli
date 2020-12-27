package com.example.flightstatsm2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_flight_list.*


class FlightTrackActivity : AppCompatActivity() {

    private lateinit var viewModel: FlightTrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_track)

        viewModel = ViewModelProvider(this).get(FlightTrackViewModel::class.java)
        viewModel.search(
            intent.getStringExtra("icao")!!,
            intent.getLongExtra("time", 0)
        )
        val icao = intent.getStringExtra("icao")!!
        //val time = intent.getLongExtra("time")

        Log.i("FLIGHT", "icao: "+icao)

        val text = "Hello toast!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()


        /*val isMobile = detail_container == null

        viewModel = ViewModelProvider(this).get(FlightTrackViewModel::class.java)
        viewModel.search(
            intent.getStringExtra("icao")!!,
            intent.getBooleanExtra("isArrival", false),
            intent.getLongExtra("begin", 0),
            intent.getLongExtra("end", 0)
        )

        viewModel.getSelectedFlightTrackLiveData().observe(this, androidx.lifecycle.Observer{
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
        })*/

    }
}

