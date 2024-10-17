package com.project.lolchampions

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.*


class FiveXFiveActivityTest {

    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManagerCompat

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        notificationManager = mock(NotificationManagerCompat::class.java)
    }

    fun generateTeams(characterList: List<Character>): Pair<List<Character>, List<Character>> {
        if (characterList.size < 10) {
            return Pair(emptyList(), emptyList())
        }

        val shuffledCharacters = characterList.shuffled()
        val teamOne = shuffledCharacters.take(5)
        val teamTwo = shuffledCharacters.takeLast(5)
        return Pair(teamOne, teamTwo)
    }

    @Test
    fun `test generateTeams generates teams correctly`() {

        val characterList = listOf(
            Character("1", "key1", "Champion 1", "Title 1", "Lore 1", listOf("Tag1"), Stats(100, 200, 300, 50, 30.5, 100), "icon1"),
            Character("2", "key2", "Champion 2", "Title 2", "Lore 2", listOf("Tag2"), Stats(200, 300, 400, 60, 40.5, 200), "icon2"),
        )

        val (teamOne, teamTwo) = generateTeams(characterList)

        assertEquals(5, teamOne.size)
        assertEquals(5, teamTwo.size)
    }


}
