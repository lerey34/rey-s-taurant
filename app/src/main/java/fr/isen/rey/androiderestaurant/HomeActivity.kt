package fr.isen.rey.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.rey.androiderestaurant.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeCategory("Entrées")
    }

    private fun changeCategory(str : String) {
        val intent =  Intent(this,EntreeActivity::class.java)
        intent.putExtra("CATEGORY_NAME", str)
        startActivity(intent)
    }
}