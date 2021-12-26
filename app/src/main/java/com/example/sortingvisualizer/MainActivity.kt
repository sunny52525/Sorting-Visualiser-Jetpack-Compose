package com.example.sortingvisualizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sortingvisualizer.models.SortingAlgorithms
import com.example.sortingvisualizer.ui.theme.SortingVisualizerTheme
import com.example.sortingvisualizer.utils.algorithms.*
import com.example.sortingvisualizer.utils.generateRandomArray
import com.example.sortingvisualizer.utils.getScreenHeight
import com.example.sortingvisualizer.utils.getScreenWidth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SortingVisualizerTheme(darkTheme = false) {
                val width = getScreenWidth()
                val height = getScreenHeight() / 3 * 2
                var array by remember {
                    mutableStateOf(generateRandomArray(width, height))
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
                                    .height(202.dp)

                            ) {
                                Column(
                                    Modifier
                                        .padding(vertical = 20.dp)
                                ) {
                                    LazyRow(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(20.dp)

                                    ) {
                                        enumValues<SortingAlgorithms>().forEach {
                                            item {
                                                FancyButton(text = it.value) {
                                                    onSortingChanged(it)
                                                }
                                            }
                                        }
//                                        items(sortingAlgorithms, key = {
//                                            it.value
//                                        }) {
//                                            FancyButton(text = it.value) {
//                                                onSortingChanged(it)
//                                            }
//                                        }

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
                                                        bubbleSort(array, onFinished = {

                                                        }, onUpdateItems = onArrayUpdated)
                                                    }
                                                }
                                                SortingAlgorithms.INSERTION -> {
                                                    job = scope.launch {
                                                        insertionSort(array, onArrayUpdated)
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
                                                            list.size - 1,
                                                            onArrayUpdated
                                                        )
                                                    }
                                                }
                                                SortingAlgorithms.QUICK -> {
                                                    job = scope.launch {
                                                        com.example.sortingvisualizer.utils.algorithms.quickSort(
                                                            array,
                                                            0,
                                                            array.size - 1,
                                                            onArrayUpdated
                                                        )
                                                    }
                                                }
                                                SortingAlgorithms.SELECTION -> {
                                                    job = scope.launch {
                                                        selectionSort(array, onArrayUpdated)
                                                    }
                                                }
                                                SortingAlgorithms.TWO_POINTER -> {

                                                    job = scope.launch {
                                                        twoPointerQuickSort(
                                                            array,
                                                            0,
                                                            array.size - 1,
                                                            onArrayUpdated
                                                        )
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
                        }
                    ) {
                        Column(modifier = Modifier.padding(it)) {
                            SortingBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.Red), items = array
                            )
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(array: MutableList<Int>, onArrayChange: (MutableList<Int>) -> Unit) {


    var isSorting by remember {
        mutableStateOf(false)
    }
    val sorting by derivedStateOf {
        isSorting
    }
    val scope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxSize()
            .border(1.dp, Color.Red)
            .background(Color.Black),
        verticalArrangement = Arrangement.Center
    ) {


        Button(onClick = {

            if (!isSorting) {

                onArrayChange(array)

            }
            isSorting = true
            scope.launch {
//                com.example.sortingvisualizer.utils.algorithms.quickSort(array, 0, array.size - 1) {
//                    isSorting = it == array
//                    array = mutableListOf()
//                    array = it
//                }
//
                bubbleSort(array, onFinished = {
                    isSorting = false
                }) {
                    isSorting = it == array
                    onArrayChange(it)
                }
            }

        }) {
            Text(text = "Sort")
        }


    }

}


@Composable
fun SortingBar(
    modifier: Modifier,
    items: MutableList<Int>
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize()

    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        items.forEachIndexed { i, height ->
            drawLine(
                start = Offset(x = (i).toFloat(), y = canvasHeight),
                end = Offset(x = (i).toFloat(), y = canvasHeight - height),
                color = Color.Green,
                strokeWidth = 1f,

                )


        }
    }
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
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }


}