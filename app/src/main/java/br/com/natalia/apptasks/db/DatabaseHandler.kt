package br.com.natalia.apptasks.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import br.com.natalia.apptasks.models.Tarefa
import java.util.*

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DatabaseHandler.DB_NAME, null, DatabaseHandler.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NOME TEXT,$DESCRICAO TEXT,$REALIZADA TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addTarefa(tarefa: Tarefa): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(NOME, tarefa.nome)
        values.put(DESCRICAO, tarefa.descricao)
        values.put(REALIZADA, tarefa.realizada)
        val _successo = db.insert(TABLE_NAME, null, values)

        values.clear()
        db.close()
        Log.v("InsertedId", "$_successo")

        return (Integer.parseInt("$_successo") != -1)
    }

    fun getTarefa(_id: Int): Tarefa {
        val tarefa = Tarefa()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        tarefa.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        tarefa.nome = cursor.getString(cursor.getColumnIndex(NOME))
        tarefa.descricao = cursor.getString(cursor.getColumnIndex(DESCRICAO))
        tarefa.realizada = cursor.getString(cursor.getColumnIndex(REALIZADA))

        cursor.close()
        db.close()

        return tarefa
    }

    fun getTarefas(): List<Tarefa> {
        val listaTarefas = ArrayList<Tarefa>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val tarefa = Tarefa()
                    tarefa.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tarefa.nome = cursor.getString(cursor.getColumnIndex(NOME))
                    tarefa.descricao = cursor.getString(cursor.getColumnIndex(DESCRICAO))
                    tarefa.realizada = cursor.getString(cursor.getColumnIndex(REALIZADA))
                    listaTarefas.add(tarefa)
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()

        return listaTarefas
    }

    fun updateTarefa(tarefa: Tarefa): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(NOME, tarefa.nome)
        values.put(DESCRICAO, tarefa.descricao)
        values.put(REALIZADA, tarefa.realizada)
        val _successo = db.update(TABLE_NAME, values, "$ID=?", arrayOf(tarefa.id.toString())).toLong()

        values.clear()
        db.close()

        return Integer.parseInt("$_successo") != -1
    }

    fun deleteTarefa(_id: Int): Boolean {
        val db = this.writableDatabase
        val _successo = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()

        db.close()

        return Integer.parseInt("$_successo") != -1
    }

    fun deleteTarefas(): Boolean {
        val db = this.writableDatabase
        val _successo = db.delete(TABLE_NAME, null, null).toLong()

        db.close()

        return Integer.parseInt("$_successo") != -1
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "DBTarefas"
        private const val TABLE_NAME = "tarefa"
        private const val ID = "id"
        private const val NOME = "nome"
        private const val DESCRICAO = "descricao"
        private const val REALIZADA = "realizada"
    }
}