package com.example.haydaykeyboard.util

import android.widget.ImageView
import coil.load
import com.example.haydaykeyboard.R
import com.example.haydaykeyboard.data.Product

fun ImageView.loadProductImage(product: Product) {
    fun tryCandidate(index: Int) {
        if (index >= product.localImageCandidates.size) {
            val remote = product.imageUrl
            if (remote.isNullOrBlank()) {
                setImageResource(R.drawable.ic_launcher)
            } else {
                load(remote) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher)
                    error(R.drawable.ic_launcher)
                }
            }
            return
        }
        val candidate = "file:///android_asset/${product.localImageCandidates[index]}"
        load(candidate) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher)
            error(R.drawable.ic_launcher)
            listener(onError = { _, _ -> tryCandidate(index + 1) })
        }
    }
    tryCandidate(0)
}
