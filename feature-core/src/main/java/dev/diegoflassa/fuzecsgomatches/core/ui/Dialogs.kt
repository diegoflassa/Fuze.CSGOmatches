package dev.diegoflassa.fuzecsgomatches.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import dev.diegoflassa.fuzecsgomatches.core.R
import dev.diegoflassa.fuzecsgomatches.core.extensions.copy
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesColors
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesTheme
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesThemeContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Stack

@Suppress("unused")
object DialogManagerFactory {

    private val dialogManagers = mutableMapOf<String, DialogManager>()
    private val mutex = Mutex()

    /**
     * Retrieves or creates a DialogManager for the specified screen identifier.
     * This function is thread-safe using a Mutex.
     */
    suspend fun getDialogManager(tela: String = "Global"): DialogManager {
        mutex.withLock {
            val existingManager = dialogManagers[tela]
            if (existingManager != null) {
                return existingManager
            }

            val newManager = DialogManager()
            dialogManagers[tela] = newManager
            return newManager
        }
    }

    /**
     * Optional: Clears a specific DialogManager from the factory.
     * This function is thread-safe using a Mutex.
     */
    suspend fun removeDialogManager(tela: String) {
        mutex.withLock {
            dialogManagers.remove(tela)
        }
    }

    /**
     * Optional: Clears all DialogManagers from the factory.
     * This function is thread-safe using a Mutex.
     */
    suspend fun clearAllDialogManagers() {
        mutex.withLock {
            dialogManagers.clear()
        }
    }
}

class DialogManager internal constructor() {
    private val _dialogsParaExibir: MutableStateFlow<Stack<DialogState>> =
        MutableStateFlow(Stack())
    val dialogsParaExibir = _dialogsParaExibir.asStateFlow()

    private val ioScope = CoroutineScope(Dispatchers.Default)

    fun adicionarDialog(dialog: DialogState) {
        val copiaDialogsAtuais = _dialogsParaExibir.value.copy()
        copiaDialogsAtuais.push(dialog)
        emitirAtualizacoes(copiaDialogsAtuais)
    }

    fun removerDialog(): DialogState? {
        if (_dialogsParaExibir.value.isNotEmpty()) {
            val copiaDialogsAtuais = _dialogsParaExibir.value.copy()
            val poppedDialog = copiaDialogsAtuais.pop()
            emitirAtualizacoes(copiaDialogsAtuais)
            return poppedDialog
        }
        return null
    }

    fun dialogAtual(): DialogState? = _dialogsParaExibir.value.lastOrNull()

    private fun emitirAtualizacoes(dialogs: Stack<DialogState>) {
        ioScope.launch {
            delay(500)
            _dialogsParaExibir.emit(dialogs)
        }
    }
}

@Suppress("unused")
open class DialogState(
    val onDismissRequest: (() -> Unit)? = null,
    val confirmButton: @Composable (() -> Unit)? = null,
    val modifier: Modifier? = null,
    val body: @Composable (() -> Unit)? = null,
    val dismissButton: @Composable (() -> Unit)? = null,
    val icon: @Composable (() -> Unit)? = null,
    val title: @Composable (() -> Unit)? = null,
    val text: @Composable (() -> Unit)? = null,
    val shape: Shape? = null,
    val containerColor: Color? = null,
    val iconContentColor: Color? = null,
    val titleContentColor: Color? = null,
    val textContentColor: Color? = null,
    val tonalElevation: Dp? = null,
    val properties: DialogProperties = DialogProperties()
) {

    class Builder {
        private var onDismissRequest: (() -> Unit)? = null
        private var confirmButton: @Composable (() -> Unit)? = null
        private var modifier: Modifier? = null
        private var body: @Composable (() -> Unit)? = null
        private var dismissButton: @Composable (() -> Unit)? = null
        private var icon: @Composable (() -> Unit)? = null
        private var title: @Composable (() -> Unit)? = null
        private var text: @Composable (() -> Unit)? = null
        private var shape: Shape? = null
        private var containerColor: Color? = null
        private var iconContentColor: Color? = null
        private var titleContentColor: Color? = null
        private var textContentColor: Color? = null
        private var tonalElevation: Dp? = null
        private var properties: DialogProperties = DialogProperties()

        fun onDismissRequest(onDismissRequest: () -> Unit): Builder {
            this.onDismissRequest = onDismissRequest
            return this
        }

        fun confirmButton(confirmButton: @Composable () -> Unit): Builder {
            this.confirmButton = confirmButton
            return this
        }

        fun modifier(modifier: Modifier): Builder {
            this.modifier = modifier
            return this
        }

        fun body(body: @Composable () -> Unit): Builder {
            this.body = body
            return this
        }

        fun dismissButton(dismissButton: @Composable () -> Unit): Builder {
            this.dismissButton = dismissButton
            return this
        }

        fun icon(icon: @Composable () -> Unit): Builder {
            this.icon = icon
            return this
        }

        fun title(title: @Composable () -> Unit): Builder {
            this.title = title
            return this
        }

        fun title(title: String): Builder {
            this.title = { TitleDialog(title = title) }
            return this
        }

        fun text(text: @Composable () -> Unit): Builder {
            this.text = text
            return this
        }

        fun text(text: String): Builder {
            this.text = { TextDialog(text = text) }
            return this
        }

        fun shape(shape: Shape): Builder {
            this.shape = shape
            return this
        }

        fun containerColor(containerColor: Color): Builder {
            this.containerColor = containerColor
            return this
        }

        fun iconContentColor(iconContentColor: Color): Builder {
            this.iconContentColor = iconContentColor
            return this
        }

        fun titleContentColor(titleContentColor: Color): Builder {
            this.titleContentColor = titleContentColor
            return this
        }

        fun textContentColor(textContentColor: Color): Builder {
            this.textContentColor = textContentColor
            return this
        }

        fun tonalElevation(tonalElevation: Dp): Builder {
            this.tonalElevation = tonalElevation
            return this
        }

        fun properties(properties: DialogProperties): Builder {
            this.properties = properties
            return this
        }

        fun build(): DialogState = DialogState(
            onDismissRequest = onDismissRequest,
            confirmButton = confirmButton,
            modifier = modifier,
            body = body,
            dismissButton = dismissButton,
            icon = icon,
            title = title,
            text = text,
            shape = shape,
            containerColor = containerColor,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            tonalElevation = tonalElevation,
            properties = properties
        )
    }

    @Composable
    fun ExibirDialog() {
        return AlertDialog(
            onDismissRequest = {
                onDismissRequest?.invoke()
            },
            confirmButton = confirmButton ?: {},
            modifier = modifier ?: Modifier,
            dismissButton = dismissButton ?: {},
            icon = icon,
            title = title,
            text = body ?: text,
            shape = shape ?: AlertDialogDefaults.shape,
            containerColor = containerColor ?: FuzeCSGOMatchesTheme.colorScheme.secondaryContainer,
            iconContentColor = iconContentColor
                ?: FuzeCSGOMatchesTheme.colorScheme.onSecondaryContainer,
            titleContentColor = titleContentColor
                ?: FuzeCSGOMatchesTheme.colorScheme.onSecondaryContainer,
            textContentColor = textContentColor
                ?: FuzeCSGOMatchesTheme.colorScheme.onSecondaryContainer,
            tonalElevation = tonalElevation ?: AlertDialogDefaults.TonalElevation,
            properties = properties
        )
    }
}

fun desejaSairDoAppDialog(
    title: String,
    text: String,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
): DialogState {
    return DialogState.Builder().onDismissRequest(onDismiss)
        .title(title)
        .text(text)
        .confirmButton {
            ButtonDialogSimConfirm(onClick = onConfirm)
        }.dismissButton {
            ButtonDialogNaoDismiss(onClick = onDismiss)
        }.build()
}

val textStyleTitleDialog = TextStyle(
    color = Color.White,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    textAlign = TextAlign.Start,
    letterSpacing = 0.sp
)

val textStyleTextDialog = TextStyle(
    color = Color.White,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    textAlign = TextAlign.Start,
    letterSpacing = 0.sp
)

val textStyleButtonDialog = TextStyle(
    color = Color.White,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 24.sp,
    textAlign = TextAlign.Center,
    letterSpacing = 0.03.sp
)

val colorsButtonDialog = ButtonColors(
    containerColor = FuzeCSGOMatchesColors.primaryContainer,
    contentColor = Color.White,
    disabledContainerColor = FuzeCSGOMatchesColors.primaryContainer,
    disabledContentColor = FuzeCSGOMatchesColors.primaryContainer,
)

@Composable
fun TitleDialog(modifier: Modifier = Modifier, title: String = "Test") {
    Text(modifier = modifier, text = title, style = textStyleTitleDialog)
}

@Composable
fun TextDialog(modifier: Modifier = Modifier, text: String = "Test") {
    Text(
        modifier = modifier,
        text = text,
        style = textStyleTextDialog,
        textAlign = TextAlign.Left,
    )
}

@Composable
fun ButtonDialog(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = Color.White,
    buttonBackground: Color = FuzeCSGOMatchesTheme.colorScheme.primaryContainer,
    onClick: (() -> Unit)? = null
) {
    Button(
        modifier = modifier.wrapContentSize(),
        colors = colorsButtonDialog.copy(
            containerColor = buttonBackground,
            disabledContainerColor = buttonBackground
        ),
        onClick = onClick ?: {}
    ) {
        Text(
            modifier = modifier,
            style = textStyleButtonDialog.copy(color = textColor),
            text = text
        )
    }
}

@Composable
fun ButtonDialogText(
    modifier: Modifier = Modifier,
    text: String = "",
    buttonBackground: Color = FuzeCSGOMatchesTheme.colorScheme.primaryContainer,
    onClick: (() -> Unit)? = null
) {
    ButtonDialog(
        modifier = modifier,
        text = text,
        textColor = FuzeCSGOMatchesTheme.colorScheme.onSecondaryContainer,
        buttonBackground = buttonBackground,
        onClick = onClick
    )
}

@Composable
fun ButtonDialogOkConfirm(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    ButtonDialog(
        modifier = modifier,
        text = stringResource(R.string.ok),
        textColor = FuzeCSGOMatchesTheme.colorScheme.onSecondaryContainer,
        onClick = onClick
    )
}

@Composable
fun ButtonDialogSimConfirm(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    ButtonDialog(
        modifier = modifier,
        text = stringResource(R.string.sim),
        textColor = FuzeCSGOMatchesTheme.colorScheme.onSecondaryContainer,
        onClick = onClick
    )
}

@Composable
fun ButtonDialogNaoDismiss(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    ButtonDialog(
        modifier = modifier,
        text = stringResource(R.string.nao),
        textColor = FuzeCSGOMatchesTheme.colorScheme.onSecondaryContainer,
        onClick = onClick
    )
}

@Composable
fun ButtonDialogCancel(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    ButtonDialog(
        modifier = modifier,
        text = stringResource(R.string.cancel),
        textColor = FuzeCSGOMatchesTheme.colorScheme.onSecondaryContainer,
        onClick = onClick
    )
}

@Composable
fun filterDialog(
    onDismissRequest: () -> Unit = {},
    onFilter: (onlyFutureEvents: Boolean) -> Unit = { _ -> },
    dialogTitle: String = "Filters",
    futureEventsSwitchText: String = stringResource(R.string.search_future_events),
    onlyFutureGames: Boolean = true,
): DialogState {

    var onlyFutureGamesState by remember { mutableStateOf(onlyFutureGames) }

    val dialogBody: @Composable () -> Unit = {
        SettingsBody(
            futureEventsValue = onlyFutureGamesState,
            onFutureEventsChange = { newValue -> onlyFutureGamesState = newValue },
            futureEventsText = futureEventsSwitchText
        )
    }

    return DialogState.Builder()
        .title(dialogTitle)
        .body(dialogBody)
        .onDismissRequest(onDismissRequest)
        .dismissButton {
            ButtonDialogCancel {
                onDismissRequest.invoke()
            }
        }
        .confirmButton {
            ButtonDialogText(text = stringResource(R.string.apply)) {
                onFilter.invoke(onlyFutureGamesState)
                onDismissRequest()
            }
        }
        .build()
}

@Composable
private fun SettingsBody(
    futureEventsValue: Boolean,
    onFutureEventsChange: (Boolean) -> Unit,
    futureEventsText: String
) {
    Column(
        modifier = Modifier
            .padding(
                start = FuzeCSGOMatchesTheme.dimen.mediumLargePadding,
                end = FuzeCSGOMatchesTheme.dimen.mediumLargePadding,
                top = FuzeCSGOMatchesTheme.dimen.mediumPadding,
                bottom = FuzeCSGOMatchesTheme.dimen.smallPadding
            )
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = FuzeCSGOMatchesTheme.dimen.mediumPadding)
        ) {
            Text(
                text = futureEventsText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = futureEventsValue,
                onCheckedChange = onFutureEventsChange
            )
        }

    }
}

@Preview(showBackground = true, name = "Filter Dialog Preview")
@Composable
fun FilterDialogPreview() {
    FuzeCSGOMatchesThemeContent {
        val dialogState = filterDialog(
            onDismissRequest = {},
            onFilter = { _ -> },
            dialogTitle = "Filter Events Preview",
        )
        dialogState.ExibirDialog()
    }
}
