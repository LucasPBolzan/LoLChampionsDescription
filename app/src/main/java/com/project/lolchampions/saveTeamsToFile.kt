package com.project.lolchampions

import android.content.Context
import java.io.File

fun saveTeamsToFile(context: Context, teamOne: List<Character>, teamTwo: List<Character>) {
    val fileName = "times_5x5.txt"
    val file = File(context.filesDir, fileName)

    val teamText = buildString {
        appendLine("Time 1:")
        teamOne.forEach { character ->
            appendLine("${character.name} - ${character.title}")
        }
        appendLine()
        appendLine("Time 2:")
        teamTwo.forEach { character ->
            appendLine("${character.name} - ${character.title}")
        }
    }

    file.writeText(teamText)
}
