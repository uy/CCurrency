package com.simurg.ccurrency

import android.util.Log
//import com.simurg.ccurrency.R.id.ccPopupListview
//import com.simurg.ccurrency.adapters.PopupAdapter
//import kotlinx.android.synthetic.main.popup_custom.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
* Created by uyegen on 05.12.2017.
*/

class CoinMarketCapAPI {
    fun ticker(l: () -> Unit) {
        doAsync { // do background task here
            val apiUrl = "https://api.coinmarketcap.com/v1/ticker/"
            var result = ""
            try {
                val url = URL(apiUrl)
                val connect = url.openConnection() as HttpURLConnection

                connect.readTimeout = 8000
                connect.connectTimeout = 8000
                connect.requestMethod = "GET"
                connect.doOutput = false
                connect.connect()

                val responseCode: Int = connect.responseCode
                Log.d("Call", "ResponseCode" + responseCode)

                if (responseCode == 200) {
                    val tempStream: InputStream = connect.inputStream
                    result = convertToString(tempStream)
                } else {
                }
            } catch (Ex: Exception) {
                Log.d("catch", "Error in doInBackground " + Ex.message)
//                changeUserInput(false)
//
//                snackBar("We have got with a problem.")
            }

            uiThread { //update UI thread after completing task
                Log.d("uiThread", result)
//                changeUserInput(false)
//                snackBar("We took list from outer space.")
//
//                val gson = Gson()
//                val list = gson.fromJson(result, Array<ModelItem>::class.java)
//                ccList.adapter = ListAdapter(view.context, list)
            }
        }
    }

    private fun convertToString(inStream: InputStream): String {
        var result = ""
        val isReader = InputStreamReader(inStream)
        val bReader = BufferedReader(isReader)
        var tempStr: String?

        try {

            while (true) {
                tempStr = bReader.readLine()
                if (tempStr == null) {
                    break
                }
                result += tempStr
            }
        } catch (Ex: Exception) {
            Log.e("3", "Error in ConvertToString " + Ex.printStackTrace())
        }
        return result
    }
}