package com.example.flightstatsm2

import android.os.AsyncTask
import android.util.Log

/**
 * Created by lisa on 24/12/2020
 */
class SearchFlightTrackAsyncTask(requestListener: RequestsManager.RequestListener) :
    AsyncTask<SearchFlightTrackDataModel, Void, String>() {

    var mRequestListener: RequestsManager.RequestListener? = null
    init {
        mRequestListener = requestListener
    }

    override fun doInBackground(vararg searchModel: SearchFlightTrackDataModel?): String? {
        val data = searchModel[0]
        val baseUrl: String = "https://opensky-network.org/api/tracks/all"
        val result: String? =
            RequestsManager.get(baseUrl, getRequestParams(searchModel = searchModel[0]))

        return result
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        if (result == null) {
            mRequestListener?.onRequestFailed()
        } else {
            Log.v("RESULT", result)
            mRequestListener?.onRequestSuccess(result)
        }

    }

    private fun getRequestParams(searchModel: SearchFlightTrackDataModel?): Map<String, String>? {
        val params = HashMap<String, String>()
        if (searchModel != null) {
            params["icao24"] = searchModel.icao
            params["time"] = searchModel.time.toString()
        }
        return params
    }


}