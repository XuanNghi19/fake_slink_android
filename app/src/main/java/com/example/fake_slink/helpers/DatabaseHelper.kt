package com.example.fake_slink.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper
    (context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "fake_slink.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createNotificationTableQuery = """
        CREATE TABLE notification (
            notification_id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            create_at TEXT NOT NULL,
            status INTEGER NOT NULL,
            content TEXT NOT NULL,
            last_read_date TEXT
        )
    """.trimIndent()

        db?.execSQL(createNotificationTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS notification")
        onCreate(db)
    }
}