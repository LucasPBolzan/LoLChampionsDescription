package com.project.lolchampions



import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun generateTeams(scope: CoroutineScope, teamOne: MutableState<List<Character>>, teamTwo: MutableState<List<Character>>, context: Context) {
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

fun saveTeams(teamOne: List<Character>, teamTwo: List<Character>, context: Context) {
    try {
        saveTeamsToFile(context, teamOne, teamTwo)
        Toast.makeText(context, "Times 5x5 salvos com sucesso!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Erro ao salvar os times.", Toast.LENGTH_SHORT).show()
    }
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

fun formatTeamText(teamName: String, team: List<Character>): String {
    return team.joinToString(separator = "\n") { character -> "${character.name} - ${character.title}" }
        .let { "$teamName:\n$it" }
}
