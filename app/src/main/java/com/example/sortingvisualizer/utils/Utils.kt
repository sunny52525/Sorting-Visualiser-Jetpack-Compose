package com.example.sortingvisualizer.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.sortingvisualizer.models.SortingAlgorithms.*

fun generateRandomArray(size: Int, max: Int): MutableList<Int> {
    val array = ArrayList<Int>()
    for (i in 0 until size) {
        array.add(i, (Math.random() * max).toInt())
    }
    val list = mutableListOf<Int>()
    list.addAll(array)
    return list
}

val sortingAlgorithms = arrayListOf(BUBBLE, INSERTION, MERGE, QUICK, SELECTION)

@Composable
fun getScreenWidth(): Int {
    val metrics = LocalContext.current.resources.displayMetrics
    return metrics.widthPixels
}

@Composable
fun getScreenHeight(): Int {
    val metrics = LocalContext.current.resources.displayMetrics
    return metrics.heightPixels
}