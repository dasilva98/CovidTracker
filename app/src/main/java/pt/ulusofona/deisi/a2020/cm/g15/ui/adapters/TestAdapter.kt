package pt.ulusofona.deisi.a2020.cm.g15.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_test.view.*
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.TestEnt
import pt.ulusofona.deisi.a2020.cm.g15.ui.fragments.TestsFragmentDirections

class TestAdapter: RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    private var testList = emptyList<TestEnt>()

    class TestViewHolder(view: View) : RecyclerView.ViewHolder(view)
        /*override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                //listener.onTestClick(position)
            }
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return TestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false))
    }

    override fun getItemCount(): Int {
        return testList.size
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val currentItem = testList[position]
        holder.itemView.iconResultImage.setImageResource(currentItem.iconImageResource)
        holder.itemView.result.text = if (currentItem.result) "Positivo" else "Negativo"
        holder.itemView.date.text = currentItem.date

        holder.itemView.testCardFragment.setOnClickListener{
            val id = currentItem.uuid
            val resultString = if (currentItem.result) "Positivo" else "Negativo"
            val action = TestsFragmentDirections.actionTestsFragmentToDetailTestFragment(id)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(testEnts: List<TestEnt>){
        this.testList = testEnts
        Thread.sleep(50)
        notifyDataSetChanged()
    }
}
