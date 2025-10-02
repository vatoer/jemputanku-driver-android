package id.stargan.jemputankudriver.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val DATASTORE_NAME = "onboarding_prefs"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

object OnboardingPreferences {
    private val ONBOARDING_SHOWN_KEY = booleanPreferencesKey("onboarding_shown")

    fun isOnboardingShown(context: Context): Flow<Boolean> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { prefs ->
                prefs[ONBOARDING_SHOWN_KEY] ?: false
            }

    suspend fun setOnboardingShown(context: Context, shown: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_SHOWN_KEY] = shown
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
