package fr.isen.rey.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import fr.isen.rey.androiderestaurant.databinding.ActivityRegisterBinding
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.idRegister.setOnClickListener {
            register()
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnPaiement.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register() {
        val nom = binding.nom.text
        val prenom = binding.prenom.text
        val mdp1 = binding.mdp1.text
        val email = binding.email.text
        val adress = binding.adresse.text
        if (nom.isEmpty() || prenom.isEmpty() || mdp1.isEmpty() || email.isEmpty() || adress.isEmpty()){
            Snackbar.make(binding.root, "Un élément est vide!", Snackbar.LENGTH_SHORT).show()
        } else {
            val url = "http://test.api.catering.bluecodegames.com/user/register"
            val jsonObject = JSONObject()

            jsonObject.put("id_shop", 1)
            jsonObject.put("firstname", prenom)
            jsonObject.put("lastname", nom)
            jsonObject.put("address", adress)
            jsonObject.put("email", email)
            jsonObject.put("password", mdp1)

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                {reponse ->
                    Snackbar.make(binding.root, "Bien enregistré", Snackbar.LENGTH_SHORT).show()
                }, {
                    Log.e("erreur : ", it.toString())
                }
            )
            Volley.newRequestQueue(this).add(jsonRequest)
        }
    }
}