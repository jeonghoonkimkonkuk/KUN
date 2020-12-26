package com.kunotice.kunotice.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_VERSION = 1
        val DB_NAME = "mydb.db"
        val TABLE_NAME = arrayOf("major", "time", "languageCode")
        val MNAME = "mname"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table_1 = "create table if not exists " + TABLE_NAME[0] + "(" +
                MNAME + " text primary key)"
        val create_table_2 =
            "create table if not exists " + TABLE_NAME[1] + "(time text primary key)"
        val create_table_3 =
            "create table if not exists " + TABLE_NAME[2] + "(languageCode text primary key)"
        db?.execSQL(create_table_1)
        db?.execSQL(create_table_2)
        db?.execSQL(create_table_3)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table_1 = "drop table if exists " + TABLE_NAME[0]
        val drop_table_2 = "drop table if exists " + TABLE_NAME[1]
        val drop_table_3 = "drop table if exists " + TABLE_NAME[2]
        db?.execSQL(drop_table_1)
        db?.execSQL(drop_table_2)
        db?.execSQL(drop_table_3)
        onCreate(db)
    }

    fun insertLanguageCode(languageCode: String): Boolean {
        val values = ContentValues()
        values.put("languageCode", languageCode)
        val db = this.writableDatabase
        if (db.insert(TABLE_NAME[2], null, values) > 0) {
            db.close()
            return true
        } else {
            db.close()
            return false
        }
    }

    fun deleteLanguageCode(): Boolean {
        val db = this.writableDatabase
        db.execSQL("delete from " + TABLE_NAME[2])

        return true
    }

    fun getLanguageCode(): ArrayList<String> {
        val ret: ArrayList<String> = ArrayList<String>()
        val strsql = "select * from " + TABLE_NAME[2]
        val db = this.readableDatabase
        val cursor = db.rawQuery(strsql, null)
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                ret.add(cursor.getString(0))
            }
        }
        cursor.close()
        db.close()

        return ret
    }

    fun insertTime(time: String): Boolean {
        val values = ContentValues()
        values.put("time", time)
        val db = this.writableDatabase
        if (db.insert(TABLE_NAME[1], null, values) > 0) {
            db.close()
            return true
        } else {
            db.close()
            return false
        }
    }

    fun deleteTime(): Boolean {
        val db = this.writableDatabase
        db.execSQL("delete from " + TABLE_NAME[1])

        return true
    }

    fun getTime(): ArrayList<String> {
        val ret: ArrayList<String> = ArrayList<String>()
        val strsql = "select * from " + TABLE_NAME[1]
        val db = this.readableDatabase
        val cursor = db.rawQuery(strsql, null)
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                ret.add(cursor.getString(0))
            }
        }
        cursor.close()
        db.close()

        return ret
    }

    fun deleteProduct(mname: String): Boolean {
        val strsql = "select * from " + TABLE_NAME[0] + " where " +
                MNAME + " = \'" + mname + "\'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(strsql, null)
        if (cursor.moveToFirst()) {
            db.delete(TABLE_NAME[0], MNAME + "=?", arrayOf(mname))
            cursor.close()
            db.close()
            return true;
        }
        cursor.close()
        db.close()
        return false
    }


    fun insertProduct(mname: String): Boolean {
        val values = ContentValues()
        values.put(MNAME, mname)
        val db = this.writableDatabase
        if (db.insert(TABLE_NAME[0], null, values) > 0) {
            db.close()
            return true
        } else {
            db.close()
            return false
        }
    }

    fun getAllRecord(): ArrayList<String> {
        var mnames = ArrayList<String>()
        val strsql = "select * from " + TABLE_NAME[0]
        val db = this.readableDatabase
        val cursor = db.rawQuery(strsql, null)
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                mnames.add(cursor.getString(0))
            }
        }
        cursor.close()
        db.close()

        return mnames
    }
}