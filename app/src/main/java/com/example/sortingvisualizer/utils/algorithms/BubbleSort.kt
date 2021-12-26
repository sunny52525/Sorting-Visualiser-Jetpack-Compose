package com.example.sortingvisualizer.utils.algorithms

import kotlinx.coroutines.delay

suspend fun bubbleSort(
    list: MutableList<Int>,
    onFinished:()->Unit,
    comparing:(Int,Int)->Unit,
    onUpdateItems: (MutableList<Int>) -> Unit
) {
    val n = list.size

    for (i in 0 until n - 1) {
        var swapped = false
        for (j in 0 until (n - i - 1)) {
            if (list[j] > list[j + 1]) {
                // swap
                comparing(j,j+1)
                val t = list[j]
                list[j] = list[j + 1]
                list[j + 1] = t
                swapped = true
            }
        }

        delay(100)
        onUpdateItems(list)
        if (!swapped) break
    }

    onFinished()

}

suspend fun quickSort(
    list: MutableList<Int>,
    start: Int,

    end: Int,
    comparing:(Int,Int)->Unit,

    onUpdateItems: (MutableList<Int>) -> Unit
) {
    if (start >= end) {
        return
    }

    val pivotIndex = partitionQuick(list, start, end,comparing, onUpdateItems)
    quickSort(list, start, pivotIndex - 1,comparing, onUpdateItems)
    quickSort(list, pivotIndex + 1, end, comparing = comparing, onUpdateItems)

    delay(100)
    onUpdateItems(list)
}

suspend fun partitionQuick(
    list: MutableList<Int>,
    start: Int,
    end: Int,
    comparing:(Int,Int)->Unit,

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
            comparing(i,pivotIndex)
            ++pivotIndex
        }

        delay(100)
        onUpdateItems(list)
    }

    // swap list[pivotIndex] and list[end]
    val temp = list[pivotIndex]
    list[pivotIndex] = list[end]
    list[end] = temp

    onUpdateItems(list)

    return pivotIndex
}
suspend fun selectionSort(

    list: MutableList<Int>,
    comparing:(Int,Int)->Unit,

    onUpdateItems: (MutableList<Int>) -> Unit
) {
    val n = list.size

    for (i in 0 until n - 1) {
        var indexMin = i

        for (j in i + 1 until n) {
            if (list[j] < list[indexMin]) {
                indexMin = j
            }
        }
        // swap
        val temp = list[i]
        list[i] = list[indexMin]
        list[indexMin] = temp
        comparing(i,indexMin)

        delay(100)
        onUpdateItems(list)
    }
}

suspend fun insertionSort(
    list: MutableList<Int>,
    comparing:(Int,Int)->Unit,

    onUpdateItems: (MutableList<Int>) -> Unit
) {
    val n = list.size

    for (i in 1 until n) {
        val value = list[i]
        var hole = i

        while (hole > 0 && list[hole - 1] > value) {
            list[hole] = list[hole - 1]
            comparing(hole,hole-1)
            --hole
        }
        delay(100)
        list[hole] = value
        onUpdateItems(list)
    }
}
suspend fun mergeSort(
    list: MutableList<Int>,
    temp: MutableList<Int>,
    leftStart: Int,
    comparing:(Int,Int)->Unit,

    rightEnd: Int,
    onUpdateItems: (MutableList<Int>) -> Unit
) {
    if (leftStart >= rightEnd) {
        return
    }

    val mid = (leftStart + rightEnd) / 2

    mergeSort(list, temp, leftStart,comparing, rightEnd, onUpdateItems)
    mergeSort(list, temp, mid + 1, comparing ,rightEnd, onUpdateItems)
    merge(list, temp,comparing, leftStart, rightEnd, onUpdateItems)
    delay(100)
    onUpdateItems(list)
}

suspend fun merge(
    list: MutableList<Int>,
    temp: MutableList<Int>,
    comparing:(Int,Int)->Unit,

    leftStart: Int,
    rightEnd: Int,
    onUpdateItems: (MutableList<Int>) -> Unit
) {
    val leftEnd = (leftStart + rightEnd) / 2
    val rightStart = leftEnd + 1
    val size = rightEnd - leftStart + 1

    var left = leftStart
    var right = rightStart
    var index = leftStart

    while (left <= leftEnd && right <= rightEnd) {

        if (list[left] <= list[right]) {
            temp[index] = list[left]
            left++
            comparing(index,left)
        } else {
            temp[index] = list[right]
            right++
            comparing(index,right)
        }
        index++
    }

    while (left <= leftEnd) {
        temp[index] = list[left]
        left++
        index++
    }

    while (right <= rightEnd) {
        temp[index] = list[right]
        right++
        index++
    }

    for (i in 0 until size) {
        list[leftStart + i] = temp[leftStart + i]
    }
    delay(100)
    onUpdateItems(list)
}


