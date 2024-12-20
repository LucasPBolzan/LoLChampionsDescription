package com.project.lolchampions

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.project.lolchampions.ui.theme.LOLChampionsTheme
import com.yariksoffice.lingver.Lingver

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Lingver.init(application)

        val localeChangeReceiver = LocaleChangeReceiver()
        val intentFilter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        registerReceiver(localeChangeReceiver, intentFilter)

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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
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
        Card(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Transparent),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "League of Legends Champions",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0000FF),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Button(
                    onClick = onLoadCharactersClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Carregar Personagens",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onFiveXFiveClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "5 X 5",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
