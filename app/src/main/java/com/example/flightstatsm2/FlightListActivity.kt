package com.example.flightstatsm2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_flight_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.flight_cell.*


class FlightListActivity : AppCompatActivity() {

    private lateinit var viewModel: FlightListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        val isMobile = detail_container == null

        viewModel = ViewModelProvider(this).get(FlightListViewModel::class.java)
        viewModel.search(
            intent.getStringExtra("icao")!!,
            intent.getBooleanExtra("isArrival", false),
            intent.getLongExtra("begin", 0),
            intent.getLongExtra("end", 0)
        )

        val icao = intent.getStringExtra("icao")!!
        val begin = intent.getLongExtra("begin", 0)
        val end = intent.getLongExtra("end", 0)
        val time = end - begin

        val flight_cell = findViewById<View>(R.id.info_flight)

        viewModel.getSelectedFlightNameLiveData().observe(this, androidx.lifecycle.Observer {
            //switch fragment
            val newFragment: FlightDetailFragment = FlightDetailFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            if (isMobile) {
                transaction.add(R.id.activityContainer, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()

                flight_cell.setOnClickListener { viewDetailsFlight(icao, time) }

            } else {
                transaction.add(R.id.detail_container, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
        })
    }

   private fun viewDetailsFlight(icao : String, time : Long) {
        val i = Intent(this, FlightTrackActivity::class.java)
        i.putExtra("icao", icao)
        i.putExtra("time", time)
        startActivity(i)
    }
}