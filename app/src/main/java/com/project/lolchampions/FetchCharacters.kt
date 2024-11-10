package com.project.lolchampions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

suspend fun fetchCharacters(): List<Character> {
    return withContext(Dispatchers.IO) {
        val characters = mutableListOf<Character>()
        var page = 1
        val size = 20

        while (true) {
            val url = URL("http://girardon.com.br:3001/champions?page=$page&size=$size")
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"

            try {
                val inputStream = urlConnection.inputStream
                val result = inputStream.bufferedReader().use { it.readText() }

                val jsonArray = JSONArray(result)

                if (jsonArray.length() == 0) {
                    break
                }

                for (i in 0 until jsonArray.length()) {
                    val characterJson = jsonArray.getJSONObject(i)

                    val id = characterJson.getString("id")
                    val key = characterJson.getString("key")
                    val name = characterJson.getString("name")
                    val title = characterJson.getString("title")
                    val lore = characterJson.getString("description")

                    val icon = if (characterJson.has("icon") && !characterJson.isNull("icon")) {
                        characterJson.getString("icon")
                    } else {
                        ""
                    }

                    val tags = characterJson.getJSONArray("tags").let { tagsArray ->
                        List(tagsArray.length()) { index -> tagsArray.getString(index) }
                    }
                    val statsJson = characterJson.getJSONObject("stats")
                    val stats = Stats(
                        hp = statsJson.getInt("hp"),
                        mp = statsJson.getInt("mp"),
                        movespeed = statsJson.getInt("movespeed"),
                        armor = statsJson.getInt("armor"),
                        spellblock = statsJson.getDouble("spellblock"),
                        attackdamage = statsJson.getInt("attackdamage")
                    )

                    characters.add(Character(id, key, name, title, lore, tags, stats, icon))
                }

                if (jsonArray.length() < size) {
                    break
                }

                page += 1
            } finally {
                urlConnection.disconnect()
            }
        }

        return@withContext characters
    }
}
