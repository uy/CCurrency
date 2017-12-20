package com.simurg.ccurrency

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
* Created by uyegen on 30.11.2017.
*/

class ListAdapter(context: Context, list: Array<ModelItem?>): BaseAdapter() {
    private var list = list
    private var inflator = LayoutInflater.from(context)

    @SuppressLint("ViewHolder")
    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup?): View {
        //var vi = convertView
        val vi = inflator.inflate(R.layout.list_item, null)

        val rank = vi.findViewById<TextView>(R.id.txt_rank)
        val symbol = vi.findViewById<TextView>(R.id.txt_symbol)
        val name = vi.findViewById<TextView>(R.id.txt_name)
        val btc = vi.findViewById<TextView>(R.id.txt_btc)
        val usd = vi.findViewById<TextView>(R.id.txt_usd)

        rank.text = list[i]?.rank
        symbol.text = list[i]?.symbol
        name.text = list[i]?.name
        btc.text = list[i]?.price_btc
        usd.text = list[i]?.price_usd

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
}