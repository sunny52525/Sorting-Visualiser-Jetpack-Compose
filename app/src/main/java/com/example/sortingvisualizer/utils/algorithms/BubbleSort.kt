package com.example.sortingvisualizer.utils.algorithms

import kotlinx.coroutines.delay

suspend fun bubbleSort(
    list: MutableList<Int>,
    onFinished:()->Unit,
    onUpdateItems: (MutableList<Int>) -> Unit
) {
    val n = list.size

    for (i in 0 until n - 1) {
        var swapped = false
        for (j in 0 until (n - i - 1)) {
            if (list[j] > list[j + 1]) {
                // swap
                val t = list[j]
                list[j] = list[j + 1]
                list[j + 1] = t
                swapped = true
            }
        }

        delay(1)
        onUpdateItems(list)
        if (!swapped) break
    }

    onFinished()

}

suspend fun quickSort(
    list: MutableList<Int>,
    start: Int,
    end: Int,
    onUpdateItems: (MutableList<Int>) -> Unit
) {
    if (start >= end) {
        return
    }

    val pivotIndex = partitionQuick(list, start, end, onUpdateItems)
    quickSort(list, start, pivotIndex - 1, onUpdateItems)
    quickSort(list, pivotIndex + 1, end, onUpdateItems)

    delay(1)
    onUpdateItems(list)
}

suspend fun partitionQuick(
    list: MutableList<Int>,
    start: Int,
    end: Int,
    onUpdateItems: (MutableList<Int>) -> Unit
): Int {
    var pivotIndex = start
    val pivotValue = list[end]

    for (i in start until end) {
        if (list[i] < pivotValue) {
            // swap list[i] and list[pivotIndex]
            val temp = list[i]
            list[i] = list[pivotIndex]
            list[pivotIndex] = temp

            ++pivotIndex
        }

        delay(1)
        onUpdateItems(list)
    }

    // swap list[pivotIndex] and list[end]
    val temp = list[pivotIndex]
    list[pivotIndex] = list[end]
    list[end] = temp

    onUpdateItems(list)

    return pivotIndex
}