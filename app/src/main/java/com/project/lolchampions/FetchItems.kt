package com.project.lolchampions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

suspend fun fetchItems(baseUrl: String = "http://girardon.com.br:3001"): List<Item> {
    return withContext(Dispatchers.IO) {
        val items = mutableListOf<Item>()
        var page = 1
        val size = 20

        while (true) {
            val url = URL("$baseUrl/items?page=$page&size=$size")
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
            } finally {
                urlConnection.disconnect()
            }
        }

        return@withContext items
    }
}
