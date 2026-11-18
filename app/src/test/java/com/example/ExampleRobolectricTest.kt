package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LottiePreferences
import com.example.model.LottiePetState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("My Friend Lottie", appName)
  }

  @Test
  fun `test pet state persistence in preferences`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = LottiePreferences(context)
    val testState = LottiePetState(
        name = "Bambú",
        hunger = 95f,
        bambooCoins = 250,
        hasCompletedOnboarding = true
    )
    prefs.savePetState(testState)
    val loaded = prefs.loadPetState()
    assertEquals("Bambú", loaded.name)
    assertEquals(95f, loaded.hunger, 0.01f)
    assertEquals(250, loaded.bambooCoins)
    assertTrue(loaded.hasCompletedOnboarding)
  }
}
