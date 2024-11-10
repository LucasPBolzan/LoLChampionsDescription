import android.os.Build
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.project.lolchampions.MainActivity
import com.project.lolchampions.SplashScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.util.ReflectionHelpers
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP], manifest = Config.NONE)
class SplashScreenTest {
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        // Configura o Build.FINGERPRINT para evitar o erro
        ReflectionHelpers.setStaticField(Build::class.java, "FINGERPRINT", "robolectric")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun splashScreen_isDisplayed() = runBlockingTest {
        composeTestRule.setContent {
            SplashScreen(onTimeout = {})
        }

        // Verifica se a tela de splash está sendo exibida
        composeTestRule
            .onNodeWithContentDescription("Splash Screen Logo")
            .assertExists()  // Assegura que o elemento com a descrição esteja presente
    }
}
