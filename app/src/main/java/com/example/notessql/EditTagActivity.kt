package com.example.notessql

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessql.Adapters.TasksAdapterForTagUpdate
import com.example.notessql.data.Tag
import com.example.notessql.databinding.ActivityEditTagBinding

class EditTagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTagBinding
    private lateinit var db: TasksDatabaseHelper
    private lateinit var tasksAdapter: TasksAdapterForTagUpdate
    private var tagId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        tagId = intent.getIntExtra("tag_id", -1)
        if(tagId == -1){
            finish()
            return
        }

        val tag = db.getTagByID(tagId)
        tasksAdapter = TasksAdapterForTagUpdate(db.getAllTasksWithThisTag(tagId), this)

        binding.updateTagNameEditText.setText(tag.name)
        binding.tasksRecyclerViewForTagUpdate.adapter = tasksAdapter

        binding.updateTagButton.setOnClickListener{
            val newName = binding.updateTagNameEditText.text.toString()


            val updatedTag = Tag(tagId, newName)
            db.updateTag(updatedTag)
            finish()
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
        }

        val rv = findViewById(R.id.tasksRecyclerViewForTagUpdate) as RecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()

        rv.adapter = tasksAdapter
    }
}