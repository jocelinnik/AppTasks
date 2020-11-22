package br.com.natalia.apptasks.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import br.com.natalia.apptasks.AddOrEditActivity

import br.com.natalia.apptasks.R
import br.com.natalia.apptasks.models.Tarefa

import java.util.ArrayList

class TarefaRecyclerAdapter(tarefaList: List<Tarefa>, internal var context: Context) : RecyclerView.Adapter<TarefaRecyclerAdapter.TaskViewHolder>() {

    private var tarefaList: List<Tarefa> = ArrayList()
    init {
        this.tarefaList = tarefaList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_tarefas, parent, false)
        return TaskViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tarefa = tarefaList[position]

        holder.nome.text = tarefa.nome
        holder.descricao.text = tarefa.descricao

        if (tarefa.realizada == "Y")
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorSuccess)
        else
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorUnSuccess)

        holder.itemView.setOnClickListener {
            val i = Intent(context, AddOrEditActivity::class.java)
            i.putExtra("Modo", "Edicao")
            i.putExtra("Id", tarefa.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return tarefaList.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nome: TextView = view.findViewById(R.id.nome_tarefa) as TextView
        var descricao: TextView = view.findViewById(R.id.descricao_tarefa) as TextView
        var list_item: LinearLayout = view.findViewById(R.id.list_item) as LinearLayout
    }
}
