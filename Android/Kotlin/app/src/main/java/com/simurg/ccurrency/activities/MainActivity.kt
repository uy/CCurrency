package com.simurg.ccurrency.activities

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import com.simurg.ccurrency.ListAdapter
import com.simurg.ccurrency.ModelItem
import com.simurg.ccurrency.R
import com.simurg.ccurrency.ccapi.CoinMarketCapAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.popup.view.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    private var list: Array<ModelItem?> = arrayOfNulls(0) // <ModelItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show()
            finish()
        } else {
            getAndFillData(View(this@MainActivity))
        }

        btnRefresh.setOnClickListener {
            if (!isNetworkAvailable(this)) {
                snackBar("Internet connection has closed")
            } else {
                getAndFillData(View(this@MainActivity))
            }
        }

        ccList.isClickable = true
        ccList.onItemClickListener = AdapterView.OnItemClickListener{ _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            initiatePopupWindow(list[position])
        }

        srl.setOnRefreshListener {
            srl.isEnabled = false
            srl.isRefreshing = false
            getAndFillData(View(this@MainActivity))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings ->
                // User chose the "Settings" item, show the app settings UI...
                return true

            R.id.action_favorite ->
                startActivity<FavoritesActivity>()

//            else ->
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getAndFillData(view: View) {
        changeUserInput(true)
        CoinMarketCapAPI.ticker { resultBool, resultText, resultList ->
            list = resultList
            changeUserInput(resultBool)
            snackBar(resultText)
            if (resultList.isNotEmpty()) {
                updateList(resultList, view)
            }
        }
    }

    private fun updateList(list: Array<ModelItem?>, view: View) {
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
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    finish()
                }
                .setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.cancel()
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val conMan = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return conMan.activeNetworkInfo != null && conMan.activeNetworkInfo.isConnected
    }
}

