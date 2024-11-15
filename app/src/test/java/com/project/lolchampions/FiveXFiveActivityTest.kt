package com.project.lolchampions

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.ibm.icu.impl.SimpleFormatterImpl.IterInternal.step
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class FiveXFiveActivityTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<FiveXFiveActivity>()

    @Test
    fun verificaSeTimesSaoGerados() = run {
        step("Verifica se os times são gerados e exibidos na tela") {
            composeTestRule.onNodeWithText("Time 1").assertIsDisplayed()
            composeTestRule.onNodeWithText("Time 2").assertIsDisplayed()

            // Verifica se existem 5 personagens listados para cada time
            composeTestRule.onAllNodes(hasParent(hasText("Time 1")) and hasText { true })
                .assertCountEquals(5)
            composeTestRule.onAllNodes(hasParent(hasText("Time 2")) and hasText { true })
                .assertCountEquals(5)
        }
    }

    @Test
    fun verificaBotaoMostrarOcultarOpcoes() = run {
        step("Verifica o funcionamento do botão de mostrar/ocultar opções") {
            composeTestRule.onNodeWithText("Mostrar opções").assertIsDisplayed()
            composeTestRule.onNodeWithText("Mostrar opções").performClick()
            composeTestRule.onNodeWithText("Ocultar opções").assertIsDisplayed()
            composeTestRule.onNodeWithText("Recarregar").assertIsDisplayed()
            composeTestRule.onNodeWithText("Compartilhar").assertIsDisplayed()
            composeTestRule.onNodeWithText("Salvar").assertIsDisplayed()
            composeTestRule.onNodeWithText("Ocultar opções").performClick()
            composeTestRule.onNodeWithText("Mostrar opções").assertIsDisplayed()
            composeTestRule.onNodeWithText("Recarregar").assertDoesNotExist()
        }
    }

    @Test
    fun verificaBotaoRecarregar() = run {
        step("Verifica se o botão Recarregar atualiza os times") {
            composeTestRule.onNodeWithText("Mostrar opções").performClick()

            // Obtém os nomes dos personagens antes de recarregar
            val time1Antes = obterListaPersonagens("Time 1")
            composeTestRule.onNodeWithText("Recarregar").performClick()
            composeTestRule.waitForIdle()
            val time1Depois = obterListaPersonagens("Time 1")

            // Verifica se a lista de personagens mudou após recarregar
            assert(time1Antes != time1Depois) { "Os times não foram atualizados após recarregar." }
        }
    }

    private fun obterListaPersonagens(time: String): List<String> {
        val personagens = mutableListOf<String>()
        composeTestRule.onNodeWithText(time).assertExists()
        composeTestRule.onAllNodes(hasParent(hasText(time)) and hasText { true })
            .forEach { node ->
                val texto = node.fetchSemanticsNode().config.getOrNull(SemanticsProperties.Text)?.text
                if (!texto.isNullOrEmpty()) {
                    personagens.add(texto)
                }
            }
        return personagens
    }

    @Test
    fun verificaBotaoSalvar() = run {
        step("Verifica se o botão Salvar exibe uma mensagem de sucesso") {
            composeTestRule.onNodeWithText("Mostrar opções").performClick()
            composeTestRule.onNodeWithText("Salvar").performClick()

            // Verificar Toast usando Espresso dentro do Kaspresso
            composeTestRule.activityRule.scenario.onActivity { activity ->
                onView(withText("Times 5x5 salvos com sucesso!"))
                    .inRoot(withDecorView(not(activity.window.decorView)))
                    .check(matches(isDisplayed()))
            }
        }
    }

    @Test
    fun verificaBotaoCompartilhar() = run {
        step("Verifica se o botão Compartilhar abre a intent de compartilhamento") {
            composeTestRule.onNodeWithText("Mostrar opções").performClick()
            composeTestRule.onNodeWithText("Compartilhar").performClick()

            // Usando Intents para verificar se a Intent de compartilhamento foi disparada
            Intents.init()
            Intents.intended(hasAction(Intent.ACTION_CHOOSER))
            Intents.release()
        }
    }
}
