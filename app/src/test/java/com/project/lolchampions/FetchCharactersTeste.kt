package com.project.lolchampions

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FetchCharactersTest {

    private lateinit var urlConnection: HttpURLConnection
    private lateinit var inputStream: InputStream
    private lateinit var bufferedReader: BufferedReader

    @BeforeEach
    fun setUp() {

        urlConnection = mock()
        inputStream = mock()
        bufferedReader = mock()

        val url: URL = mock()
        whenever(url.openConnection()).thenReturn(urlConnection)
        whenever(urlConnection.inputStream).thenReturn(inputStream)
    }

    @Test
    fun `fetchCharacters parses JSON response correctly`() = runBlocking {
        val jsonResponse = """
            [
                {
                    "id": "1",
                    "key": "champ1",
                    "name": "Champion 1",
                    "title": "The Brave",
                    "description": "A brave warrior",
                    "icon": "http://example.com/icon1.png",
                    "tags": ["Warrior"],
                    "stats": {
                        "hp": 1000,
                        "mp": 200,
                        "movespeed": 300,
                        "armor": 50,
                        "spellblock": 30.5,
                        "attackdamage": 100
                    }
                }
            ]
        """.trimIndent()

        whenever(bufferedReader.readLine()).thenReturn(jsonResponse, null)
        whenever(inputStream.bufferedReader()).thenReturn(bufferedReader)

        val characters = fetchCharacters()

        assertEquals(1, characters.size)
        assertEquals("Champion 1", characters[0].name)
        assertEquals(1000, characters[0].stats.hp)
        assertEquals(30.5, characters[0].stats.spellblock)
        assertEquals("http://example.com/icon1.png", characters[0].icon)
    }
}
