package fr.isen.rey.androiderestaurant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import fr.isen.rey.androiderestaurant.databinding.ActivityDetailBinding
import fr.isen.rey.androiderestaurant.model.EntreeModel
import fr.isen.rey.androiderestaurant.model.PanierModel
import fr.isen.rey.androiderestaurant.model.PanierResult
import java.io.File

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    var prix = 0.0;
    var total = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val entree = intent.getSerializableExtra("entree") as EntreeModel

        binding.plus.setOnClickListener {
            total++
            prix = entree.prices[0].price.toDouble() * total
            binding.nb.text = total.toString()
            binding.total.text = "Ajouter au panier : " + prix.toString() + " €"
        }

        binding.moins.setOnClickListener {
            if (total > 1) total-- else total = 1
            prix = entree.prices[0].price.toDouble() * total
            binding.nb.text = total.toString()
            binding.total.text = "Ajouter au panier : " + prix.toString() + " €"
        }

        binding.btnBack.setOnClickListener {
            changeCategory("Entrées")
        }

        binding.btnPanier.setOnClickListener {
            changeCategory("Panier")
        }

        binding.btnTotal.setOnClickListener {
            val itemPaniers = PanierModel(entree.name_fr, entree.pictures[0], entree.prices[0].price.toDouble(), total)
            val filename = "/panier.json"
            val file = File("/data/user/0/fr.isen.rey.androiderestaurant/cache" + filename)
            var totalItem:Int =0
            var nameItem :String =""
            var inPanier:Boolean = false
            Snackbar.make(view, "Ajouté au panier", Snackbar.LENGTH_SHORT).show()

            val picture = entree.pictures[0]
            if(picture.isNotEmpty()){
                itemPaniers.pictures = picture
            } else {
                itemPaniers.pictures = "R.drawable.empty_food.toString()"
            }

            if (file.exists()) {
                var entreePanier = Gson().fromJson(file.readText(), PanierResult::class.java)

                for(item in entreePanier.itemPanier){
                    if (item.name_fr == itemPaniers.name_fr){
                        totalItem = item.total + itemPaniers.total
                        nameItem = item.name_fr
                        inPanier = true
                    } else {
                        inPanier = false
                    }
                }

                if(entreePanier.itemPanier.size == 0) inPanier = false

                if (inPanier == false)  entreePanier.itemPanier = entreePanier.itemPanier + itemPaniers

                entreePanier.itemPanier.forEach { if (it.name_fr== nameItem ) it.total = totalItem}

                file.writeText(Gson().toJson(PanierResult(entreePanier.itemPanier)))
            }
            else {
                //val txt2: List<PanierModel> = itemPaniers
                file.writeText(Gson().toJson(PanierResult(arrayListOf(itemPaniers))))
            }

        }

        afficheDetail(entree)
    }

    private fun changeCategory(str : String) {
        val intent =  Intent(this,EntreeActivity::class.java)
        intent.putExtra("CATEGORY_NAME", str)
        startActivity(intent)
    }

    private fun afficheDetail(entree: EntreeModel) {
        binding.detailTitle.text = entree.name_fr
        binding.prix.text = entree.getFormattedPrice()
        binding.ingredient.text = entree.ingredients.joinToString(", ") { it.name_fr }
        prix = entree.prices[0].price.toDouble() * total
        binding.nb.text = total.toString()
        binding.total.text = "Ajouter au panier : " + prix.toString() + " €"
        val picture = entree.pictures[0]
       if(picture.isNotEmpty()){
           Picasso.get()
               .load(picture)
               .error(R.drawable.empty_food_w)
               .placeholder(R.drawable.empty_food_w)
               .into(binding.detailPicture)
       } else {
           binding.detailPicture.setImageResource(R.drawable.empty_food_w)
       }
    }
}