package com.example.swipeassignmentbymustafa

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private lateinit var searchView: SearchView
    private lateinit var postAdapter: PostAdapter
    private lateinit var allPosts:MutableList<PostModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatingActionButton: FloatingActionButton = findViewById(R.id.floatingButton)

        floatingActionButton.setOnClickListener {
            val intent: Intent = Intent(this, ProductScreen::class.java)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getProducts()

        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<MutableList<PostModel>> {
            override fun onResponse(
                call: Call<MutableList<PostModel>>,
                response: Response<MutableList<PostModel>>
            ) {
                if (response.isSuccessful) {

                    allPosts = response.body()!!
                    postAdapter = PostAdapter(allPosts,searchView)
                    progressBar.visibility = View.GONE




                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = postAdapter
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<PostModel>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
                Log.e("API Failure", t.message ?: "Unknown error")
                progressBar.visibility = View.GONE

            }

        })


        searchView = findViewById(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {



                if (newText.isNullOrBlank()) {
                    postAdapter.setData(allPosts)
                    filterPosts("")
                    searchView.clearFocus()



                } else {
                    filterPosts(newText)
                }

                //newText?.let { filterPosts(it) }
                return true
            }
        })

        searchView.setOnCloseListener {
            filterPosts("")
            postAdapter.setData(allPosts)



            true


        }

    }

    private fun  filterPosts(query: String) {

        val filteredPosts = mutableListOf<PostModel>()
        for (post in allPosts) {
            if (post.product_name.contains(query, ignoreCase = true)) {
                filteredPosts.add(post)
            }
        }
        postAdapter.setData(filteredPosts)

    }




}