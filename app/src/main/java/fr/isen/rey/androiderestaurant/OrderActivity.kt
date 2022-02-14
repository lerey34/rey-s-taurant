package fr.isen.rey.androiderestaurant

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import fr.isen.rey.androiderestaurant.databinding.ActivityOrderBinding
import fr.isen.rey.androiderestaurant.model.PanierResult
import org.json.JSONObject
import java.io.File

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private var totalPrice = 0.0
    private var totalPlat = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnBack.setOnClickListener {
            changeCategory("Entrées")
        }

        binding.btnBack.setOnClickListener {
            changeCategory("Entrées")
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.orderPanier.setOnClickListener {
            val objectSharedPreferences = this?.getSharedPreferences("id", Context.MODE_PRIVATE)
            val idUser = objectSharedPreferences.getInt("idUser", 0)
            if (idUser != 0){
                binding.progressBar.visibility = View.VISIBLE
                val filename = "/panier.json"
                val file = File("/data/user/0/fr.isen.rey.androiderestaurant/cache" + filename)
                if (file.exists()) {
                    var entreePanier = Gson().fromJson(file.readText(), PanierResult::class.java)
                    val url = "http://test.api.catering.bluecodegames.com/user/order"
                    val jsonObject = JSONObject()

                    jsonObject.put("id_shop", 1)
                    jsonObject.put("id_user", idUser)
                    jsonObject.put("msg", entreePanier)

                    val jsonRequest = JsonObjectRequest(
                        Request.Method.POST, url, jsonObject,
                        {reponse ->
                            binding.progressBar.visibility = View.INVISIBLE
                            Snackbar.make(view, "Votre commande est en cours de livraison", Snackbar.LENGTH_SHORT).show()
                        }, {
                            Log.e("erreur : ", it.toString())
                        }
                    )
                    Volley.newRequestQueue(this).add(jsonRequest)
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        afficheRessources()
    }

    private fun afficheRessources() {
        val filename = "/panier.json"
        val file = File("/data/user/0/fr.isen.rey.androiderestaurant/cache" + filename)
        if (file.exists()) {
            var entreePanier = Gson().fromJson(file.readText(), PanierResult::class.java)
            for (item in entreePanier.itemPanier) {
                totalPrice += (item.prices * item.total)
                totalPlat += item.total
            }
            binding.prix.text = totalPrice.toString() + " €"
            binding.nb.text = totalPlat.toString() + " Plats"
        }
    }

    private fun changeCategory(str : String) {
        val intent =  Intent(this,EntreeActivity::class.java)
        intent.putExtra("CATEGORY_NAME", str)
        startActivity(intent)
    }


}