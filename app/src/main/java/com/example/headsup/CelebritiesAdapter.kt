package com.example.headsup

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celebrity_item.view.*

class CelebritiesAdapter(private val celebritiesList: ArrayList<Celebrities.Celebrity>, private val context: Context) :
    RecyclerView.Adapter<CelebritiesAdapter.CelebrityViewHolder>() {
    class CelebrityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val celebrityNameTextView: TextView = itemView.tv_celebrityName
        val taboo1TextView: TextView = itemView.tv_taboo1
        val taboo2TextView: TextView = itemView.tv_taboo2
        val taboo3TextView: TextView = itemView.tv_taboo3
        val pkTextView: TextView = itemView.tv_pk
        val celebrityItemLinearLayout : LinearLayout = itemView.ll_celebrityItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CelebrityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.celebrity_item,
            parent,
            false
        )
        return CelebrityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CelebrityViewHolder, position: Int) {
        val celebrityName = celebritiesList[position].name
        val taboo1 = celebritiesList[position].taboo1
        val taboo2 = celebritiesList[position].taboo1
        val taboo3 = celebritiesList[position].taboo1
        val pk = celebritiesList[position].pk
        holder.celebrityNameTextView.text = celebrityName
        holder.taboo1TextView.text = taboo1
        holder.taboo2TextView.text = taboo2
        holder.taboo3TextView.text = taboo3
        holder.pkTextView.text = pk.toString()
        holder.celebrityItemLinearLayout.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("celebrityName", celebrityName)
            intent.putExtra("taboo1", taboo1)
            intent.putExtra("taboo2", taboo2)
            intent.putExtra("taboo3", taboo3)
            intent.putExtra("pk", pk)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = celebritiesList.size
}