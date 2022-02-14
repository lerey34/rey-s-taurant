package fr.isen.rey.androiderestaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import fr.isen.rey.androiderestaurant.R
import fr.isen.rey.androiderestaurant.databinding.PanierCellBinding
import fr.isen.rey.androiderestaurant.model.PanierModel
import fr.isen.rey.androiderestaurant.model.PanierResult
import java.io.File

class panierAdapter(private val data: List<PanierModel>, val onEntreeClick: (PanierModel) -> Unit): RecyclerView.Adapter<panierAdapter.ViewHolder>() {
    private var list: MutableList<PanierModel> = data as MutableList<PanierModel>
    class ViewHolder(binding: PanierCellBinding): RecyclerView.ViewHolder(binding.root){
        val panierPicture = binding.panierPicture
        val panierName = binding.panierName
        val panierPrice = binding.panierPrice
        val totalItem = binding.totalItem
        val suppPanier = binding.suppPanier
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PanierCellBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.panierName.text = list[position].name_fr
        holder.panierPrice.text = "Total : " + (list[position].prices * list[position].total).toString() + " €"
        holder.totalItem.text = list[position].total.toString() + " Plats"

        val picture = list[position].pictures
        if(picture.isNotEmpty()){
            Picasso.get()
                .load(picture)
                .error(R.drawable.empty_food)
                .placeholder(R.drawable.empty_food)
                .into(holder.panierPicture)
        } else {
            holder.panierPicture.setImageResource(R.drawable.empty_food)
        }

        holder.suppPanier.setOnClickListener {
            val filename = "/panier.json"
            val file = File("/data/user/0/fr.isen.rey.androiderestaurant/cache" + filename)
            if (file.exists()) {
                var entreePanier = Gson().fromJson(file.readText(), PanierResult::class.java)

                entreePanier.itemPanier.forEach {
                    if (it.name_fr== list[position].name_fr ) {
                        val itemPaniers = PanierModel(list[position].name_fr, list[position].pictures, list[position].prices, list[position].total)
                        entreePanier.itemPanier = entreePanier.itemPanier - itemPaniers
                        file.writeText(Gson().toJson(PanierResult(entreePanier.itemPanier)))
                    }
                }
                onEntreeClick(list[position])
                list.removeAt(position)
                notifyDataSetChanged()
            }
        }

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = list.size
}