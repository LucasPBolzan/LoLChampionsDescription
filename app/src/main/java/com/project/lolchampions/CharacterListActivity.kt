package com.project.lolchampions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

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