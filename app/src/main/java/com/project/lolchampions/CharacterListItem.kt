package com.project.lolchampions

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

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