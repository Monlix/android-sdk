package com.monlixv2.util

import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.IOException


object QrCode {
    // Function to create the QR code
    @Throws(WriterException::class, IOException::class)
    fun createQR(
        data: String,
        charset: String?,
        height: Int, width: Int
    ): Bitmap? {
        val matrix = MultiFormatWriter().encode(
            String(data.toByteArray(charset(charset!!))),
            BarcodeFormat.QR_CODE, width, height
        )

        return createBitmap(matrix!!);
    }

    private fun createBitmap(matrix: BitMatrix): Bitmap? {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix[x, y]) BLACK else WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}
