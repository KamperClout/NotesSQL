package com.example.notessql

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessql.Adapters.CustomAdapter
import com.example.notessql.data.Note
import com.example.notessql.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Date

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: TasksDatabaseHelper
    private lateinit var adapter: CustomAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        val tags = db.getAllTags()
        var checkedItems = ArrayList<String>()

        adapter = CustomAdapter(this, tags)
        binding.saveButton.setOnClickListener {

            var title = binding.titleEditText.text.toString()
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a Title!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var description = binding.contentEditText.text.toString()

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

            val task = Note(0, title, description, currentDate)
            db.insertTask(task)



            var i = 0
            while(i < adapter.checkedTags.size){
                val tag = adapter.checkedTags[i]
                checkedItems.add(tag.name)
                i++
            }

            if(adapter.checkedTags.size > 0){
                for(name in checkedItems){
                    db.addTaskTagRelation(db.getTagByName(name), db.getIdOfLastAddedTask())
                }
            } else {
                Toast.makeText(this, "No tags ok", Toast.LENGTH_SHORT).show()
            }


            finish()
            Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
        }

        val rv = findViewById<RecyclerView>(R.id.tagsList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()

        rv.adapter = adapter
    }
}