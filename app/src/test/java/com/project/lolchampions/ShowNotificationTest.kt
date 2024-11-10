import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.project.lolchampions.showNotification
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mockStatic
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])  // Configura para simular Android Oreo (ou superior)
class ShowNotificationTest {

    @Test
    fun `test showNotification runs without errors`() {
        val context = mock<Context>()

        val applicationInfo = ApplicationInfo().apply { targetSdkVersion = Build.VERSION_CODES.O }
        whenever(context.applicationInfo).thenReturn(applicationInfo)

        val notificationManager = mock<NotificationManager>()
        val notificationManagerCompat = mock<NotificationManagerCompat>()
        whenever(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager)

        mockStatic(NotificationManagerCompat::class.java).use { mockedNotificationManagerCompat ->
            mockedNotificationManagerCompat.`when`<NotificationManagerCompat> { NotificationManagerCompat.from(context) }
                .thenReturn(notificationManagerCompat)

            showNotification(context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                verify(notificationManager).createNotificationChannel(any<NotificationChannel>())
            }

            verify(notificationManagerCompat).notify(eq(101), any())
        }
    }
}
