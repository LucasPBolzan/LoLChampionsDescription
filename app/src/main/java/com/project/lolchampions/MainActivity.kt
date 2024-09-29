package com.project.lolchampions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.lolchampions.ui.theme.LOLChampionsTheme
import kotlinx.coroutines.Dispatchers
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
    val stats: Stats
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

                characters.add(Character(id, key, name, title, lore, tags, stats))
            }

            characters
        } finally {
            urlConnection.disconnect()
        }
    }
}

@Composable
fun MainScreen(onLoadCharactersClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onLoadCharactersClick) {
            Text(text = "Carregar Personagens")
        }
    }
}

@Composable
fun CharacterListScreen(onCharacterClick: (Character) -> Unit) {
    val scope = rememberCoroutineScope()
    val characterList = remember { mutableStateOf<List<Character>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            val fetchedCharacters = fetchCharacters()
            characterList.value = fetchedCharacters
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(characterList.value) { character ->
            CharacterListItem(character = character, onClick = { onCharacterClick(character) })
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
        Text(
            text = "Nome: ${character.name}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Título: ${character.title}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "História:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = character.lore,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Tags: ${character.tags.joinToString(", ")}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "HP: ${character.stats.hp}, Armadura: ${character.stats.armor}, Dano de Ataque: ${character.stats.attackdamage}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val characterList = remember { mutableStateOf<List<Character>>(emptyList()) }

    NavHost(navController = navController, startDestination = "main") {
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
fun GreetingPreview() {
    LOLChampionsTheme {
        MainScreen { }
    }
}
