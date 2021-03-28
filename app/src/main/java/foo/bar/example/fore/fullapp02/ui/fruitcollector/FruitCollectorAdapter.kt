package foo.bar.example.fore.fullapp02.ui.fruitcollector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.early.fore.adapters.Adaptable
import co.early.fore.adapters.Notifyable
import co.early.fore.kt.adapters.NotifyableImp
import foo.bar.example.fore.fullapp02.R
import foo.bar.example.fore.fullapp02.feature.fruitcollector.Fruit
import foo.bar.example.fore.fullapp02.feature.fruitcollector.FruitCollectorModel
import kotlinx.android.synthetic.main.fragment_fruitcollector_listitem.view.*


class FruitCollectorAdapter(
        private val fruitCollectorModel: FruitCollectorModel,
        private val notifyableImp: NotifyableImp<ViewHolder> = NotifyableImp(updateable = fruitCollectorModel)
) :
        RecyclerView.Adapter<FruitCollectorAdapter.ViewHolder>(),
        Notifyable<FruitCollectorAdapter.ViewHolder> by notifyableImp,
        Adaptable<Fruit> by fruitCollectorModel {

    init {
        notifyableImp.initializeAdapter(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_fruitcollector_listitem, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.tag = holder
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = fruitCollectorModel.getItem(position)

        holder.itemView.setBackgroundResource(if (item.isCitrus) R.color.colorYellow else R.color.colorPrimary)
        holder.itemView.fruititem_letter_text.text = "" + item.firstLetterUpperCase
        holder.itemView.fruititem_name_text.text = "" + item.name
        holder.itemView.fruititem_remove_button.setOnClickListener {
            val betterPosition = holder.adapterPosition
            if (betterPosition != -1) {
                fruitCollectorModel.removeFruit(betterPosition)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
