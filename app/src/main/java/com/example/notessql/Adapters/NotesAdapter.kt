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
import com.example.notessql.R
import com.example.notessql.TasksDatabaseHelper
import com.example.notessql.data.Note

class NotesAdapter (private var tasks: List<Note>, context: Context) : RecyclerView.Adapter<NotesAdapter.TaskViewHolder>() {

    private val db: TasksDatabaseHelper = TasksDatabaseHelper(context)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val taskTagsList: TextView = itemView.findViewById(R.id.taskTagsList)

        val updateButton: ImageView = itemView.findViewById(R.id.updateTaskButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteTaskButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.descriptionTextView.text = if (task.description.isEmpty()) "No description" else task.description
        holder.timeTextView.text = task.date_of_create

        val tags = db.getAllTagsByTaskId(task.id)
        var str: String = "Tags: "
        for(tagName in tags){
            str += " $tagName;"
        }

        holder.taskTagsList.text = if (tags.isEmpty()) "No tags" else str


        holder.updateButton.setOnClickListener {
            var intent = Intent(holder.itemView.context, EditNoteActivity::class.java).apply {
                putExtra("task_id", task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteTask(task.id)
            refreshData(db.getAllTasks())
            Toast.makeText(holder.itemView.context, "Task deleted", Toast.LENGTH_SHORT).show()
        }

    }

    fun refreshData(newTasks: List<Note>){
        tasks = newTasks
        notifyDataSetChanged()
    }

}
