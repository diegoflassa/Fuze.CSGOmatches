@file:Suppress("unused")

package dev.diegoflassa.fusecsgomatches.core.data.adapters

import android.net.Uri
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import androidx.core.net.toUri

class UriJsonAdapter {

    @ToJson
    fun toJson(uri: Uri?): String? {
        return uri?.toString()
    }

    @FromJson
    fun fromJson(uriString: String?): Uri? {
        return when {
            uriString == null -> null
            uriString.isEmpty() -> Uri.EMPTY
            else -> uriString.toUri()
        }
    }
}
