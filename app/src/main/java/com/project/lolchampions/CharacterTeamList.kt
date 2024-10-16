package com.project.lolchampions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CharacterTeamList(team: List<Character>, positions: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(team.zip(positions)) { (character, position) ->
            CharacterListItemWithPosition(character = character, position = position)
        }
    }
}

@Composable
fun CharacterListItemWithPosition(character: Character, position: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
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

            Column {
                Text(
                    text = "$position: ${character.name}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = character.title,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}