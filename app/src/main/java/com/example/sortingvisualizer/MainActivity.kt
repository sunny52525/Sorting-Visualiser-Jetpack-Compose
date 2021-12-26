package com.example.sortingvisualizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sortingvisualizer.models.SortingAlgorithms
import com.example.sortingvisualizer.ui.theme.SortingVisualizerTheme
import com.example.sortingvisualizer.utils.algorithms.bubbleSort
import com.example.sortingvisualizer.utils.algorithms.insertionSort
import com.example.sortingvisualizer.utils.algorithms.mergeSort
import com.example.sortingvisualizer.utils.algorithms.selectionSort
import com.example.sortingvisualizer.utils.generateRandomArray
import com.example.sortingvisualizer.utils.getScreenHeight
import com.example.sortingvisualizer.utils.sortingAlgorithms
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SortingVisualizerTheme(darkTheme = false) {
                val width = 70
                var comparing by remember {
                    mutableStateOf(Pair(0, 0))
                }
                val height = getScreenHeight() / 3 * 2
                var array by remember {
                    mutableStateOf(
//                        mutableListOf(100,1000,200,1500)
                        generateRandomArray(width, height)
                    )
                }

                var currentSortingAlgorithm by remember { mutableStateOf(SortingAlgorithms.BUBBLE) }
                val scope = rememberCoroutineScope()

                val onSortingChanged: (SortingAlgorithms) -> Unit = {
                    if (this::job.isInitialized)
                        job.cancel()
                    currentSortingAlgorithm = it
                    array = arrayListOf()
                    array = generateRandomArray(width, height)
                }

                val compare: (Int, Int) -> Unit = {first,second->
                    comparing = Pair(first,second)
                }
                val onArrayUpdated: (MutableList<Int>) -> Unit = {
                    array = arrayListOf()
                    array = it
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomAppBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                    .height(202.dp),
                                backgroundColor = Color.White

                            ) {
                                Column(
                                    Modifier
                                        .padding(vertical = 20.dp)
                                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),


                                    ) {
                                    LazyRow(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(20.dp)

                                    ) {

                                        items(sortingAlgorithms, key = {
                                            it.value
                                        }) {
                                            FancyButton(text = it.value) {
                                                onSortingChanged(it)
                                            }
                                        }

                                    }


                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {


                                        FancyButton(
                                            text = "Sort"
                                        ) {


                                            when (currentSortingAlgorithm) {
                                                SortingAlgorithms.BUBBLE -> {
                                                    job = scope.launch {
                                                        bubbleSort(
                                                            array,
                                                            onFinished = {

                                                            },
                                                            onUpdateItems = onArrayUpdated,
                                                            comparing = { first, second ->
                                                                comparing = Pair(first, second)
                                                            })
                                                    }
                                                }
                                                SortingAlgorithms.INSERTION -> {
                                                    job = scope.launch {
                                                        insertionSort(array, compare,onArrayUpdated)
                                                    }
                                                }
                                                SortingAlgorithms.MERGE -> {
                                                    val list = array
                                                    val temp = mutableListOf<Int>()
                                                    array.forEach { temp.add(it) }
                                                    job = scope.launch {
                                                        mergeSort(
                                                            list,
                                                            temp,
                                                            0,
                                                            compare,list.size - 1,
                                                            onArrayUpdated
                                                        )
                                                    }
                                                }
                                                SortingAlgorithms.QUICK -> {
                                                    job = scope.launch {
                                                        com.example.sortingvisualizer.utils.algorithms.quickSort(
                                                            list = array,
                                                            start = 0,
                                                            end = array.size - 1,
                                                            comparing = { first, second ->
                                                                comparing = Pair(first, second)
                                                            },
                                                            onUpdateItems = onArrayUpdated,
                                                        )
                                                    }
                                                }
                                                SortingAlgorithms.SELECTION -> {
                                                    job = scope.launch {
                                                        selectionSort(array, compare,onArrayUpdated)
                                                    }
                                                }
                                            }

                                        }


                                        FancyButton(text = "Stop", color = Color.Red) {
                                            if (this@MainActivity::job.isInitialized)
                                                job.cancel()
                                        }
                                    }
                                }
                            }
                        },

                        ) {
                        Column(modifier = Modifier.padding(it)) {
                            SortingBar(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                items = array,
                                comparing
                            )
                        }
                    }


                }
            }
        }
    }
}


@Composable
fun SortingBar(
    modifier: Modifier,
    items: MutableList<Int>,
    comparing: Pair<Int, Int>
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize()

    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height

//        items.forEachIndexed { i, height ->
//            drawLine(
//                start = Offset(x=0.0f,canvasHeight),
//                end = Offset(x=0.0f,10f),
//                color = if (i % 2 == 0) Color.Red else Color.Green,
//                strokeWidth = 10f,
//
//                )
//
//
//        }
//
//
        items.asReversed().forEachIndexed { index, i ->
            drawLine(
                end = Offset(x = index * 10.0f, y = canvasHeight),
                start = Offset(x = index * 10.0f, y = i.toFloat()),
                color = if (index == comparing.first || index == comparing.second) Color.Red else Color.Green,
                strokeWidth = 10f,
            )
        }


    }
}

fun percent(value: Int, total: Int): Float {
    return value.toFloat() / total.toFloat()
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun FancyButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    text: String = "Bubble", onClick: () -> Unit = {},

    ) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = color.copy(alpha = 0.5f),
        modifier = modifier
            .width(150.dp)

    ) {
        Box(
            modifier = Modifier
                .background(color = color.copy(alpha = 0.5f))
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .background(color = color.copy(alpha = 0.5f)),
            )
        }
    }


}