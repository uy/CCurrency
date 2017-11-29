package com.simurg.ccurrency

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
* Created by uyegen on 29.11.2017.
*/
class CallAPI: AsyncTask<Void, Void, String>() {
    private var result = ""
    override fun doInBackground(vararg params: Void?): String? {
        val apiUrl = "https://api.coinmarketcap.com/v1/ticker/"

        try {

            val url = URL(apiUrl)
            val connect = url.openConnection() as HttpURLConnection

            connect.readTimeout = 8000
            connect.connectTimeout = 8000
            connect.requestMethod = "GET"
            connect.doOutput = false
            connect.connect()

            val responseCode: Int = connect.responseCode
            Log.d("2", "ResponseCode" + responseCode)

            if (responseCode == 200) {
                val tempStream: InputStream = connect.inputStream
                result = this.convertToString(tempStream)
            }
        } catch(Ex: Exception) {
            Log.d("1", "Error in doInBackground " + Ex.message)
        }

        return result
    }

    private fun convertToString(inStream: InputStream): String {

        var result = ""
        val isReader = InputStreamReader(inStream)
        val bReader = BufferedReader(isReader)
        var tempStr: String?

        try {

            while (true) {
                tempStr = bReader.readLine()
                if (tempStr == null) { break }
                result += tempStr
            }
        } catch(Ex: Exception) {
            Log.e("3", "Error in ConvertToString " + Ex.printStackTrace())
        }
        return result
    }
}