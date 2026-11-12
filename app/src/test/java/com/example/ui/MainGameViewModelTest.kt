package com.example.ui

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MainGameViewModelTest {

    private lateinit var application: Application

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext()
        if (FirebaseApp.getApps(application).isEmpty()) {
            FirebaseApp.initializeApp(application)
        }
    }

    @Test
    fun testViewModelInstantiationSucceedsWithoutCrashing() {
        val viewModel = MainGameViewModel(application)
        assertNotNull("ViewModel instance should not be null", viewModel)
        assertEquals(ScreenRoute.MAIN_MENU, viewModel.currentScreen.value)
        assertNotNull(viewModel.currentUser)
        assertNotNull(viewModel.allTeams)
    }
}
