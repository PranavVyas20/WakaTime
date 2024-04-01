package com.example.wakatime.ui.widgets.glance

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream

object WakaGlanceStateDefinition: GlanceStateDefinition<WakaUserSummaryResponseState> {
    const val fileName = "WIDGET_WAKA_DATASTORE_FILE"
    private val Context.wakaDataStore by dataStore(fileName, CurrentWeatherModelSerializer)

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<WakaUserSummaryResponseState> = context.wakaDataStore

    override fun getLocation(context: Context, fileKey: String): File = context.dataStoreFile(fileName)

    object CurrentWeatherModelSerializer :
        Serializer<WakaUserSummaryResponseState> {

        private val kSerializable = WakaUserSummaryResponseState.serializer()

        override val defaultValue: WakaUserSummaryResponseState
            get() = WakaUserSummaryResponseState.IsLoading


        @OptIn(ExperimentalSerializationApi::class)
        override suspend fun readFrom(input: InputStream): WakaUserSummaryResponseState {
            return try {
                input.use { stream ->
                    Json.decodeFromStream(kSerializable, stream)
                }
            } catch (exception: SerializationException) {
                throw CorruptionException("Could not read location data: ${exception.message}")
            } catch (e: Exception) {
                throw e
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        override suspend fun writeTo(t: WakaUserSummaryResponseState, output: OutputStream) {
            output.use { stream ->
                Json.encodeToStream(kSerializable, t, stream)
            }
        }
    }
}