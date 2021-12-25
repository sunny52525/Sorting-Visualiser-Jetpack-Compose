package com.example.sortingvisualizer.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlin.random.Random

object Utils {
    fun generateRandomArray(size: Int, max: Int): MutableList<Int> {
        val array = ArrayList<Int>()
        for (i in 0 until size) {
            array.add(i, (Math.random() * max).toInt())
        }
        val list = mutableListOf<Int>()
        list.addAll(array)
        return list
    }

}