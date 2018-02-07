package com.simurg.ccurrency.ccapi

import android.util.Log
import com.google.gson.Gson
import com.simurg.ccurrency.ModelItem
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
* Created by uyegen on 28.12.2017.
*/

class CoinMarketCapAPI {
    companion object {

        fun ticker(completionHandler: (resultBool: Boolean, resultText: String, resultList: Array<ModelItem?>) -> Unit) {
            var list: Array<ModelItem?> = arrayOfNulls(0)

            doAsync {
                // do background task here
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
                    }
                } catch (Ex: Exception) {
                    Log.d("catch", "Error in doInBackground " + Ex.message)
                    completionHandler(false, "We have got with a problem!", list)
                }

                uiThread {
                    val gson = Gson()
                    list = gson.fromJson(result, Array<ModelItem?>::class.java)
                    completionHandler(false, "We took list from outer space!", list)
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
}