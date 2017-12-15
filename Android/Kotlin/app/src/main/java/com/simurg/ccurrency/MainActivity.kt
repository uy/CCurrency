package com.simurg.ccurrency

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.ColorSpace
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*

import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.popup.*
import kotlinx.android.synthetic.main.popup.view.*

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.net.ConnectivityManager
import android.widget.*


class MainActivity : AppCompatActivity() {

    private var list: Array<ModelItem?> = arrayOfNulls<ModelItem>(0)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show();
            finish()
        } else
            getAndFillData(View(this@MainActivity))

        btnRefresh.setOnClickListener {
            if (!isNetworkAvailable(this)){
                snackBar("Internet connection has closed")
            } else
            getAndFillData(View(this@MainActivity)) }

        ccList.isClickable = true
        ccList.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            initiatePopupWindow(list[position])
        }
    }

    private fun getAndFillData(view: View) {
        changeUserInput(true)
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
                } else {
                }
            } catch (Ex: Exception) {
                Log.d("catch", "Error in doInBackground " + Ex.message)

                changeUserInput(false)
                snackBar("We have got with a problem.")
            }

            uiThread {
                //update UI thread after completing task
                changeUserInput(false)
                snackBar("We took list from outer space.")
                updateList(result, view)
            }
        }
    }

    private fun updateList(result: String, view: View) {
        val gson = Gson()
        list = gson.fromJson(result, Array<ModelItem?>::class.java)
        ccList.adapter = ListAdapter(view.context, list)
    }

    private fun initiatePopupWindow(data: ModelItem?) {
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.popup, findViewById<View>(R.id.popup_element) as? ViewGroup)

        layout.findViewById<TextView>(R.id.txt_24h_volume_usd).text = data?.volume_usd_24h
        layout.findViewById<TextView>(R.id.txt_market_cap_usd).text = data?.market_cap_usd
        layout.findViewById<TextView>(R.id.txt_available_supply).text = data?.available_supply
        layout.findViewById<TextView>(R.id.txt_total_supply).text = data?.total_supply
        layout.findViewById<TextView>(R.id.txt_max_supply).text = data?.max_supply
        layout.findViewById<TextView>(R.id.txt_percent_change_1h).text = data?.percent_change_1h
        layout.findViewById<TextView>(R.id.txt_percent_change_24h).text = data?.percent_change_24h
        layout.findViewById<TextView>(R.id.txt_percent_change_7d).text = data?.percent_change_7d
        layout.findViewById<TextView>(R.id.txt_last_updated).text = data?.last_updated

        val pw = PopupWindow(layout, 900, 700, true)
        pw.setOnDismissListener {
            // pop-up in disina basildiginda kapatilmasini sagliyor
            rltvProgressBar.visibility = ProgressBar.GONE
        }

        pw.showAtLocation(layout, Gravity.CENTER, 0, 0)
        rltvProgressBar.visibility = ProgressBar.VISIBLE
        layout.btnOk.setOnClickListener {

            pw.dismiss()
            rltvProgressBar.visibility = ProgressBar.GONE
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

    private fun changeUserInput(noTouchable: Boolean) {
        if (noTouchable) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar.visibility = ProgressBar.VISIBLE
            rltvProgressBar.visibility = ProgressBar.VISIBLE
            btnRefresh.hide()
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar.visibility = ProgressBar.GONE
            rltvProgressBar.visibility = ProgressBar.GONE
            btnRefresh.show()
        }
    }

    private fun snackBar(msg: String) {
        Snackbar.make(window.decorView.rootView, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        exit()
        return
    }

    private fun exit() {
        AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure want to close the application?")
                .setPositiveButton(android.R.string.yes) { dialog, _ -> finish() }
                .setNegativeButton(android.R.string.no) { dialog, _ -> dialog.cancel() }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val conMan = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return conMan.activeNetworkInfo != null && conMan.activeNetworkInfo.isConnected
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
