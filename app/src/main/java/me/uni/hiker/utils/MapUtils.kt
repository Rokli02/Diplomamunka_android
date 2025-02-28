package me.uni.hiker.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object MapUtils {
    private val bitmapResCache: MutableMap<Int, BitmapDescriptor?> = mutableMapOf()

    fun bitmapDescriptorRes(
        context: Context,
        resId: Int,
    ): BitmapDescriptor? {
        bitmapResCache[resId].let {
            if (it != null) return it
        }

        val drawable = ContextCompat.getDrawable(context, resId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bm = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // draw it onto the bitmap
        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        val bitmapDesc = BitmapDescriptorFactory.fromBitmap(bm)
        bitmapResCache[resId] = bitmapDesc

        return bitmapDesc
    }
}