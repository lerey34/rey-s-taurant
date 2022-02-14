package fr.isen.rey.androiderestaurant

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import fr.isen.rey.androiderestaurant.databinding.ActivityLoginBinding
import fr.isen.rey.androiderestaurant.model.UserData
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.idLogin.setOnClickListener {
            login()
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnPaiement.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val mdp1 = binding.mdp1.text
        val email = binding.email.text

        if (mdp1.isEmpty() || email.isEmpty()){
            Snackbar.make(binding.root, "Un élément est vide!", Snackbar.LENGTH_SHORT).show()
        } else {
            val url = "http://test.api.catering.bluecodegames.com/user/login"
            val jsonObject = JSONObject()

            jsonObject.put("id_shop", 1)
            jsonObject.put("email", email.toString())
            jsonObject.put("password", mdp1.toString())

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                {reponse ->
                    val userResult = Gson().fromJson(reponse.toString(), UserData::class.java)
                    //Log.e("user", userResult.data.id)
                    val objectSharedPreferences = this?.getSharedPreferences("id", Context.MODE_PRIVATE)
                    val editeur = objectSharedPreferences.edit()
                    editeur.putInt("idUser", userResult.data.id.toInt())
                    editeur.commit()
                    Snackbar.make(binding.root, "Bien connecté", Snackbar.LENGTH_SHORT).show()
                }, {
                    Log.e("erreur : ", it.toString())
                }
            )
            Volley.newRequestQueue(this).add(jsonRequest)
        }
    }
}