package com.umairadil.androidjetpack.utils

import android.content.Context
import android.graphics.Typeface
import io.reactivex.annotations.NonNull

class Utils {

    companion object {

        @NonNull
        private val ourInstance = Utils()

        @NonNull
        fun getInstance(): Utils {
            return ourInstance
        }

    }

    fun setTypeface(@NonNull type: Int, @NonNull context: Context): Typeface {

        val assets = context.assets

        var font = Typeface.createFromAsset(assets, "fonts/Roboto-Black.ttf")

        when (type) {
            0 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Black.ttf")
            1 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-BlackItalic.ttf")
            2 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Bold.ttf")
            3 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-BoldItalic.ttf")
            4 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Italic.ttf")
            5 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")
            6 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-LightItalic.ttf")
            7 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Medium.ttf")
            8 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-MediumItalic.ttf")
            9 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Regular.ttf")
            10 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Thin.ttf")
            11 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-ThinItalic.ttf")
            12 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Bold.ttf")
            13 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-BoldItalic.ttf")
            14 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Italic.ttf")
            15 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Light.ttf")
            16 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-LightItalic.ttf")
            17 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Regular.ttf")
        }

        return font
    }
}