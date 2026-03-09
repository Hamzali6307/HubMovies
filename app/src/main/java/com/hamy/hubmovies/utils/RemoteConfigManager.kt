
package com.hamy.hubmovies.utils

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

object RemoteConfigManager {

    private val remoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // Fetch every hour
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        
        // Default values
        val defaultValues = mapOf(
            "splash_delay" to 3L,
            "splash_image_url" to "",
            "maintenance_mode" to false
        )
        remoteConfig.setDefaultsAsync(defaultValues)
    }

    suspend fun fetchValues() {
        try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSplashDelay(): Long = remoteConfig.getLong("splash_delay")
    fun getSplashImageUrl(): String = remoteConfig.getString("splash_image_url")
    fun isMaintenanceMode(): Boolean = remoteConfig.getBoolean("maintenance_mode")
}
