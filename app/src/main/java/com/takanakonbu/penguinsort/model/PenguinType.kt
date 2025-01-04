package com.takanakonbu.penguinsort.model

import android.annotation.SuppressLint

enum class PenguinType(private val resourceName: String) {
    NORMAL("normal"),
    RED("red"),
    BLUE("blue"),
    GREEN("green"),
    ORANGE("orange"),
    PURPLE("purple"),
    GRAY("gray"),
    SKY("sky"),
    LIGHTGREEN("lightgreen"),
    PINK("pink");

    @SuppressLint("DiscouragedApi")
    fun getResourceId(context: android.content.Context): Int {
        return context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )
    }
}