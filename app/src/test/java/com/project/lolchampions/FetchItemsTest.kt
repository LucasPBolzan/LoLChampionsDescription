import com.project.lolchampions.fetchItems
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import java.net.HttpURLConnection

class FetchItemsTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchItems_returnsEmptyList_forEmptyJsonArray() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("[]")
        )

        val result = fetchItems(baseUrl = mockWebServer.url("/").toString())

        assertEquals(0, result.size)
    }

    @Test
    fun fetchItems_returnsListOfItems_forValidJsonArray() = runBlocking {
        val jsonResponse = """
            [
                {
                    "name": "Item1",
                    "description": "Description of Item1",
                    "price": {"base": 300, "total": 800, "sell": 400},
                    "purchasable": true,
                    "icon": "icon1.png"
                },
                {
                    "name": "Item2",
                    "description": "Description of Item2",
                    "price": {"base": 500, "total": 1000, "sell": 700},
                    "purchasable": false,
                    "icon": "icon2.png"
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonResponse)
        )

        val result = fetchItems(baseUrl = mockWebServer.url("/").toString())

        assertEquals(2, result.size)

        assertEquals("Item1", result[0].name)
        assertEquals("Item2", result[1].name)
        assertEquals(300, result[0].price.base)
        assertEquals(500, result[1].price.base)
    }
}
