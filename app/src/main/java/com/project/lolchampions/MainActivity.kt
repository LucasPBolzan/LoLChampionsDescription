package com.project.lolchampions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.project.lolchampions.ui.theme.LOLChampionsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LOLChampionsTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun MainScreen(onLoadCharactersClick: () -> Unit, onFiveXFiveClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackgroundImage()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        ContentButtons(
            onLoadCharactersClick = onLoadCharactersClick,
            onFiveXFiveClick = onFiveXFiveClick
        )
    }
}

@Composable
fun BackgroundImage() {
    Image(
        painter = rememberAsyncImagePainter(model = R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ContentButtons(onLoadCharactersClick: () -> Unit, onFiveXFiveClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = onLoadCharactersClick) {
                Text(text = "Carregar Personagens")
            }

            Button(onClick = onFiveXFiveClick) {
                Text(text = "5 X 5")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    LOLChampionsTheme {
        MainScreen(
            onLoadCharactersClick = {},
            onFiveXFiveClick = {}
        )
    }
}
