package dev.diegoflassa.fusecsgomatches.core.data.config

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.diegoflassa.fusecsgomatches.core.R
import javax.inject.Inject

class Config @Inject constructor(@param:ApplicationContext val context: Context) : IConfig {
    override val pandascoreKey by lazy {
        context.getString(R.string.pandascore_key)
    }
    override val pandascoreApi by lazy {
        context.getString(R.string.pandascore_api)
    }
}
