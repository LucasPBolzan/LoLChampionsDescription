package com.project.lolchampions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

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