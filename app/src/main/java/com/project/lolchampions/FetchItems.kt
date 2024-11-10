package com.project.lolchampions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection

suspend fun fetchItems(
    baseUrl: String = "http://girardon.com.br:3001",
    networkCall: suspend (String) -> String = { url ->
        val urlConnection = java.net.URL(url).openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.inputStream.bufferedReader().use { it.readText() }
    }
): List<Item> {
    return withContext(Dispatchers.IO) {
        val items = mutableListOf<Item>()
        var page = 1
        val size = 20

        while (true) {
            val apiUrl = "$baseUrl/items?page=$page&size=$size"
            val result = networkCall(apiUrl)  // Usa a função `networkCall` para obter a resposta

            val jsonArray = JSONArray(result)

            if (jsonArray.length() == 0) {
                break
            }

            for (i in 0 until jsonArray.length()) {
                val itemJson = jsonArray.getJSONObject(i)

                val name = itemJson.getString("name")
                val description = itemJson.getString("description")

                val priceJson = itemJson.getJSONObject("price")
                val price = Price(
                    base = priceJson.getInt("base"),
                    total = priceJson.getInt("total"),
                    sell = priceJson.getInt("sell")
                )

                val purchasable = itemJson.getBoolean("purchasable")
                val icon = itemJson.getString("icon")

                items.add(Item(name, description, price, purchasable, icon))
            }

            if (jsonArray.length() < size) {
                break
            }

            page += 1
        }

        return@withContext items
    }
}
