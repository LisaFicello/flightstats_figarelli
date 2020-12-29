package com.example.flightstatsm2

import android.os.Bundle
import androidx.lifecycle.ViewModel

class PlaneInfoViewModel : ViewModel(), RequestsManager.RequestListener {




        override fun onRequestSuccess(result: String?) {
        TODO("Not yet implemented")
    }

    override fun onRequestFailed() {
        TODO("Not yet implemented")
    }
}