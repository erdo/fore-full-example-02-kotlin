package foo.bar.example.fore.fullapp02.ui.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.early.fore.adapters.ChangeAwareAdapter
import co.early.fore.core.logging.Logger
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.todolist.TodoListModel
import kotlinx.android.synthetic.main.fragment_todolist_listitem.view.*


class TodoListAdapter(val todoListModel: TodoListModel, val logger: Logger) : ChangeAwareAdapter<TodoListAdapter.ViewHolder>(todoListModel) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_todolist_listitem, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.tag = holder
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = todoListModel.getItem(position)

        holder.itemView.todolist_done_checkbox.isChecked = item.isDone
        holder.itemView.todolist_done_checkbox.setOnCheckedChangeListener { _, isChecked ->
            //yuk, can't find a way around this, without checking
            //here you will occasionally get outofindex errors
            //if you tap very fast on different rows removing them
            //while you are using adapter animations
            val betterPosition = holder.adapterPosition
            if (betterPosition != -1) {
                todoListModel.setDone(isChecked, betterPosition)
            }
        }

        holder.itemView.todolist_description_text.text = "" + item.description
    }

    override fun getItemCount(): Int {
        return todoListModel.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
