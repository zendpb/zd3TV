package com.example.zd3_2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Menu : AppCompatActivity() {
    lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        pref = getSharedPreferences("PREF", MODE_PRIVATE)
    }




    fun channel1(view: View) {
        val intent = Intent(this, QuestsActivity::class.java)
        val stringArray: Set<String> =
            setOf("Pushpa", "Palm Springs", "The Substance", "Conclave")
        pref.edit().putStringSet("Array", stringArray).apply()
        startActivity(intent)
    }
    fun channel2(view: View) {
        val intent = Intent(this, QuestsActivity::class.java)
        val stringArray: Set<String> =
            setOf("Next", "Public Enemies", "The Pacifier", "Cinderella man", "Shark Tale", "Over The Hedge")
        pref.edit().putStringSet("Array", stringArray).apply()
        startActivity(intent)
    }
}