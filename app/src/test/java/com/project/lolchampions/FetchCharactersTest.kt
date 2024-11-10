package com.project.lolchampions

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FetchCharactersTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(3001)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchCharacters returns correct list of characters`() = runBlocking {
        val characterJson = JSONObject().apply {
            put("id", "aatrox")
            put("key", "266")
            put("name", "Aatrox")
            put("title", "the Darkin Blade")
            put("description", "Once honored defenders of Shurima...")
            put("icon", "https://example.com/aatrox.png")
            put("tags", JSONArray(listOf("Fighter", "Tank")))
            put("stats", JSONObject().apply {
                put("hp", 580)
                put("mp", 0)
                put("movespeed", 345)
                put("armor", 38)
                put("spellblock", 32)
                put("attackdamage", 60)
            })
        }
        val responseArray = JSONArray().put(characterJson)
        mockWebServer.enqueue(MockResponse().setBody(responseArray.toString()).setResponseCode(200))

        val characters = fetchCharacters()

        assertEquals(1, characters.size)
        val character = characters[0]
        assertEquals("aatrox", character.id)
        assertEquals("Aatrox", character.name)
        assertEquals("the Darkin Blade", character.title)
        assertEquals(580, character.stats.hp)
    }
}
