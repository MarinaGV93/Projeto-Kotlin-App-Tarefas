package br.com.dio.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.dio.todolist.databinding.ActivityMainBinding
import br.com.dio.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //Lazy = vai esperar ser chamado o atributo (adapter) para que ele faça a instanciação do adapter (TaskListAdapter())
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configuração do RecyclerView
        binding.rvTasks.adapter = adapter //Vai receber o adapter que foi criado
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            //Mandar essa Activity chamar a próxima Activity (AddTaskActivity) e retornar um resultado
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            /*Log.e("TAG", "ListenerEdit:  + $it" )*/
            //Atualizar o item da lista
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)

        }

        adapter.listenerDelete = {
            /*Log.e("TAG", "ListenerDelete:  + $it" )*/
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Ver se é o mesmo request code que mandou
        if (requestCode == CREATE_NEW_TASK && requestCode == Activity.RESULT_OK){

        }
    }

    private fun updateList(){
        adapter.submitList(TaskDataSource.getList())
    }

    //Criar Request Code
    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}