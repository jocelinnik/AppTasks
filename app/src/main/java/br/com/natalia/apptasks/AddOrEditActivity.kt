package br.com.natalia.apptasks

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import br.com.natalia.apptasks.db.DatabaseHandler
import br.com.natalia.apptasks.models.Tarefa
import kotlinx.android.synthetic.main.activity_add_edit.*

class AddOrEditActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var isModoEdicao = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDB()
        initOperations()
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        btn_deletar.visibility = View.INVISIBLE

        if (intent != null && intent.getStringExtra("Modo") == "Edicao") {
            isModoEdicao = true
            val tarefa: Tarefa = dbHandler!!.getTarefa(intent.getIntExtra("Id",0))

            input_nome.setText(tarefa.nome)
            input_desc.setText(tarefa.descricao)
            swt_realizada.isChecked = tarefa.realizada == "Y"
            btn_deletar.visibility = View.VISIBLE
        }
    }

    private fun initOperations() {
        btn_salvar.setOnClickListener {
            var successo: Boolean = false

            if (!isModoEdicao) {
                val tarefa: Tarefa = Tarefa()

                tarefa.nome = input_nome.text.toString()
                tarefa.descricao = input_desc.text.toString()
                if (swt_realizada.isChecked) tarefa.realizada = "Y"
                else tarefa.realizada = "N"

                successo = dbHandler?.addTarefa(tarefa) as Boolean
            } else {
                val tarefa: Tarefa = Tarefa()

                tarefa.id = intent.getIntExtra("Id", 0)
                tarefa.nome = input_nome.text.toString()
                tarefa.descricao = input_desc.text.toString()
                if (swt_realizada.isChecked) tarefa.realizada = "Y"
                else tarefa.realizada = "N"

                successo = dbHandler?.updateTarefa(tarefa) as Boolean
            }

            if (successo) finish()
        }

        btn_deletar.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Deletar").setMessage("Deseja deletar esta tarefa?")
                    .setPositiveButton("SIM") { dialog, _ ->
                        val successo = dbHandler?.deleteTarefa(intent.getIntExtra("Id", 0)) as Boolean
                        if (successo) finish()
                        dialog.dismiss()
                    }
                    .setNegativeButton("NAO") { dialog, _ ->
                        dialog.dismiss()
                    }
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
