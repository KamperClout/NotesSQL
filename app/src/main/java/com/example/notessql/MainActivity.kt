package com.example.notessql

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notessql.Adapters.NotesAdapter
import com.example.notessql.Adapters.TagsAdapter
import com.example.notessql.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: TasksDatabaseHelper
    private lateinit var tasksAdapter: NotesAdapter
    private lateinit var tagsAdapter: TagsAdapter
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    db = TasksDatabaseHelper(this)
    tasksAdapter = NotesAdapter(db.getAllTasks(), this)
    tagsAdapter = TagsAdapter(db.getAllTags(), this)

    binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
    binding.tasksRecyclerView.adapter = tasksAdapter
    binding.addButton.setOnClickListener {
        val intent = Intent(this, AddNoteActivity::class.java)

        startActivity(intent)
    }

    binding.tasksHeading.setOnClickListener {

        tasksAdapter = NotesAdapter(db.getAllTasks(), this)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = tasksAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)

            startActivity(intent)
        }

    }

    binding.tagsHeading.setOnClickListener {

        tagsAdapter = TagsAdapter(db.getAllTags(), this)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = tagsAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddTagActivity::class.java)

            startActivity(intent)
        }
    }

}

override fun onResume(){
    super.onResume()
    tasksAdapter.refreshData(db.getAllTasks())
    tagsAdapter.refreshData(db.getAllTags())
}
}