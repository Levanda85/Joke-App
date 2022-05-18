package com.levanda.jokeapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.levanda.jokeapp.databinding.ActivityMainBinding
import com.levanda.jokeapp.model.JokeModel
import com.levanda.jokeapp.service.JokeAPI
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val apiUrl = "https://joke.deno.dev"
    private var lastScope: CoroutineScope? = null
    private var currentJoke: JokeModel? = null
    private var isJokeExist: Boolean = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFunctional.setOnClickListener {
            if (isJokeExist) {
                setAnswerTextAndSetButtonText()
            } else {
                loadData()
            }
        }
    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(JokeAPI::class.java)
        val fetch = service.fetch()

        fetch.enqueue(object : Callback<JokeModel> {
            override fun onResponse(call: Call<JokeModel>, response: Response<JokeModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        currentJoke = JokeModel(joke = it.joke, answer = it.answer)
                        setJokeTextAndSetButtonText()
                    }
                }
            }

            override fun onFailure(call: Call<JokeModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun setJokeTextAndSetButtonText() {
        lastScope?.cancel()
        binding.tvJoke.text = currentJoke?.joke
        binding.tvAnswer.text = ""
        binding.btnFunctional.text = getString(R.string.before_joke_text)
        isJokeExist = true
    }

    private fun setAnswerTextAndSetButtonText() {
        clearTextsInTime()
        binding.tvAnswer.text = currentJoke?.answer
        binding.btnFunctional.text = getString(R.string.after_joke_text)
        isJokeExist = false
    }

    private fun clearTextsInTime(): Job = CoroutineScope(Dispatchers.IO).launch {
        lastScope = this
        delay(10000)
        clearJokeAndAnswerText()
    }

    private fun clearJokeAndAnswerText() {
        runOnUiThread {
            binding.tvJoke.text = ""
            binding.tvAnswer.text = ""
        }
    }
}