package com.example.zd3_2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.json.JSONObject

class QuestsActivity : AppCompatActivity() {

    lateinit var stringArray : Set<String>

    lateinit var gridLayout: GridLayout
    lateinit var view : View
    lateinit var edit : EditText
    lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quests)
        gridLayout = findViewById(R.id.gridLayout)
        view = findViewById(R.id.questsView)
        edit = findViewById(R.id.editSearch)
        pref = getSharedPreferences("PREF", MODE_PRIVATE)
        stringArray = pref.getStringSet("Array", setOf())!!



        var genreSpinner = findViewById<Spinner>(R.id.genreSpinner)
        val genres = arrayOf("Драма", "Комедия", "Экшен", "Хоррор", "Фантастика")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.setAdapter(adapter)



        genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedGenre = parent.getItemAtPosition(position).toString()
                val stringArray: Set<String>

                when (position) {
                    0 -> {
                       return
                    }
                    1 -> {
                        stringArray = setOf("Forrest Gump", "Moana", "Tucker & Dale vs Evil", "Red One")
                    }
                    2 -> {
                        stringArray = setOf("Sinister", "The Maze Runner", "Green Book", "Batman", "Spiderman", "Avengers")
                    }
                    3 -> {
                        stringArray = setOf("Sinister", "The Further", "Azrael", "Split")
                    }
                    4 -> {
                        stringArray = setOf("Sinister", "The Maze Runner", "Green Book", "Batman", "Spiderman", "Avengers")
                    }
                    else -> return
                }


                pref.edit().putStringSet("Array", stringArray).apply()


                val intent = Intent(this@QuestsActivity, QuestsActivity::class.java)
                startActivity(intent)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }





        val itemWidth = 1800 / 3 // Width for 3 columns

        var rows: Int = 0
        var columns: Int = 0

        if (stringArray != null) {
            for (item in stringArray)
            {
                var url = "https://www.omdbapi.com/?apikey=8424b5c9&t=" + item

                val queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(
                    com.android.volley.Request.Method.GET,
                    url,
                    { response ->
                        val obj = JSONObject(response)

                        val poster = obj.getString("Poster")
                        val title = obj.getString("Title")
                        val year = obj.getString("Year")

                        val linearLayout = LinearLayout(this@QuestsActivity).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = GridLayout.LayoutParams().apply {
                                rowSpec = GridLayout.spec(rows)
                                columnSpec = GridLayout.spec(columns)
                            }
                        }

                        // Создаем FrameLayout для наложения градиента на изображение
                        val frameLayout = FrameLayout(this@QuestsActivity).apply {
                            layoutParams = LinearLayout.LayoutParams(itemWidth, LayoutParams.MATCH_PARENT)
                        }

                        // Создаем ImageView для постера
                        val imageView = ImageView(this@QuestsActivity).apply {
                            val params = LinearLayout.LayoutParams(itemWidth, LayoutParams.MATCH_PARENT)
                            params.setMargins(20, 10, 20, 10)
                            layoutParams = params
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            Picasso.get()
                                .load(poster)
                                .placeholder(R.drawable.holder)
                                .into(this)
                            adjustViewBounds = true
                        }

                        // Создаем View для градиента
                        val gradientView = View(this@QuestsActivity).apply {
                            layoutParams = FrameLayout.LayoutParams(itemWidth, LayoutParams.MATCH_PARENT)
                            background = resources.getDrawable(R.drawable.image_gradient, null) // Используем ваш градиент
                        }

                        // Добавляем ImageView и градиент в FrameLayout
                        frameLayout.addView(imageView)
                        frameLayout.addView(gradientView)

                        // Создаём и задаем TextView для названия и года
                        val titleTextView = TextView(this@QuestsActivity).apply {
                            text = title
                            textSize = 20F
                            layoutParams = LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT)
                            gravity = Gravity.CENTER
                            setTextColor(getResources().getColor(R.color.white))
                        }

                        val yearTextView = TextView(this@QuestsActivity).apply {
                            text = year
                            textSize = 20F
                            layoutParams = LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT)
                            gravity = Gravity.CENTER
                            setTextColor(getResources().getColor(R.color.white))
                            setPadding(0, 0, 0, 16)
                        }

                        linearLayout.addView(frameLayout)
                        linearLayout.addView(titleTextView)
                        linearLayout.addView(yearTextView)

                        gridLayout.addView(linearLayout)

                        columns += 1
                        if (columns == 3) {
                            rows += 1
                            columns = 0
                        }
                    },
                    { error ->
                        // Обработка ошибок
                    }
                )

// Не забудьте добавить запрос в очередь
                queue.add(stringRequest)
            }
        }
    }

    fun Search(view: View) {
        if (edit.text.toString().isNotEmpty()) {
            if (stringArray != null) {
                for (item in stringArray) {
                    if (item.toLowerCase().contains(edit.text.toString().toLowerCase())) {
                        val url = "https://www.omdbapi.com/?apikey=8424b5c9&t=$item"

                        val queue = Volley.newRequestQueue(this)
                        val stringRequest = StringRequest(
                            com.android.volley.Request.Method.GET,
                            url,
                            { response ->
                                val obj = JSONObject(response)

                                val poster = obj.getString("Poster")
                                val title = obj.getString("Title")
                                val year = obj.getString("Year")
                                val genre = obj.getString("Genre")
                                val plot = obj.getString("Plot")

                                // Save data to SharedPreferences
                                pref.edit().apply {
                                    putString("poster", poster)
                                    putString("title", title)
                                    putString("year", year)
                                    putString("genre", genre)
                                    putString("plot", plot)
                                    apply()
                                }

                                // Start the Result activity after data is fetched
                                val intent = Intent(this, Result::class.java)
                                startActivity(intent)
                            },
                            {
                                Snackbar.make(view, "Не удалось выполнить запрос", Snackbar.LENGTH_LONG).show()
                            }
                        )
                        queue.add(stringRequest)
                        return // Exit the function after starting the request
                    }
                }
            }
            // Show error if no movie found
            val alert = AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Такой фильм не был найден")
                .setPositiveButton("Ok", null)
                .create()
                .show()
        } else {
            // Show error if input is empty
            val alert = AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Заполните пустое поле")
                .setPositiveButton("Ok", null)
                .create()
                .show()
        }
    }

    fun goBack(view: View) {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }
}