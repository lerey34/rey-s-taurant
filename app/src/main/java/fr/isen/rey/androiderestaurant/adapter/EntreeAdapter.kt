package fr.isen.rey.androiderestaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.rey.androiderestaurant.R
import fr.isen.rey.androiderestaurant.databinding.CategoryCellBinding
import fr.isen.rey.androiderestaurant.model.EntreeModel

class entreeAdapter(private val entree: List<EntreeModel>, val onEntreeClick: (EntreeModel) -> Unit): RecyclerView.Adapter<entreeAdapter.ViewHolder>() {
    class ViewHolder(binding: CategoryCellBinding): RecyclerView.ViewHolder(binding.root){
        val entreePicture = binding.entreePicture
        val entreeName = binding.entreeName
        val entreePrice = binding.entreePrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryCellBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.entreeName.text = entree[position].name_fr
        holder.entreePrice.text = entree[position].getFormattedPrice()

        Picasso.get()
            .load(entree[position].getFirstPicture())
            .error(R.drawable.empty_food)
            .placeholder(R.drawable.empty_food)
            .into(holder.entreePicture)

        holder.itemView.setOnClickListener {
            onEntreeClick(entree[position])
        }
    }

    override fun getItemCount(): Int = entree.size
}