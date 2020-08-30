package foo.bar.example.fore.fullapp02.ui.todolist

import androidx.fragment.app.Fragment
import foo.bar.example.fore.fullapp02.R


class TodoListFragment : Fragment(R.layout.fragment_todolist) {
    companion object {
        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }
}
