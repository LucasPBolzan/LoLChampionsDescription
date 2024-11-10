import android.content.Context
import com.project.lolchampions.Character
import com.project.lolchampions.Item
import com.project.lolchampions.Price
import com.project.lolchampions.Stats
import com.project.lolchampions.saveTeamsToFile
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File

class SaveTeamsToFileTest {

    private lateinit var mockContext: Context
    private lateinit var filesDir: File

    @Before
    fun setUp() {
        mockContext = Mockito.mock(Context::class.java)
        filesDir = File.createTempFile("tempDir", null).apply { delete(); mkdir() }

        Mockito.`when`(mockContext.filesDir).thenReturn(filesDir)
    }

    @Test
    fun `test saveTeamsToFile writes correct content`() {
        val teamOne = listOf(
            Character(
                id = "1",
                key = "key1",
                name = "Character1",
                title = "Title1",
                lore = "Lore1",
                tags = listOf("Mage"),
                stats = Stats(hp = 500, mp = 300, movespeed = 350, armor = 20, spellblock = 30.0, attackdamage = 60),
                icon = "icon1.png",
                items = listOf(
                    Item(name = "Item1", description = "Description1", price = Price(300, 800, 400), purchasable = true, icon = "item1.png")
                )
            ),
            Character(
                id = "2",
                key = "key2",
                name = "Character2",
                title = "Title2",
                lore = "Lore2",
                tags = listOf("Warrior"),
                stats = Stats(hp = 600, mp = 200, movespeed = 340, armor = 25, spellblock = 32.0, attackdamage = 70),
                icon = "icon2.png",
                items = listOf(
                    Item(name = "Item2", description = "Description2", price = Price(500, 1000, 600), purchasable = true, icon = "item2.png")
                )
            )
        )

        val teamTwo = listOf(
            Character(
                id = "3",
                key = "key3",
                name = "Character3",
                title = "Title3",
                lore = "Lore3",
                tags = listOf("Assassin"),
                stats = Stats(hp = 450, mp = 100, movespeed = 370, armor = 18, spellblock = 28.5, attackdamage = 80),
                icon = "icon3.png",
                items = listOf(
                    Item(name = "Item3", description = "Description3", price = Price(400, 900, 500), purchasable = false, icon = "item3.png")
                )
            ),
            Character(
                id = "4",
                key = "key4",
                name = "Character4",
                title = "Title4",
                lore = "Lore4",
                tags = listOf("Tank"),
                stats = Stats(hp = 700, mp = 150, movespeed = 300, armor = 30, spellblock = 35.0, attackdamage = 50),
                icon = "icon4.png",
                items = listOf(
                    Item(name = "Item4", description = "Description4", price = Price(600, 1200, 700), purchasable = true, icon = "item4.png")
                )
            )
        )

        saveTeamsToFile(mockContext, teamOne, teamTwo)

        val expectedContent = """
            Time 1:
            Character1 - Title1
            Character2 - Title2
            
            Time 2:
            Character3 - Title3
            Character4 - Title4
        """.trimIndent()

        val savedFile = File(filesDir, "times_5x5.txt")
        assertTrue(savedFile.exists())
        val actualContent = savedFile.readText()
        assertTrue(actualContent.contains(expectedContent))
    }
}
