package com.project.lolchampions

import ItemList
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun FiveXFiveActivity() {
    val scope = rememberCoroutineScope()
    val teamOne = remember { mutableStateOf<List<Character>>(emptyList()) }
    val teamTwo = remember { mutableStateOf<List<Character>>(emptyList()) }
    val context = LocalContext.current
    var areButtonsVisible by remember { mutableStateOf(false) }

    fun generateTeams() {
        scope.launch {
            try {
                val fetchedCharacters = fetchCharacters()
                val fetchedItems = fetchItems()

                if (fetchedCharacters.size >= 10) {
                    val shuffledCharacters = fetchedCharacters.shuffled()
                    teamOne.value = shuffledCharacters.take(5).map { character ->
                        character.copy(items = fetchedItems.shuffled().take(6))
                    }
                    teamTwo.value = shuffledCharacters.takeLast(5).map { character ->
                        character.copy(items = fetchedItems.shuffled().take(6))
                    }
                    showNotification(context)
                } else {
                    Toast.makeText(
                        context,
                        "Não há personagens suficientes para gerar os times.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Erro ao buscar personagens ou itens.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun saveTeams(teamOne: List<Character>, teamTwo: List<Character>) {
        try {
            saveTeamsToFile(context, teamOne, teamTwo)
            Toast.makeText(context, "Times 5x5 salvos com sucesso!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao salvar os times.", Toast.LENGTH_SHORT).show()
        }
    }

    fun formatTeamText(teamName: String, team: List<Character>): String {
        return team.joinToString(separator = "\n") { character -> "${character.name} - ${character.title}" }
            .let { "$teamName:\n$it" }
    }

    fun shareTeams(teamOne: List<Character>, teamTwo: List<Character>, context: Context) {
        val teamOneText = formatTeamText("Time 1", teamOne)
        val teamTwoText = formatTeamText("Time 2", teamTwo)
        val shareText = "$teamOneText\n\n$teamTwoText"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Compartilhar Times 5x5"))
    }

    LaunchedEffect(Unit) {
        generateTeams()
    }

    val positions = listOf("Topo", "Selva", "Meio", "Atirador", "Suporte")
    Spacer(modifier = Modifier.height(40.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF00008B))
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Time 1",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            items(teamOne.value.zip(positions)) { (character, position) ->
                CharacterCard(character = character, position = position)
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFB22222))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Time 2",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            items(teamTwo.value.zip(positions)) { (character, position) ->
                CharacterCard(character = character, position = position)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (areButtonsVisible) "Ocultar opções" else "Mostrar opções",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            IconButton(
                onClick = { areButtonsVisible = !areButtonsVisible }
            ) {
                Icon(
                    imageVector = if (areButtonsVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Mostrar opções"
                )
            }
        }

        if (areButtonsVisible) {
            Button(
                onClick = { generateTeams() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Recarregar")
            }

            Button(
                onClick = { shareTeams(teamOne.value, teamTwo.value, context) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Compartilhar")
            }

            Button(
                onClick = { saveTeams(teamOne.value, teamTwo.value) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Salvar")
            }
        }
    }


    }
    @Composable
    fun CharacterCard(character: Character, position: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                CharacterListItemWithPosition(character = character, position = position)
                Spacer(modifier = Modifier.height(8.dp))

                ItemList(items = character.items.take(6))
            }
        }
    }









