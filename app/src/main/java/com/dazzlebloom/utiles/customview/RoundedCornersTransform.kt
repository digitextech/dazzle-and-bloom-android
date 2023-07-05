package com.dazzlebloom.utiles.customview


import android.content.Context
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;


class RoundedCornersTransform(context: Context) : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }
        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)
        val paint = Paint()
      //  val shader = BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP)
       // paint.setShader(shader)
        paint.setAntiAlias(true)
        val r = size / 8f
        canvas.drawRoundRect(
            RectF(0F, 0F, source.width.toFloat(), source.height.toFloat()),
            r,
            r,
            paint
        )
        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "rounded_corners"
    }
}