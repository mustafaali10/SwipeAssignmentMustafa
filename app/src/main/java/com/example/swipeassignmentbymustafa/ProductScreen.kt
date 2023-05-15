package com.example.swipeassignmentbymustafa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductScreen : AppCompatActivity() {

    private val options = arrayOf("Product",
                                "Service")

    private lateinit var  autoCompleteTextView:AutoCompleteTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_screen)

        autoCompleteTextView= findViewById(R.id.autoCompleteTextView)
        val adapter = ArrayAdapter<String>(this, R.layout.list_item, options)

        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val selectedItem = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(applicationContext,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }

        val nameEditText = findViewById<EditText>(R.id.name)
        val taxEditText = findViewById<EditText>(R.id.productTax)
        val priceEditText = findViewById<EditText>(R.id.sellingPrice)
        //autocomplete product_type textview already added
        val submitButton = findViewById<Button>(R.id.addProduct)

        submitButton.setOnClickListener {
            val productName = nameEditText.text.toString()
            val productType = autoCompleteTextView.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull()
            val tax = taxEditText.text.toString().toDoubleOrNull()

            if (productName.isNotEmpty() && productType.isNotEmpty() && price != null && tax != null) {
                val addProductRequest = PostModel(productName, productType, price, tax)
                addProductToApi(addProductRequest)
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun addProductToApi(request: PostModel){


        val retrofit = Retrofit.Builder()
            .baseUrl("https://app.getswipe.in/api/")
            .addConverterFactory(GsonConverterFactory.create()) // Optional if you want to use Gson for JSON parsing
            .build()

        val swipeApi=retrofit.create(ApiService::class.java)

        val productNameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), request.product_name)
        val productTypeRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), request.product_type)
        val priceRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), request.price.toString())
        val taxRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), request.tax.toString())


        val call=swipeApi.addProducts(productNameRequestBody,
            productTypeRequestBody,
            priceRequestBody,
            taxRequestBody,
             null)


        //val serviceGenerator=ServiceGenerator.buildService(ApiService::class.java)
        //val call=serviceGenerator.addProducts(request)

        call.enqueue(object: Callback <ResponseBody>{


            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@ProductScreen,"Product Added",Toast.LENGTH_SHORT).show()
                    val intent:Intent= Intent(this@ProductScreen,MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ProductScreen,"${t.localizedMessage}",Toast.LENGTH_SHORT).show()
            }

        })










    }
}