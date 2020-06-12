package com.android.matrimonyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_member.view.*

class MemberRecyclerAdapter(
    var list: ArrayList<MemberData>,
    var context: Context,
    var db: AppDatabase
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as MemberViewHolder
        Picasso.with(context).load(list[position].picture).into(viewHolder.itemView.imageView)
        viewHolder.itemView.tvName.text = list[position].name
        viewHolder.itemView.tvLocation.text = list[position].location
        viewHolder.itemView.tvAge.text = list[position].age + " years"

        if (list[position].accepted == "none") {
            viewHolder.itemView.llBtns.visibility = View.VISIBLE
            viewHolder.itemView.tvAccepted.visibility = View.GONE
        } else {
            viewHolder.itemView.llBtns.visibility = View.INVISIBLE
            viewHolder.itemView.tvAccepted.visibility = View.VISIBLE
            if (list[position].accepted == "accepted")
                viewHolder.itemView.tvAccepted.text = "Member accepted"
            else
                viewHolder.itemView.tvAccepted.text = "Member declined"
        }

    }

    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.btnDecline.setOnClickListener {
                itemView.tvAccepted.visibility = View.VISIBLE
                itemView.llBtns.visibility = View.INVISIBLE
                itemView.tvAccepted.text = "Member declined"
                list[adapterPosition].accepted = "declined"
                object : Thread() {
                    override fun run() {
                        db.memberDao().update("declined", list[adapterPosition].uid)
                    }
                }.start()

            }

            itemView.btnAccept.setOnClickListener {
                itemView.tvAccepted.visibility = View.VISIBLE
                itemView.llBtns.visibility = View.INVISIBLE
                itemView.tvAccepted.text = "Member accepted"
                list[adapterPosition].accepted = "accepted"
                object : Thread() {
                    override fun run() {
                        db.memberDao().update("accepted", list[adapterPosition].uid)
                    }
                }.start()

            }

        }

    }

    fun updateList(arrayList: ArrayList<MemberData>) {
        clearList()
        list.addAll(arrayList)
        notifyDataSetChanged()
    }

    fun clearList() {
        list.clear()
    }
}
