package me.uni.hiker.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object MapUtils {
    private val bitmapResCache: MutableMap<String, BitmapDescriptor?> = mutableMapOf()

    fun bitmapDescriptorRes(
        context: Context,
        resId: Int,
        size: Size? = null,
    ): BitmapDescriptor? {
        val key = "$resId-${size?.width}x${size?.height}"
        bitmapResCache[key].let {
            if (it != null) return it
        }

        val drawable = ContextCompat.getDrawable(context, resId) ?: return null
        val width = size?.width ?: drawable.intrinsicWidth
        val height = size?.height ?: drawable.intrinsicHeight
        drawable.setBounds(0, 0, width, height)
        val bm = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        // draw it onto the bitmap
        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        val bitmapDesc = BitmapDescriptorFactory.fromBitmap(bm)
        bitmapResCache[key] = bitmapDesc

        return bitmapDesc
    }
}