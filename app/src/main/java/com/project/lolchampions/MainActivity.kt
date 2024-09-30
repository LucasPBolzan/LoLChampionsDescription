package com.project.lolchampions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.project.lolchampions.ui.theme.LOLChampionsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

data class Character(
    val id: String,
    val key: String,
    val name: String,
    val title: String,
    val lore: String,
    val tags: List<String>,
    val stats: Stats,
    val icon: String
)

data class Stats(
    val hp: Int,
    val mp: Int,
    val movespeed: Int,
    val armor: Int,
    val spellblock: Double,
    val attackdamage: Int
)

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

suspend fun fetchCharacters(): List<Character> {
    return withContext(Dispatchers.IO) {
        val characters = mutableListOf<Character>()
        val url = URL("http://girardon.com.br:3001/champions")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"

        return@withContext try {
            val inputStream = urlConnection.inputStream
            val result = inputStream.bufferedReader().use { it.readText() }

            val jsonArray = JSONArray(result)

            for (i in 0 until jsonArray.length()) {
                val characterJson = jsonArray.getJSONObject(i)

                val id = characterJson.getString("id")
                val key = characterJson.getString("key")
                val name = characterJson.getString("name")
                val title = characterJson.getString("title")
                val lore = characterJson.getString("description")

                val icon = if (characterJson.has("icon") && !characterJson.isNull("icon")) {
                    characterJson.getString("icon")
                } else {
                    ""
                }

                val tags = characterJson.getJSONArray("tags").let { tagsArray ->
                    List(tagsArray.length()) { index -> tagsArray.getString(index) }
                }
                val statsJson = characterJson.getJSONObject("stats")
                val stats = Stats(
                    hp = statsJson.getInt("hp"),
                    mp = statsJson.getInt("mp"),
                    movespeed = statsJson.getInt("movespeed"),
                    armor = statsJson.getInt("armor"),
                    spellblock = statsJson.getDouble("spellblock"),
                    attackdamage = statsJson.getInt("attackdamage")
                )

                characters.add(Character(id, key, name, title, lore, tags, stats, icon))
            }

            characters
        } finally {
            urlConnection.disconnect()
        }
    }
}

// Nova Composable para a Splash Screen

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.splashbg2),
            contentDescription = "Splash Screen Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    LaunchedEffect(Unit) {
        val fetchedCharacters = fetchCharacters()
        delay(4000)
        onTimeout()
    }
}

@Composable
fun MainScreen(onLoadCharactersClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.background),
            contentDescription = "Imagem de fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = onLoadCharactersClick) {
                Text(text = "Carregar Personagens")
            }
        }
    }
}

@Composable
fun CharacterListScreen(onCharacterClick: (Character) -> Unit) {
    val scope = rememberCoroutineScope()
    val characterList = remember { mutableStateOf<List<Character>>(emptyList()) }
    val searchQuery = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            val fetchedCharacters = fetchCharacters()
            characterList.value = fetchedCharacters
        }
    }

    val filteredCharacters = characterList.value.filter { character ->
        character.name.contains(searchQuery.value, ignoreCase = true)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            placeholder = { Text("Pesquisar personagem") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(filteredCharacters) { character ->
                CharacterListItem(character = character, onClick = { onCharacterClick(character) })
            }
        }
    }
}

@Composable
fun CharacterListItem(character: Character, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val imageUrl = character.icon.ifEmpty {
                "https://via.placeholder.com/48"
            }

            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "${character.name} icon",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(48.dp)
            )

            Text(
                text = character.name,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun CharacterDetailScreen(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val imageUrl = character.icon.ifEmpty {
            "https://via.placeholder.com/200"
        }

        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "${character.name} icon",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
            text = "Título: ${character.title}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
            text = "História:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Text(
            text = character.lore,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        Text(
            text = "Tags: ${character.tags.joinToString(", ")}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Card(
            modifier = Modifier
                .padding(top = 16.dp)
                .shadow(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Estatísticas",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    text = "HP: ${character.stats.hp}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Armadura: ${character.stats.armor}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Dano de Ataque: ${character.stats.attackdamage}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val characterList = remember { mutableStateOf<List<Character>>(emptyList()) }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
        composable("main") {
            MainScreen(onLoadCharactersClick = {
                navController.navigate("characterList")
            })
        }
        composable("characterList") {
            LaunchedEffect(Unit) {
                characterList.value = fetchCharacters()
            }
            CharacterListScreen { character ->
                navController.navigate("characterDetail/${character.name}")
            }
        }
        composable(
            "characterDetail/{characterName}",
            arguments = listOf(navArgument("characterName") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterName = backStackEntry.arguments?.getString("characterName")

            val character = characterList.value.find { it.name == characterName }

            if (character != null) {
                CharacterDetailScreen(character = character)
            } else {
                Text(text = "Personagem não encontrado.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    LOLChampionsTheme {
        MainScreen { }
    }
}
