package com.example.notessql.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notessql.R
import com.example.notessql.TasksDatabaseHelper
import com.example.notessql.data.Note

class TasksAdapterForTagUpdate(private var tasks: List<Note>, context: Context) : RecyclerView.Adapter<TasksAdapterForTagUpdate.TaskViewHolder>() {

    private val db: TasksDatabaseHelper = TasksDatabaseHelper(context)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextViewForTagUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item_for_update_tag, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title

    }

    fun refreshData(newTasks: List<Note>){
        tasks = newTasks
        notifyDataSetChanged()
    }

}