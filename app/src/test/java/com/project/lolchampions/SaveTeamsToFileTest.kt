package com.project.lolchampions

import android.content.Context
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.io.File

class SaveTeamsToFileTest {

    private lateinit var context: Context
    private lateinit var file: File

    @BeforeEach
    fun setUp() {
        context = mock()
        file = mock()

        whenever(context.filesDir).thenReturn(File("/mock/files/dir"))

        whenever(file.writeText(any())).then { /* Simula a escrita do arquivo */ }
    }

    @Test
    fun `test saveTeamsToFile writes correct content to file`() {

        val teamOne = listOf(
            Character("1", "key1", "Champion 1", "Title 1", "Lore 1", listOf("Tag1"), Stats(100, 200, 300, 50, 30.5, 100), "icon1")
        )
        val teamTwo = listOf(
            Character("2", "key2", "Champion 2", "Title 2", "Lore 2", listOf("Tag2"), Stats(200, 300, 400, 60, 40.5, 200), "icon2")
        )

        val captor = argumentCaptor<String>()

        saveTeamsToFile(context, teamOne, teamTwo)

        verify(file).writeText(captor.capture())

        val expectedContent = """
            Time 1:
            Champion 1 - Title 1
            
            Time 2:
            Champion 2 - Title 2
        """.trimIndent()

        assert(captor.firstValue == expectedContent)
    }
}
