package com.dazzlebloom.utiles.customtypeface

import android.content.Context
import android.graphics.Typeface

class CustomTypeface(val context : Context){

    var ralewayMedium : Typeface?= null
    var ralewayBlack: Typeface?= null
    var ralewayBold : Typeface?= null
    var ralewayBoldItalic : Typeface?= null
    var ralewayExtraBold : Typeface?= null
    var ralewayExtraBoldItalic : Typeface?= null
    var ralewayExtraLight : Typeface?= null
    var ralewayExtraLightItalic: Typeface?= null
    var ralewayItalic : Typeface?= null
    var ralewayItalicVariable : Typeface?= null
    var ralewayLight : Typeface?= null
    var ralewayLightItalic : Typeface?= null
    var ralewayMediumItalic : Typeface?= null
    var ralewayRegular : Typeface?= null
    var ralewaySemiBold : Typeface?= null
    var ralewaySemiBoldItalic : Typeface?= null
    var ralewayThin : Typeface?= null
    var ralewayThinItalic : Typeface?= null
    var ralewayVariableFontWght : Typeface?= null

    init {
        ralewayMedium = Typeface.createFromAsset(context.getAssets(), "font/Raleway-Medium.ttf")
        ralewayBlack = Typeface.createFromAsset(context.getAssets(), "font/Raleway-Black.ttf")
        ralewayBold = Typeface.createFromAsset(context.getAssets(), "font/Raleway-Bold.ttf")
        ralewayBoldItalic = Typeface.createFromAsset(context.getAssets(), "font/Raleway-BoldItalic.ttf")
        ralewayExtraBold = Typeface.createFromAsset(context.getAssets(), "font/Raleway-ExtraBold.ttf")
        ralewayExtraBoldItalic = Typeface.createFromAsset(context.getAssets(), "font/Raleway-ExtraBoldItalic.ttf")
        ralewayExtraLight = Typeface.createFromAsset(context.getAssets(), "font/Raleway-ExtraLight.ttf")
        ralewayExtraLightItalic = Typeface.createFromAsset(context.getAssets(), "font/Raleway-ExtraLightItalic.ttf")
        ralewayItalic = Typeface.createFromAsset(context.getAssets(), "font/Raleway-Italic.ttf")
        ralewayItalicVariable = Typeface.createFromAsset(context.getAssets(), "font/Raleway-Italic-VariableFont_wght.ttf")
        ralewayLight = Typeface.createFromAsset(context.getAssets(), "font/Raleway-Light.ttf")
        ralewayLightItalic = Typeface.createFromAsset(context.getAssets(), "font/Raleway-LightItalic.ttf")
        ralewayMediumItalic = Typeface.createFromAsset(context.getAssets(), "font/Raleway-MediumItalic.ttf")
        ralewayRegular = Typeface.createFromAsset(context.getAssets(), "font/Raleway-Regular.ttf")
        ralewaySemiBold = Typeface.createFromAsset(context.getAssets(), "font/Raleway-SemiBold.ttf")
        ralewaySemiBoldItalic = Typeface.createFromAsset(context.getAssets(), "font/Raleway-SemiBoldItalic.ttf")
        ralewayThin = Typeface.createFromAsset(context.getAssets(), "font/Raleway-Thin.ttf")
        ralewayThinItalic = Typeface.createFromAsset(context.getAssets(), "font/Raleway-ThinItalic.ttf")
        ralewayVariableFontWght = Typeface.createFromAsset(context.getAssets(), "font/Raleway-VariableFont_wght.ttf")
    }
}