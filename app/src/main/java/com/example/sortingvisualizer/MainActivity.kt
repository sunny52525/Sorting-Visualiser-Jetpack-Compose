package com.example.sortingvisualizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sortingvisualizer.ui.theme.SortingVisualizerTheme
import com.example.sortingvisualizer.utils.Utils.generateRandomArray
import com.example.sortingvisualizer.utils.algorithms.bubbleSort
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SortingVisualizerTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    var timer by remember {
        mutableStateOf(0)
    }

    var array by remember {
        mutableStateOf(generateRandomArray(1000, 1000))
    }

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

        Text(text = "$timer", color = Color.White)

        Button(onClick = {

            if (!isSorting) {
                if (timer != 0) {
                    array = mutableListOf()
                    array = generateRandomArray(1000, 1000)
                }
                timer = 0
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
                    array = mutableListOf()
                    array = it
                }
            }

            scope.launch {
                while (sorting) {
                    timer++
                    delay(1000)

                }

            }
        }) {
            Text(text = "Hello $name!")
        }

        DrawingCanvas(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Red), items = array
        )

    }

}


@Composable
fun DrawingCanvas(
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