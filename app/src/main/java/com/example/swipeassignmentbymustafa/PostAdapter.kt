package com.example.swipeassignmentbymustafa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Locale

class PostAdapter(private val allPosts: MutableList<PostModel>,private val searchView:androidx.appcompat.widget.SearchView):RecyclerView.Adapter<PostAdapter.PostViewHolder>(),Filterable {

    private var filteredPosts: MutableList<PostModel> = allPosts.toMutableList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.each_item,parent,false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredPosts.size

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post=filteredPosts[position]
        return holder.bindView(post)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.toLowerCase(Locale.ROOT)
                filteredPosts = if (query.isNullOrEmpty()) {
                    allPosts.toMutableList()
                } else {
                    allPosts.filter { post ->
                        post.product_name.toLowerCase(Locale.ROOT).contains(query)
                    }.toMutableList()
                }

                val filterResults = FilterResults()
                filterResults.values = filteredPosts
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredPosts = results?.values as? MutableList<PostModel> ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    fun setData(posts: List<PostModel>) {
        allPosts.clear()
        allPosts.addAll(posts)
        filter.filter(searchView.query)

    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName = itemView.findViewById<TextView>(R.id.productName)
        private val productType = itemView.findViewById<TextView>(R.id.productType)
        private val productPrice = itemView.findViewById<TextView>(R.id.productPrice)
        private val productTax = itemView.findViewById<TextView>(R.id.productTax)
        private val productImage = itemView.findViewById<ImageView>(R.id.productImage)

        fun bindView(postModel: PostModel) {
            productName.text = postModel.product_name
            productType.text = postModel.product_type
            productPrice.text = postModel.price.toString()
            productTax.text = postModel.tax.toString()
            Glide.with(itemView.context)
                .load(postModel.image)
                .placeholder(R.drawable.swipe)
                .into(productImage)
        }
    }
}