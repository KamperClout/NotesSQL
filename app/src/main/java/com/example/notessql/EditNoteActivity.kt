package com.example.notessql

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessql.Adapters.CustomAdapter
import com.example.notessql.databinding.ActivityEditNoteBinding
import com.example.notessql.data.Note

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var db: TasksDatabaseHelper
    private lateinit var adapter: CustomAdapter
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)


        var checkedItems = ArrayList<String>()




        taskId = intent.getIntExtra("task_id", -1)
        if(taskId == -1){
            finish()
            return
        }

        val allTags = db.getAllTags()
        val tags = db.getAllTagsForThisTask(taskId)
        for(i in allTags.indices){
            for(element in tags){
                if(allTags[i].name == element.name){
                    allTags[i].isSelected = true
                }
            }
        }
        adapter = CustomAdapter(this, allTags)

        val task = db.getTaskByID(taskId)
        binding.updateTitleEditText.setText(task.title)
        binding.updateDescriptionEditText.setText(task.description)



        binding.updateTaskButton.setOnClickListener{

            val newTitle = binding.updateTitleEditText.text.toString()
            if (newTitle.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newDescription = binding.updateDescriptionEditText.text.toString()


            var i = 0
            while(i < adapter.checkedTags.size){
                val tag = adapter.checkedTags[i]
                checkedItems.add(tag.name)
                i++
            }

            db.deleteTagsRelationsByTaskId(taskId)
            if(adapter.checkedTags.size > 0){
                for(name in checkedItems){
                    db.addTaskTagRelation(db.getTagByName(name), taskId)
                }
            } else {
                Toast.makeText(this, "No tags ok", Toast.LENGTH_SHORT).show()
            }


            val updatedTask = Note(taskId, newTitle, newDescription, task.date_of_create)
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
        }

        val rv = findViewById(R.id.tagsListInUpdatePage) as RecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()

        rv.adapter = adapter
    }
}