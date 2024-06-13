package com.example.notessql

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notessql.data.Tag
import com.example.notessql.databinding.ActivityAddTagBinding

class AddTagActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTagBinding
    private lateinit var db: TasksDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        binding.saveButton.setOnClickListener {
            var name = binding.titleEditText.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val tags = db.getAllTags()
            for(tag in tags){
                if(tag.name == name){
                    Toast.makeText(this, "The same tag is already exists", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val tag = Tag(0, name)
            db.insertTag(tag)
            finish()
            Toast.makeText(this, "Tag Saved", Toast.LENGTH_SHORT).show()
        }
    }

}