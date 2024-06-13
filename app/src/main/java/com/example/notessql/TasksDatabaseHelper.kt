package com.example.notessql

import com.example.notessql.data.Note
import com.example.notessql.data.Tag

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TasksDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "taskslist4.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alltasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE_OF_CREATE = "date_of_create"

        private const val TABLE_SECOND_NAME = "alltags"
        private const val TABLE_SECOND_COLUMN_ID = "id"
        private const val TABLE_SECOND_COLUMN_NAME = "name"

        private const val TABLE_THIRD_NAME = "tagsxtasks"
        private const val TABLE_THIRD_COLUMN_ID = "id"
        private const val TABLE_THIRD_COLUMN_TAG_ID = "tag_id"
        private const val TABLE_THIRD_COLUMN_TASK_ID = "task_id"

        private const val CONSTRAINT_1 = "fk_tag_id"
        private const val CONSTRAINT_2 = "fk_task_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_DATE_OF_CREATE TEXT);"

        val createSecondTableQuery = "CREATE TABLE $TABLE_SECOND_NAME (" +
                "$TABLE_SECOND_COLUMN_ID INTEGER PRIMARY KEY," +
                "$TABLE_SECOND_COLUMN_NAME TEXT)"

        val createThirdTableQuery = "CREATE TABLE $TABLE_THIRD_NAME (" +
                "$TABLE_THIRD_COLUMN_ID INTEGER PRIMARY KEY," +
                "$TABLE_THIRD_COLUMN_TAG_ID INTEGER NOT NULL," +
                "$TABLE_THIRD_COLUMN_TASK_ID INTEGER NOT NULL," +
                "CONSTRAINT $CONSTRAINT_1 FOREIGN KEY ($TABLE_THIRD_COLUMN_TAG_ID) " +
                "REFERENCES $TABLE_SECOND_NAME ($TABLE_SECOND_COLUMN_ID)," +
                "CONSTRAINT $CONSTRAINT_2 FOREIGN KEY ($TABLE_THIRD_COLUMN_TASK_ID) " +
                "REFERENCES $TABLE_NAME ($COLUMN_ID));"

        db?.execSQL(createTableQuery)

        db?.execSQL(createSecondTableQuery)
        db?.execSQL(createThirdTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"

        val dropSecondTableQuery = "DROP TABLE IF EXISTS $TABLE_SECOND_NAME"
        val dropThirdTableQuery = "DROP TABLE IF EXISTS $TABLE_THIRD_NAME"

        db?.execSQL(dropTableQuery)

        db?.execSQL(dropSecondTableQuery)
        db?.execSQL(dropThirdTableQuery)

        onCreate(db)
    }

    fun insertTask(task : Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DATE_OF_CREATE, task.date_of_create)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getIdOfLastAddedTask(): Int {
        val db = writableDatabase
        val query = "SELECT MAX($COLUMN_ID) FROM $TABLE_NAME"
        var cursor = db.rawQuery(query, null)
        cursor.moveToNext()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow("MAX($COLUMN_ID)"))
        cursor.close()
        db.close()

        return id
    }

    fun getAllTasks(): List<Note> {
        val tasksList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        var cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_CREATE))

            val task = Note(id, title, description, time)
            tasksList.add(task)
        }
        cursor.close()
        db.close()
        return tasksList
    }

    fun updateTask(task: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DATE_OF_CREATE, task.date_of_create)
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(task.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getTaskByID(taskId: Int): Note {
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $taskId"
        var cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_CREATE))

        cursor.close()
        db.close()
        return Note(id, title, description, time)
    }

    fun deleteTask(taskId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(taskId.toString())

        db.delete(TABLE_NAME, whereClause, whereArgs)

        val whereClause2 = "$TABLE_THIRD_COLUMN_TASK_ID = ?"
        db.delete(TABLE_THIRD_NAME, whereClause2, whereArgs)

        db.close()
    }



    fun insertTag(tag : Tag){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TABLE_SECOND_COLUMN_NAME, tag.name)
        }
        db.insert(TABLE_SECOND_NAME, null, values)
        db.close()
    }

    fun getAllTags(): List<Tag> {
        val tagsList = mutableListOf<Tag>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_SECOND_NAME"
        var cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_SECOND_COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_SECOND_COLUMN_NAME))

            val tag = Tag(id, name)
            tagsList.add(tag)

        }
        cursor.close()
        db.close()

        return tagsList
    }


    fun updateTag(tag: Tag){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TABLE_SECOND_COLUMN_NAME, tag.name)
        }

        val whereClause = "$TABLE_SECOND_COLUMN_ID = ?"
        val whereArgs = arrayOf(tag.id.toString())
        db.update(TABLE_SECOND_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getTagByID(tagId: Int): Tag {
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_SECOND_NAME WHERE $TABLE_SECOND_COLUMN_ID = $tagId"
        var cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_SECOND_COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_SECOND_COLUMN_NAME))

        cursor.close()
        db.close()
        return Tag(id, name)
    }

    fun deleteTag(tagId: Int){
        val db = writableDatabase
        val whereClause = "$TABLE_SECOND_COLUMN_ID = ?"
        val whereArgs = arrayOf(tagId.toString())

        db.delete(TABLE_SECOND_NAME, whereClause, whereArgs)

        val whereClause2 = "$TABLE_THIRD_COLUMN_TAG_ID = ?"
        db.delete(TABLE_THIRD_NAME, whereClause2, whereArgs)

        db.close()
    }


    fun getAllTagsByTaskId(taskId: Int): List<String>{
        val tagsList = mutableListOf<String>()
        val db = writableDatabase
        val query = "SELECT $TABLE_SECOND_COLUMN_NAME FROM $TABLE_THIRD_NAME " +
                "JOIN $TABLE_SECOND_NAME " +
                "ON $TABLE_THIRD_NAME.$TABLE_THIRD_COLUMN_TAG_ID = $TABLE_SECOND_NAME.$TABLE_SECOND_COLUMN_ID " +
                "WHERE $TABLE_THIRD_NAME.$TABLE_THIRD_COLUMN_TASK_ID = $taskId"

        var cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            val name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_SECOND_COLUMN_NAME))
            tagsList.add(name)
        }
        cursor.close()
        db.close()

        return tagsList
    }

    fun addTaskTagRelation(tagId: Int, taskId: Int){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TABLE_THIRD_COLUMN_TAG_ID, tagId)
            put(TABLE_THIRD_COLUMN_TASK_ID, taskId)
        }
        db.insert(TABLE_THIRD_NAME, null, values)
        db.close()
    }

    fun getTagByName(name: String?): Int{
        val db = writableDatabase
        val query = "SELECT $TABLE_SECOND_COLUMN_ID " +
                "FROM $TABLE_SECOND_NAME " +
                "WHERE $TABLE_SECOND_COLUMN_NAME = '$name'"

        var cursor = db.rawQuery(query, null)
        cursor.moveToNext()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_SECOND_COLUMN_ID))
        cursor.close()
        db.close()

        return id
    }

    fun getAllTasksWithThisTag(tagId: Int): List<Note> {
        val tasksList = mutableListOf<Note>()
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                "JOIN $TABLE_THIRD_NAME " +
                "ON $TABLE_THIRD_NAME.$TABLE_THIRD_COLUMN_TASK_ID = $TABLE_NAME.$COLUMN_ID " +
                "WHERE $TABLE_THIRD_NAME.$TABLE_THIRD_COLUMN_TAG_ID = $tagId"

        var cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_CREATE))

            val task = Note(id, title, description, time)
            tasksList.add(task)
        }
        cursor.close()
        db.close()
        return tasksList
    }

    fun getAllTagsForThisTask(taskId: Int): List<Tag> {
        val tagsList = mutableListOf<Tag>()
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_SECOND_NAME " +
                "JOIN $TABLE_THIRD_NAME " +
                "ON $TABLE_THIRD_NAME.$TABLE_THIRD_COLUMN_TAG_ID = $TABLE_SECOND_NAME.$COLUMN_ID " +
                "WHERE $TABLE_THIRD_NAME.$TABLE_THIRD_COLUMN_TASK_ID = $taskId"

        var cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_SECOND_COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_SECOND_COLUMN_NAME))

            val tag = Tag(id, name)
            tagsList.add(tag)

        }
        cursor.close()
        db.close()

        return tagsList
    }

    fun deleteTagsRelationsByTaskId(taskId: Int){
        val db = writableDatabase
        val whereClause = "$TABLE_THIRD_COLUMN_TASK_ID = ?"
        val whereArgs = arrayOf(taskId.toString())

        db.delete(TABLE_THIRD_NAME, whereClause, whereArgs)

        db.close()
    }
}


//    private const val TABLE_SECOND_NAME = "alltags"
//    private const val TABLE_SECOND_COLUMN_ID = "id"
//    private const val TABLE_SECOND_COLUMN_NAME = "name"
//
//    private const val TABLE_THIRD_NAME = "tagsxtasks"
//    private const val TABLE_THIRD_COLUMN_ID = "id"
//    private const val TABLE_THIRD_COLUMN_TAG_ID = "tag_id"
//    private const val TABLE_THIRD_COLUMN_TASK_ID = "task_id"