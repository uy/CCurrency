package com.simurg.ccurrency

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

/**
* Created by uyegen on 30.11.2017.
*/

class ListAdapter(context: Context, list: Array<ModelItem?>): BaseAdapter() {
    private var list = list
    private var inflator = LayoutInflater.from(context)
    private var mContext = context

    @SuppressLint("ViewHolder")
    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup?): View {
        //var vi = convertView
        val vi = inflator.inflate(R.layout.list_item, null)

        val rank = vi.findViewById<TextView>(R.id.txt_rank)
        val symbol = vi.findViewById<TextView>(R.id.txt_symbol)
        val name = vi.findViewById<TextView>(R.id.txt_name)
        val btc = vi.findViewById<TextView>(R.id.txt_btc)
        val usd = vi.findViewById<TextView>(R.id.txt_usd)
        val icon = vi.findViewById<ImageView>(R.id.iv_icon)

        rank.text = list[i]?.rank
        symbol.text = list[i]?.symbol
        name.text = list[i]?.name
        btc.text = list[i]?.price_btc
        usd.text = list[i]?.price_usd
        icon.setImageDrawable(imageResource(symbol.text.toString()))

        return  vi
    }

    override fun getItem(p0: Int): ModelItem? {
        return this.list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
            return this.list.size
    }

    private fun imageResource(symbol: String): Drawable {
        val sym = symbol.toLowerCase()
        val uri = "@drawable/icon_$sym"
        val imgResource = mContext.resources.getIdentifier(uri, null, mContext.packageName)

        try {
            val res = mContext.resources.getDrawable(imgResource, null)
            return res
        } catch (e: Exception) {
            val uri2 = "@drawable/icon_"
            val imgResource2 = mContext.resources.getIdentifier(uri2, null, mContext.packageName)
            return mContext.resources.getDrawable(imgResource2, null)
        }
    }
}