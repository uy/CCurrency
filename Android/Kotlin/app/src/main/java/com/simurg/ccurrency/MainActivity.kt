package com.simurg.ccurrency

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            changeUserInput(true)
            doAsync{
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
                    } else {
                    }
                } catch(Ex: Exception) {
                    Log.d("catch", "Error in doInBackground " + Ex.message)
                    changeUserInput(false)
                    snackBar(view, "We have got with a problem.")
                }

                uiThread{
                    //update UI thread after completing task
                    Log.d("uiThread", result)
                    changeUserInput(false)
                    snackBar(view, "We took list from outer space.")

                    val gson = Gson()
                    val list = gson.fromJson(result, Array<ModelItem>::class.java)
                    ccList.adapter = ListAdapter(view.context, list)
                }
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
                if (tempStr == null) { break }
                result += tempStr
            }
        } catch(Ex: Exception) {
            Log.e("3", "Error in ConvertToString " + Ex.printStackTrace())
        }
        return result
    }

    private fun changeUserInput(noTouchable: Boolean) {
        if (noTouchable) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar.visibility = ProgressBar.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar.visibility = ProgressBar.GONE
        }
    }

    private fun snackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }
}
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
