package com.project.lolchampions

import android.content.Context
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.File
import java.nio.file.Path

class SaveTeamsToFileTest {

    @Test
    fun `test saveTeamsToFile writes correct content to file`(@TempDir tempDir: Path) {
        // Aqui a gente mocka um contexto do android, sem depender do framework do android
        val context = mock<Context>()
        // aqui o junit cria uma especie de pasta temporaria
        // adicionem logs e facam debugs para entender
        val filesDir = tempDir.toFile()
        // configura o filesDir do contexto pra retornar o diretorio temporario
        whenever(context.filesDir).thenReturn(filesDir)

        // cria a lista do time1
        val teamOne = listOf(
            Character(
                id = "1",
                key = "1",
                name = "Ashe",
                title = "the Frost Archer",
                lore = "",
                tags = listOf(),
                stats = Stats(0, 0, 0, 0, 0.0, 0),
                icon = "",
                items = emptyList()
            ),
            Character(
                id = "2",
                key = "2",
                name = "Garen",
                title = "The Might of Demacia",
                lore = "",
                tags = listOf(),
                stats = Stats(0, 0, 0, 0, 0.0, 0),
                icon = "",
                items = emptyList()
            )
        )

        // cria a lista do time 2
        val teamTwo = listOf(
            Character(
                id = "3",
                key = "3",
                name = "Ahri",
                title = "the Nine-Tailed Fox",
                lore = "",
                tags = listOf(),
                stats = Stats(0, 0, 0, 0, 0.0, 0),
                icon = "",
                items = emptyList()
            ),
            Character(
                id = "4",
                key = "4",
                name = "Darius",
                title = "the Hand of Noxus",
                lore = "",
                tags = listOf(),
                stats = Stats(0, 0, 0, 0, 0.0, 0),
                icon = "",
                items = emptyList()
            )
        )

        // Esse conteúdo é o que "deveria" estar no arquivo, se for diferente, tem algo dando ruim
        val expectedContent = """
            Time 1:
            Ashe - the Frost Archer
            Garen - The Might of Demacia

            Time 2:
            Ahri - the Nine-Tailed Fox
            Darius - the Hand of Noxus
        """.trimIndent()

        // Chama o método saveTeamsToFile com o contexto mockado e as listas de personagens
        // @lucas faz um teste, alterando o método lá na classe dele, para ver se o teste vai quebrar
        saveTeamsToFile(context, teamOne, teamTwo)

        // Asserts
        // Verificamos se o arquivo foi criado no diretório temporário
        // Tudo aqui é pra ver se o método fez o que tinha que fazer de forma coerente
        val file = File(filesDir, "times_5x5.txt")
        assertTrue(file.exists(), "O arquivo deve ser criado")
        // Aqui vamos ler o conteudo do arquivo criado
        val actualContent = file.readText().trim()
        // Compara o conteudo real do arquivo com o conteudo esperado
        // Se for diferente vai quebrar
        assertEquals(expectedContent, actualContent)
    }
}
