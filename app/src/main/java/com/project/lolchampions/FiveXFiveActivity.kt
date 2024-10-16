package com.project.lolchampions

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun FiveXFiveActivity() {
    val scope = rememberCoroutineScope()
    val characterList = remember { mutableStateOf<List<Character>>(emptyList()) }
    val teamOne = remember { mutableStateOf<List<Character>>(emptyList()) }
    val teamTwo = remember { mutableStateOf<List<Character>>(emptyList()) }
    val context = LocalContext.current

    fun generateTeams() {
        scope.launch {
            val fetchedCharacters = fetchCharacters()

            if (fetchedCharacters.size >= 10) {
                val shuffledCharacters = fetchedCharacters.shuffled()
                teamOne.value = shuffledCharacters.take(5)
                teamTwo.value = shuffledCharacters.takeLast(5)

                Toast.makeText(context, "Times 5x5 gerados com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareTeams() {
        val teamOneText = teamOne.value.joinToString(separator = "\n") { character ->
            "${character.name} - ${character.title}"
        }
        val teamTwoText = teamTwo.value.joinToString(separator = "\n") { character ->
            "${character.name} - ${character.title}"
        }

        val shareText = """
            Time 1:
            $teamOneText
            
            Time 2:
            $teamTwoText
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Compartilhar Times 5x5"))
    }

    fun saveTeams() {
        saveTeamsToFile(context, teamOne.value, teamTwo.value)
        Toast.makeText(context, "Times 5x5 salvos com sucesso!", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        generateTeams()
    }

    val positions = listOf("Topo", "Selva", "Meio", "Atirador", "Suporte")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Button(
                onClick = { generateTeams() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Recarregar")
            }
        }

        item {
            Text(
                text = "Time 1",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(teamOne.value.zip(positions)) { (character, position) ->
            CharacterListItemWithPosition(character = character, position = position)
        }

        item {
            Text(
                text = "Time 2",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        items(teamTwo.value.zip(positions)) { (character, position) ->
            CharacterListItemWithPosition(character = character, position = position)
        }

        item {
            Button(
                onClick = { shareTeams() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Compartilhar")
            }
        }

        item {
            Button(
                onClick = { saveTeams() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Salvar")
            }
        }
    }
}