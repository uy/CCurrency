package com.simurg.ccurrency

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.widget.*
import org.jetbrains.anko.*
import android.R.string.cancel
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.view.ViewGroup
import kotlinx.android.synthetic.main.popup.view.*
import java.util.ArrayList


abstract class MainActivity : AppCompatActivity() {

    lateinit var modelItems: ArrayList<ModelItem>


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        getAndFillData(View(this@MainActivity))

        btnRefresh.setOnClickListener { getAndFillData(View(this@MainActivity)) }

        ccList.isClickable = true
        ccList.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            //            val o = ccList.getItemAtPosition(position)
            initiatePopupWindow()
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
                Log.d("uiThread", result)
                changeUserInput(false)
                snackBar("We took list from outer space.")

                val gson = Gson()
                val list = gson.fromJson(result, Array<ModelItem>::class.java)
                ccList.adapter = ListAdapter(view.context, list)
            }
        }
    }

    private fun initiatePopupWindow() {
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.popup, findViewById<View>(R.id.popup_element) as? ViewGroup)
        val pw = PopupWindow(layout, 700, 700, true)
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0)
        layout.btnOk.setOnClickListener { pw.dismiss() }
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
        Exit()
        return
    }

    private fun Exit() {
        AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure want to close the application?")
                .setPositiveButton(android.R.string.yes) { dialog, _ -> finish() }
                .setNegativeButton(android.R.string.no) { dialog, _ -> dialog.cancel() }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
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
