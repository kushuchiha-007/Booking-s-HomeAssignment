package com.booking.tripsassignment.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.booking.tripsassignment.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * Helper to load images using Glide, with automatic fallback:
 * given a list of URLs, we chain them via Glide#error( RequestBuilder ),
 * so if URL1 404s we automatically try URL2, etc.
 */
object ImageLoader {
    private val FALLBACK_OPTIONS = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .skipMemoryCache(false)
        .centerCrop()
        .error(R.drawable.hotel_placeholder)

    /**
     * Tries each url in [imageUrls] in turn until one loads successfully,
     * or else shows the placeholder.
     */
    fun loadImage(view: ImageView, imageUrls: List<String>) {
        if (imageUrls.isEmpty()) {
            view.setImageResource(R.drawable.hotel_placeholder)
            return
        }

        // Build the primary request
        var chain: RequestBuilder<Drawable>? = null
        for (url in imageUrls) {
            val req = Glide.with(view)
                .load(url)
                .apply(FALLBACK_OPTIONS)

            // if previous fails, Glide will fall back to this one
            chain = chain?.error(req) ?: req
        }

        // final safety: if _all_ fail, show our placeholder
        chain?.error(R.drawable.hotel_placeholder)?.into(view)
    }
}
