package br.com.natalia.apptasks

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import br.com.natalia.apptasks.adapter.TarefaRecyclerAdapter
import br.com.natalia.apptasks.db.DatabaseHandler
import br.com.natalia.apptasks.models.Tarefa

class MainActivity : AppCompatActivity() {

    var tarefaRecyclerAdapter: TarefaRecyclerAdapter? = null;
    var fab: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var dbHandler: DatabaseHandler? = null
    var listaTarefas: List<Tarefa> = ArrayList()
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initOperations()
    }

    fun initDB() {
        dbHandler = DatabaseHandler(this)
        listaTarefas = (dbHandler as DatabaseHandler).getTarefas()
        tarefaRecyclerAdapter = TarefaRecyclerAdapter(tarefaList = listaTarefas, context = applicationContext)
        (recyclerView as RecyclerView).adapter = tarefaRecyclerAdapter
    }

    fun initViews() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.fab)
        recyclerView = findViewById(R.id.recycler_view)
        tarefaRecyclerAdapter = TarefaRecyclerAdapter(tarefaList = listaTarefas, context = applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    fun initOperations() {
        fab?.setOnClickListener {
            val i = Intent(applicationContext, AddOrEditActivity::class.java)
            i.putExtra("Mode", "A")
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.deletar_todas) {
            val dialog = AlertDialog.Builder(this).setTitle("Deletar").setMessage("Deseja deletar todas as tarefas?")
                    .setPositiveButton("SIM") { dialog, _ ->
                        dbHandler!!.deleteTarefas()
                        initDB()
                        dialog.dismiss()
                    }
                    .setNegativeButton("NAO") { dialog, _ ->
                        dialog.dismiss()
                    }
            dialog.show()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        initDB()
    }
}
