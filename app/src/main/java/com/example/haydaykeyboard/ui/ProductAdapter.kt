package com.example.haydaykeyboard.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.haydaykeyboard.R
import com.example.haydaykeyboard.data.Product
import com.example.haydaykeyboard.databinding.ItemProductBinding
import com.example.haydaykeyboard.util.loadProductImage

class ProductAdapter(
    private val isArabic: () -> Boolean,
    private val isFavorite: (String) -> Boolean,
    private val onFavoriteClick: (Product) -> Unit,
    private val onProductClick: (Product) -> Unit,
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            val arabic = isArabic()
            binding.tvName.text = if (arabic) product.nameAr else product.nameEn
            binding.tvMachine.text = if (arabic) product.machineNameAr else product.machineNameEn
            binding.tvLevel.text = binding.root.context.getString(R.string.product_level, product.level)
            binding.tvTapHint.text = binding.root.context.getString(R.string.tap_to_insert)
            binding.ivProduct.loadProductImage(product)

            val favorite = isFavorite(product.id)
            binding.btnFavorite.setImageResource(if (favorite) R.drawable.ic_star else R.drawable.ic_star_outline)
            binding.btnFavorite.contentDescription = binding.root.context.getString(if (favorite) R.string.unfavorite else R.string.favorite)

            binding.btnFavorite.setOnClickListener { onFavoriteClick(product) }
            binding.root.setOnClickListener { onProductClick(product) }
        }
    }

    private class Diff : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
    }
}
