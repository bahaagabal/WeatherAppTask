package com.example.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import com.example.core.domain.Result
import androidx.datastore.preferences.core.stringPreferencesKey

@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceImpRealDataStoreTest {

    private lateinit var tempFile: File
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataSource: LocalDataSourceImp

    private val cityKey = stringPreferencesKey("last_searched_city")

    @Before
    fun setup() {
        tempFile = File.createTempFile("test_prefs", ".preferences_pb")
        if (tempFile.exists()) tempFile.delete()

        dataStore = PreferenceDataStoreFactory.create {
            tempFile
        }

        dataSource = LocalDataSourceImp(dataStore)
    }

    @After
    fun cleanup() {
        tempFile.delete()
    }

    @Test
    fun `saveLastSearchedCity saves successfully`() = runTest {
        val result = dataSource.saveLastSearchedCity("Cairo")
        assertTrue(result is Result.Success)

        val prefs = dataStore.data.first()
        assertEquals("Cairo", prefs[cityKey])
    }

    @Test
    fun `getLastSearchedCity returns value if exists`() = runTest {
        // Save value manually
        dataStore.edit { it[cityKey] = "Alex" }

        val result = dataSource.getLastSearchedCity()
        assertTrue(result is Result.Success)
        assertEquals("Alex", (result as Result.Success).data)
    }
}
