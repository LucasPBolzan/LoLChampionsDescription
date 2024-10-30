package com.project.lolchampions

import android.media.MediaPlayer
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CharacterListScreen(onCharacterClick: (Character) -> Unit) {
    val scope = rememberCoroutineScope()
    val characterList = remember { mutableStateOf<List<Character>>(emptyList()) }
    val searchQuery = remember { mutableStateOf("") }
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

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
            .background(Color.Black)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
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
                CharacterListItem(character = character, onClick = {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()

                    val audioResId = context.resources.getIdentifier(character.id, "raw", context.packageName)
                    if (audioResId != 0) {
                        mediaPlayer = MediaPlayer.create(context, audioResId)
                        mediaPlayer?.start()
                    }

                    onCharacterClick(character)
                })
            }
        }
    }
}

