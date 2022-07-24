package pt.ulusofona.deisi.a2020.cm.g15.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_county.view.*
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.CountyEnt

class CountyAdapter: RecyclerView.Adapter<CountyAdapter.CountyViewHolder>() {

    private var countyList = emptyList<CountyEnt>()

    class CountyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountyViewHolder {
        return CountyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_county, parent, false))
    }

    override fun getItemCount(): Int {
        return countyList.size
    }

    override fun onBindViewHolder(holder: CountyViewHolder, position: Int) {
        val currentItem = countyList[position]
        holder.itemView.county.text = currentItem.county
        holder.itemView.district.text = currentItem.district
        holder.itemView.date.text = currentItem.date
        holder.itemView.incidence.text = currentItem.incidence.toString()
        holder.itemView.incidenceLevel.text = currentItem.riskLevel
        holder.itemView.population.text =currentItem.population.toString()
        holder.itemView.confirmed14.text = currentItem.cases14days.toString()
    }

    fun setData(countyEnt: MutableList<CountyEnt>){
        this.countyList = countyEnt
        Thread.sleep(50)
        notifyDataSetChanged()
    }
}