package com.example.notessql.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notessql.EditNoteActivity
import com.example.notessql.EditTagActivity
import com.example.notessql.R
import com.example.notessql.TasksDatabaseHelper
import com.example.notessql.data.Tag


class TagsAdapter(private var tags: List<Tag>, context: Context) : RecyclerView.Adapter<TagsAdapter.TagViewHolder>() {

    private val db: TasksDatabaseHelper = TasksDatabaseHelper(context)

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagNameTextView: TextView = itemView.findViewById(R.id.tagNameTextView)

        val updateTagButton: ImageView = itemView.findViewById(R.id.updateTagButton)
        val deleteTagButton: ImageView = itemView.findViewById(R.id.deleteTagButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tag_item, parent, false)
        return TagViewHolder(view)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]
        holder.tagNameTextView.text = tag.name

        holder.updateTagButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditTagActivity::class.java).apply {
                putExtra("tag_id", tag.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteTagButton.setOnClickListener {
            db.deleteTag(tag.id)
            refreshData(db.getAllTags())
            Toast.makeText(holder.itemView.context, "Tag deleted", Toast.LENGTH_SHORT).show()
        }

    }

    fun refreshData(newTags: List<Tag>){
        tags = newTags
        notifyDataSetChanged()
    }

}