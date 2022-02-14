package fr.isen.rey.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.rey.androiderestaurant.adapter.entreeAdapter
import fr.isen.rey.androiderestaurant.adapter.panierAdapter
import fr.isen.rey.androiderestaurant.databinding.ActivityEntreeBinding
import fr.isen.rey.androiderestaurant.model.*
import org.json.JSONObject
import java.io.File

class EntreeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntreeBinding
    private var all: Int = 0
    private var totalPrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEntreeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding = ActivityEntreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntree.setOnClickListener {
            changeCategory("Entrées")
        }

        binding.btnPlat.setOnClickListener {
            changeCategory("Plats")
        }

        binding.btnDessert.setOnClickListener {
            changeCategory("Desserts")
        }

        binding.btnPanier.setOnClickListener {
            changeCategory("Panier")
        }

        binding.orderPanier.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        var categoryTypes = intent.getStringExtra("CATEGORY_NAME") ?: ""

        binding.entreeTitle.text = categoryTypes

        if (binding.entreeTitle.text == "Panier"){
            binding.orderPanier.visibility = View.VISIBLE
            binding.all.visibility = View.INVISIBLE
            binding.allImage.visibility = View.INVISIBLE

            val filename = "/panier.json"
            val file = File("/data/user/0/fr.isen.rey.androiderestaurant/cache" + filename)
            if (file.exists())
            {
                var entreePanier = Gson().fromJson(file.readText(), PanierResult::class.java)
                for (item in entreePanier.itemPanier){
                    totalPrice = totalPrice + (item.prices * item.total)
                }
                binding.orderPanier.text = "Total : " + totalPrice.toString() + " €"
                //binding.entreeTitle.text = entreePanier.itemPanier[0].name_fr
                binding.entreeList.layoutManager = LinearLayoutManager(this)
                binding.entreeList.adapter = panierAdapter(entreePanier.itemPanier) {
                    totalPrice = 0.0
                    var entreePanier = Gson().fromJson(file.readText(), PanierResult::class.java)
                    for (item in entreePanier.itemPanier){
                        totalPrice = totalPrice + (item.prices * item.total)
                    }
                    binding.orderPanier.text = "Total : " + totalPrice.toString() + " €"
                }
            }
        }


        if (binding.entreeTitle.text == "Entrées" || binding.entreeTitle.text == "Plats" || binding.entreeTitle.text == "Desserts") {
            binding.orderPanier.visibility = View.INVISIBLE
            binding.all.visibility = View.VISIBLE
            binding.allImage.visibility = View.VISIBLE
            loadEntreeFromCategory(categoryTypes)
        }

        total()

    }

    private fun total() {
        val filename = "/panier.json"
        val file = File("/data/user/0/fr.isen.rey.androiderestaurant/cache" + filename)

        if (file.exists()) {
            var entreePanier = Gson().fromJson(file.readText(), PanierResult::class.java)

            for (item in entreePanier.itemPanier) {
                all += item.total
            }
        }
        binding.all.text = all.toString()
    }


    private fun changeCategory(str : String) {
        val intent =  Intent(this,EntreeActivity::class.java)
        intent.putExtra("CATEGORY_NAME", str)
        startActivity(intent)
    }

    private fun loadEntreeFromCategory(categoryType: String) {
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val jsonObject = JSONObject()
        jsonObject.put("id_shop", "1")

        val jsonRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            {reponse ->
                val entreeResutl = Gson().fromJson(reponse.toString(), EntreeResult::class.java)
                displayEntree(entreeResutl.data.firstOrNull {category -> category.name_fr == categoryType}?.items ?: listOf())
            }, {
                Log.e("EntreeActivity", "erreur lors de la recuperation")
            }
        )
        Volley.newRequestQueue(this).add(jsonRequest)
    }

    private fun displayEntree(entrees: List<EntreeModel>) {
        binding.entreeList.layoutManager = LinearLayoutManager(this)

        binding.entreeList.adapter = entreeAdapter(entrees) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("entree", it)
            startActivity(intent)
        }
    }
}