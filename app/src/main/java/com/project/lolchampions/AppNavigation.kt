package com.project.lolchampions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val characterList = remember { mutableStateOf<List<Character>>(emptyList()) }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
        composable("main") {
            MainScreen(
                onLoadCharactersClick = {
                    navController.navigate("characterList")
                },
                onFiveXFiveClick = {
                    navController.navigate("fiveXFive")
                }
            )
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
        composable("fiveXFive") {
            FiveXFiveActivity()
        }
    }
}