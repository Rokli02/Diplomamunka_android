package me.uni.hiker.utils

import android.content.Context
import android.graphics.Canvas
import android.util.Size
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object MapUtils {
    private val bitmapDescriptorResCache: Cache<String, BitmapDescriptor?> = Cache(120)

    fun bitmapDescriptorRes(
        context: Context,
        resId: Int,
        size: Size? = null,
    ): BitmapDescriptor? {
        val key = "$resId-${size?.width}x${size?.height}"
        bitmapDescriptorResCache[key]?.let {
            return it
        }

        val drawable = ContextCompat.getDrawable(context, resId) ?: return null
        val width = size?.width ?: drawable.intrinsicWidth
        val height = size?.height ?: drawable.intrinsicHeight
        drawable.setBounds(0, 0, width, height)
        val bm = createBitmap(width, height)

        // draw it onto the bitmap
        val canvas = Canvas(bm)
        drawable.draw(canvas)
        val bitmapDesc = BitmapDescriptorFactory.fromBitmap(bm)
        bitmapDescriptorResCache[key] = bitmapDesc

        return bitmapDesc
    }
}