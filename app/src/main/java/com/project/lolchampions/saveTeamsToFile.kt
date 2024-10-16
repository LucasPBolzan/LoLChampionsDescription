package com.project.lolchampions

import android.content.Context
import java.io.File

fun saveTeamsToFile(context: Context, teamOne: List<Character>, teamTwo: List<Character>) {
    val fileName = "times_5x5.txt"
    val file = File(context.filesDir, fileName)

    val teamOneText = teamOne.joinToString(separator = "\n") { character ->
        "${character.name} - ${character.title}"
    }
    val teamTwoText = teamTwo.joinToString(separator = "\n") { character ->
        "${character.name} - ${character.title}"
    }
    val fileContent = """
        Time 1:
        $teamOneText
        
        Time 2:
        $teamTwoText
    """.trimIndent()

    file.writeText(fileContent)
}