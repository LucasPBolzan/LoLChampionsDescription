package com.project.lolchampions

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

@Composable
fun FiveXFiveActivity() {
    val scope = rememberCoroutineScope()
    val teamOne = remember { mutableStateOf<List<Character>>(emptyList()) }
    val teamTwo = remember { mutableStateOf<List<Character>>(emptyList()) }
    val context = LocalContext.current

    fun showNotification(context: Context) {
        val channelId = "5x5_generated_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Geração de Times 5x5",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificação para indicar que os times 5x5 foram gerados."
            }
            notificationManager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
                return
            }
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Times 5x5 Gerados")
            .setContentText("Os times do 5x5 foram gerados com sucesso!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(101, builder.build())
        }
    }

    fun generateTeams() {
        scope.launch {
            try {
                val fetchedCharacters = fetchCharacters()
                if (fetchedCharacters.size >= 10) {
                    val shuffledCharacters = fetchedCharacters.shuffled()
                    teamOne.value = shuffledCharacters.take(5)
                    teamTwo.value = shuffledCharacters.takeLast(5)
                    showNotification(context)
                } else {
                    Toast.makeText(context, "Não há personagens suficientes para gerar os times.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Erro ao buscar personagens.", Toast.LENGTH_SHORT).show()
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
        }

        Button(
            onClick = { generateTeams() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Recarregar")
        }

        Button(
            onClick = { shareTeams(teamOne.value, teamTwo.value, context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Compartilhar")
        }

        Button(
            onClick = { saveTeams(teamOne.value, teamTwo.value) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Salvar")
        }
    }
}
