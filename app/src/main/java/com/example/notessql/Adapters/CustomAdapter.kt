package com.example.notessql.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notessql.R
import com.example.notessql.data.Tag

class CustomAdapter(
    mContext: Context, private val dataSet: List<Tag>
) :
    RecyclerView.Adapter<CustomAdapter.MyHolder>() {

    var checkedTags = ArrayList<Tag>()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyHolder {

        var v = LayoutInflater.from(parent.context).inflate(R.layout.raw_tag, null)

        return MyHolder(v)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val tag = dataSet[position]
        holder.txtName.text = tag.name
        holder.checkBox.isChecked = tag.isSelected

        if(holder.checkBox.isChecked){

            checkedTags.add(tag)
        }

        holder.setItemClickListener(object : MyHolder.ItemClickListener {
            override fun onItemClick(v: View, position: Int) {

                val myCheckBox = v as CheckBox
                val currentTag = dataSet[position]

                if(myCheckBox.isChecked) {
                    currentTag.isSelected = true
                    checkedTags.add(currentTag)
                } else if (!myCheckBox.isChecked) {
                    currentTag.isSelected = false
                    checkedTags.remove(currentTag)
                }
            }
        })

    }

    override fun getItemCount(): Int {

        return dataSet.size
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var txtName: TextView
        var checkBox: CheckBox

        lateinit var myItemClickListener: ItemClickListener

        init {
            txtName = itemView.findViewById(R.id.txtName)
            checkBox = itemView.findViewById(R.id.checkBox)
            checkBox.setOnClickListener(this)
        }

        fun setItemClickListener(ic: ItemClickListener) {

            this.myItemClickListener = ic
        }

        override fun onClick(v: View) {

            this.myItemClickListener.onItemClick(v, layoutPosition)
        }

        interface ItemClickListener {

            fun onItemClick(v: View, pos: Int)
        }

    }
}
