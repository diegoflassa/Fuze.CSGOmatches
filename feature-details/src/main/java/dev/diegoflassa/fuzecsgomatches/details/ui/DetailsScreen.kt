package dev.diegoflassa.fuzecsgomatches.details.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.diegoflassa.fuzecsgomatches.core.navigation.NavigationViewModel
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesColors
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesTheme
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesThemeContent
import dev.diegoflassa.fuzecsgomatches.core.ui.DialogManager
import dev.diegoflassa.fuzecsgomatches.core.ui.DialogManagerFactory
import dev.diegoflassa.fuzecsgomatches.core.ui.DialogState
import dev.diegoflassa.fuzecsgomatches.details.R
import dev.diegoflassa.fuzecsgomatches.details.data.dto.OpponentsResponseDto
import dev.diegoflassa.fuzecsgomatches.details.data.dto.OpponentTeamDetailDto
import dev.diegoflassa.fuzecsgomatches.details.data.dto.PlayerDetailDto
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.zIndex
import dev.diegoflassa.fuzecsgomatches.core.ui.ButtonDialogOkConfirm
import dev.diegoflassa.fuzecsgomatches.core.ui.SystemCircularLoadingIndicator

private const val tag = "DetailsScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigationViewModel: NavigationViewModel = hiltViewModel(),
    matchIdOrSlug: String?,
    leagueName: String?,
    serieFullName: String?,
    scheduledAt: String?,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = matchIdOrSlug) {
        if (!matchIdOrSlug.isNullOrEmpty()) {
            viewModel.reduce(DetailsIntent.LoadDetails(matchIdOrSlug))
        }
    }

    var dialogManager by remember { mutableStateOf<DialogManager?>(null) }
    LaunchedEffect(Unit) {
        dialogManager = DialogManagerFactory.getDialogManager(tag)
    }
    val dialogsParaExibir = dialogManager?.dialogsParaExibir?.collectAsState()

    if (dialogsParaExibir?.value?.isNotEmpty() == true) {
        dialogManager?.dialogAtual()?.ExibirDialog()
    }

    val dialogTitle = stringResource(R.string.alert)
    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DetailsEffect.ShowError -> {
                    Log.e(tag, "Error effect: ${effect.message}")
                    val onOkDismiss: () -> Unit = {
                        dialogManager?.removerDialog()
                    }
                    val dialog =
                        DialogState.Builder().title(dialogTitle)
                            .text(effect.message)
                            .onDismissRequest(onOkDismiss).confirmButton {
                                ButtonDialogOkConfirm {
                                    onOkDismiss.invoke()
                                }
                            }.build()

                    dialogManager?.adicionarDialog(dialog)
                }

                is DetailsEffect.NavigateToMain -> {
                    navigationViewModel.navigateToMain()
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    val titleText = remember(leagueName, serieFullName) {
                        val league = leagueName?.takeIf { it.isNotBlank() } ?: ""
                        val serie = serieFullName?.takeIf { it.isNotBlank() } ?: ""
                        when {
                            league.isNotEmpty() && serie.isNotEmpty() -> "$league - $serie"
                            league.isNotEmpty() -> league
                            serie.isNotEmpty() -> serie
                            else -> ""
                        }
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = titleText,
                        style = FuzeCSGOMatchesTheme.typography.textStyleDetailsScreenTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.reduce(DetailsIntent.NavigateToMain) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = stringResource(id = R.string.back_button_description),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FuzeCSGOMatchesTheme.colorScheme.surface,
                    titleContentColor = FuzeCSGOMatchesTheme.colorScheme.onSurface,
                    navigationIconContentColor = FuzeCSGOMatchesTheme.colorScheme.onSurface
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        DetailsScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            scheduledAt = scheduledAt,
            onIntent = viewModel::reduce
        )
    }
}

@Composable
fun DetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: DetailsUIState,
    scheduledAt: String? = null,
    onIntent: ((DetailsIntent) -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .background(FuzeCSGOMatchesTheme.colorScheme.background)
    ) {
        when {
            uiState.isLoading -> {
                SystemCircularLoadingIndicator()
            }

            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error,
                            color = FuzeCSGOMatchesTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = FuzeCSGOMatchesTheme.dimen.largePadding)
                        )
                        Spacer(modifier = Modifier.height(FuzeCSGOMatchesTheme.dimen.mediumPadding))
                        Button(onClick = {
                            onIntent?.invoke(DetailsIntent.Refresh)
                        }) {
                            Text(text = stringResource(id = R.string.retry))
                        }
                    }
                }
            }

            uiState.opponents == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(id = R.string.no_matches_found),
                        color = FuzeCSGOMatchesTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                OpponentDetailsBody(
                    opponentsResponse = uiState.opponents,
                    scheduledAt = scheduledAt
                )
            }
        }
    }
}

@Composable
fun OpponentDetailsBody(opponentsResponse: OpponentsResponseDto, scheduledAt: String?) {
    if (opponentsResponse.opponents.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                stringResource(id = R.string.no_opponents_message),
                color = FuzeCSGOMatchesTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .padding(top = FuzeCSGOMatchesTheme.dimen.mediumPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val teamAInfo = opponentsResponse.opponents.getOrNull(0)
            ?.let { OpponentInfoForHeader(it.name, it.imageUrl) }
        val teamBInfo = opponentsResponse.opponents.getOrNull(1)
            ?.let { OpponentInfoForHeader(it.name, it.imageUrl) }

        TeamsInfo(
            teamA = teamAInfo,
            teamB = teamBInfo
        )

        Spacer(
            modifier = Modifier
                .height(FuzeCSGOMatchesTheme.dimen.mainMatchDateTeamInfoPadding)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = scheduledAt ?: stringResource(R.string.not_available_short),
                style = FuzeCSGOMatchesTheme.typography.textStyleMainMatchDate
            )
        }

        val teamsToDisplay = opponentsResponse.opponents.take(2)

        if (teamsToDisplay.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = FuzeCSGOMatchesTheme.dimen.largePadding),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    stringResource(id = R.string.no_team_data_to_display),
                    color = FuzeCSGOMatchesTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
            return
        }

        Spacer(
            modifier = Modifier
                .height(FuzeCSGOMatchesTheme.dimen.mainMatchDateTeamInfoPadding)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FuzeCSGOMatchesTheme.dimen.mediumPadding)
        ) {
            if (teamsToDisplay.isNotEmpty()) {
                Box(modifier = Modifier.weight(1f)) {
                    TeamSection(team = teamsToDisplay[0], mirrorPlayers = false)
                }
            }

            if (teamsToDisplay.size > 1) {
                Box(modifier = Modifier.weight(1f)) {
                    TeamSection(team = teamsToDisplay[1], mirrorPlayers = true)
                }
            } else if (teamsToDisplay.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TeamSection(team: OpponentTeamDetailDto, mirrorPlayers: Boolean = false) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidthPx = windowInfo.containerSize.width
    val screenWidthDp = with(density) { screenWidthPx.toDp() }

    val columns = when {
        screenWidthDp >= FuzeCSGOMatchesTheme.dimen.tabletWidth -> GridCells.Fixed(3)
        screenWidthDp >= FuzeCSGOMatchesTheme.dimen.foldableWidth -> GridCells.Fixed(2)
        else -> GridCells.Fixed(1)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FuzeCSGOMatchesTheme.dimen.smallPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (team.players.isNullOrEmpty()) {
            Text(
                text = stringResource(R.string.details_screen_no_players_in_team_message),
                color = FuzeCSGOMatchesTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = FuzeCSGOMatchesTheme.dimen.mediumPadding),
                textAlign = TextAlign.Center
            )
        } else {
            LazyVerticalGrid(
                columns = columns,
                verticalArrangement = Arrangement.spacedBy(FuzeCSGOMatchesTheme.dimen.detailsVerticalCardsPadding),
                horizontalArrangement = Arrangement.spacedBy(FuzeCSGOMatchesTheme.dimen.detailsHorizontalCardsPadding),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = team.players,
                    key = { it.id ?: 0 }
                ) { player ->
                    PlayerCard(player = player, mirrorHorizontally = mirrorPlayers)
                }
            }
        }
    }
}

data class OpponentInfoForHeader(val name: String?, val imageUrl: Uri?)

@Composable
private fun TeamsInfo(
    teamA: OpponentInfoForHeader? = null,
    teamB: OpponentInfoForHeader? = null
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FuzeCSGOMatchesTheme.dimen.largePadding)
    ) {
        val (team1DisplayRef, vsTextRef, team2DisplayRef) = createRefs()

        val team1Name = teamA?.name?.takeIf { it.isNotBlank() }
            ?: stringResource(id = R.string.team_1)
        val team2Name = teamB?.name?.takeIf { it.isNotBlank() }
            ?: stringResource(id = R.string.team_2)

        val team1ImageUrl = teamA?.imageUrl
        val team2ImageUrl = teamB?.imageUrl
        val teamDisplayVsPadding = FuzeCSGOMatchesTheme.dimen.detailsTeamImageVsPadding

        TeamDisplay(
            modifier = Modifier
                .constrainAs(team1DisplayRef) {
                    start.linkTo(parent.start)
                    end.linkTo(vsTextRef.start, margin = teamDisplayVsPadding)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            name = team1Name,
            imageUrl = team1ImageUrl,
        )

        Text(
            modifier = Modifier
                .constrainAs(vsTextRef) {
                    centerHorizontallyTo(parent)
                    top.linkTo(team1DisplayRef.top)
                    bottom.linkTo(team1DisplayRef.bottom)
                },
            text = stringResource(id = R.string.vs),
            style = FuzeCSGOMatchesTheme.typography.textStyleVs
        )

        TeamDisplay(
            modifier = Modifier
                .constrainAs(team2DisplayRef) {
                    start.linkTo(vsTextRef.end, margin = teamDisplayVsPadding)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            name = team2Name,
            imageUrl = team2ImageUrl,
        )
    }
}

@Composable
fun TeamDisplay(
    name: String,
    imageUrl: Uri?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = FuzeCSGOMatchesTheme.dimen.extraSmallPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val context = LocalContext.current
        val brokenImageErrorPainter = rememberVectorPainter(image = Icons.Outlined.BrokenImage)
        val whiteCirclePainter =
            remember { ColorPainter(FuzeCSGOMatchesColors.imageAbsentColor) }
        val actualModel = if (imageUrl == Uri.EMPTY) null else imageUrl

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(actualModel)
                .crossfade(true)
                .build(),
            placeholder = whiteCirclePainter,
            error = brokenImageErrorPainter,
            fallback = whiteCirclePainter,
            contentDescription = stringResource(R.string.team_logo_desc, name),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(FuzeCSGOMatchesTheme.dimen.teamDisplayImageSize)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(FuzeCSGOMatchesTheme.dimen.teamDisplaySpacerHeight))
        Text(
            text = name,
            style = FuzeCSGOMatchesTheme.typography.textStyleMainScreenTeamName,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PlayerCard(
    player: PlayerDetailDto? = null,
    mirrorHorizontally: Boolean = false
) {
    Box(
        modifier = Modifier
            .width(FuzeCSGOMatchesTheme.dimen.detailsPlayerCardWidth)
            .height(FuzeCSGOMatchesTheme.dimen.detailsPlayerCardTotalHeight)
            .background(FuzeCSGOMatchesColors.transparent)
    ) {
        AsyncImage(
            modifier = Modifier
                .align(if (mirrorHorizontally) Alignment.TopStart else Alignment.TopEnd)
                .padding(
                    start = if (mirrorHorizontally) FuzeCSGOMatchesTheme.dimen.detailsPlayerImageHorizontalPadding else FuzeCSGOMatchesTheme.dimen.noPadding,
                    end = if (mirrorHorizontally) FuzeCSGOMatchesTheme.dimen.noPadding else FuzeCSGOMatchesTheme.dimen.detailsPlayerImageHorizontalPadding,
                )
                .size(FuzeCSGOMatchesTheme.dimen.detailsPlayerImageSize)
                .background(
                    FuzeCSGOMatchesTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(size = FuzeCSGOMatchesTheme.dimen.smallPadding)
                )
                .clip(RoundedCornerShape(size = FuzeCSGOMatchesTheme.dimen.smallPadding))
                .zIndex(1f),
            model = ImageRequest.Builder(LocalContext.current)
                .data(player?.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = ColorPainter(FuzeCSGOMatchesColors.imageAbsentColor),
            error = rememberVectorPainter(image = Icons.Outlined.BrokenImage),
            fallback = ColorPainter(FuzeCSGOMatchesColors.imageAbsentColor),
            contentDescription = stringResource(
                R.string.player_image_desc,
                player?.name ?: stringResource(id = R.string.unknown_player_real_name)
            ),
            contentScale = ContentScale.Crop,
        )

        Card(
            modifier = Modifier
                .align(if (mirrorHorizontally) Alignment.BottomEnd else Alignment.BottomStart)
                .height(FuzeCSGOMatchesTheme.dimen.detailsPlayerCardHeight)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = FuzeCSGOMatchesTheme.colorScheme.surfaceContainerHighest),
            shape = if (mirrorHorizontally) {
                RoundedCornerShape(
                    topStart = FuzeCSGOMatchesTheme.dimen.mediumPadding,
                    bottomStart = FuzeCSGOMatchesTheme.dimen.mediumPadding,
                    topEnd = FuzeCSGOMatchesTheme.dimen.noPadding,
                    bottomEnd = FuzeCSGOMatchesTheme.dimen.noPadding
                )
            } else {
                RoundedCornerShape(
                    topStart = FuzeCSGOMatchesTheme.dimen.noPadding,
                    bottomStart = FuzeCSGOMatchesTheme.dimen.noPadding,
                    topEnd = FuzeCSGOMatchesTheme.dimen.mediumPadding,
                    bottomEnd = FuzeCSGOMatchesTheme.dimen.mediumPadding
                )
            }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (mirrorHorizontally) Arrangement.Start else Arrangement.End
            ) {
                if (mirrorHorizontally) {
                    Spacer(
                        modifier = Modifier
                            .width(
                                FuzeCSGOMatchesTheme.dimen.detailsPlayerImageSize +
                                        FuzeCSGOMatchesTheme.dimen.detailsPlayerImageHorizontalPadding +
                                        FuzeCSGOMatchesTheme.dimen.mediumPadding
                            )
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = if (mirrorHorizontally) FuzeCSGOMatchesTheme.dimen.noPadding else FuzeCSGOMatchesTheme.dimen.smallPadding,
                            end = if (mirrorHorizontally) FuzeCSGOMatchesTheme.dimen.smallPadding else FuzeCSGOMatchesTheme.dimen.noPadding,
                            top = FuzeCSGOMatchesTheme.dimen.detailsNicknameTopPadding,
                            bottom = FuzeCSGOMatchesTheme.dimen.smallPadding
                        ),
                    horizontalAlignment = if (mirrorHorizontally) Alignment.Start else Alignment.End
                ) {
                    Text(
                        text = player?.name
                            ?: stringResource(id = R.string.unknown_player_real_name),
                        style = FuzeCSGOMatchesTheme.typography.textStyleDetailsCardNickname,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(
                        modifier = Modifier
                            .height(FuzeCSGOMatchesTheme.dimen.detailsPlayerCardSpacerHeight)
                            .fillMaxWidth()
                    )
                    val realName = ("${player?.firstName ?: ""} ${player?.lastName ?: ""}").trim()
                    Text(
                        text = realName,
                        style = FuzeCSGOMatchesTheme.typography.textStyleDetailsCardName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!mirrorHorizontally) {
                    Spacer(
                        modifier = Modifier
                            .width(
                                FuzeCSGOMatchesTheme.dimen.detailsPlayerImageSize +
                                        FuzeCSGOMatchesTheme.dimen.detailsPlayerImageHorizontalPadding +
                                        FuzeCSGOMatchesTheme.dimen.mediumPadding
                            )
                    )
                }
            }
        }
    }
}

data class ColorPainter(val color: Color) : Painter() {
    override val intrinsicSize: Size get() = Size.Unspecified
    override fun DrawScope.onDraw() {
        drawRect(color = color)
    }
}

private val samplePlayer1 = PlayerDetailDto(
    id = 1,
    name = "Player One",
    firstName = "One",
    lastName = "Player",
    imageUrl = "https://cdn.pandascore.co/images/player/image/1/player_one.png".toUri()
)
private val samplePlayer2 = PlayerDetailDto(
    id = 2,
    name = "Player Two",
    firstName = "Two",
    lastName = "Player",
    imageUrl = "https://cdn.pandascore.co/images/player/image/2/player_two.png".toUri()
)
private val samplePlayer3 = PlayerDetailDto(
    id = 3,
    name = "Player Three",
    firstName = "Three",
    lastName = "Player",
    imageUrl = "https://cdn.pandascore.co/images/player/image/3/player_three.png".toUri()
)

private val sampleTeam1Players = listOf(samplePlayer1, samplePlayer2, samplePlayer3)
private val sampleTeam2Players = listOf(
    PlayerDetailDto(
        id = 4,
        name = "Player Alpha",
        firstName = "Alpha",
        lastName = "Player",
        imageUrl = "https://cdn.pandascore.co/images/player/image/4/player_alpha.png".toUri()
    ),
    PlayerDetailDto(
        id = 5,
        name = "Player Beta",
        firstName = "Beta",
        lastName = "Player",
        imageUrl = "https://cdn.pandascore.co/images/player/image/5/player_beta.png".toUri()
    )
)

private val sampleTeam1 = OpponentTeamDetailDto(
    name = "Crimson Dragons",
    imageUrl = "https://cdn.pandascore.co/images/team/image/101/crimson_dragons.png".toUri(),
    players = sampleTeam1Players,
)
private val sampleTeam2 = OpponentTeamDetailDto(
    name = "Azure Knights withe e very long name",
    imageUrl = "https://cdn.pandascore.co/images/team/image/102/azure_knights.png".toUri(),
    players = sampleTeam2Players,
)
private val sampleTeam3 = OpponentTeamDetailDto(
    name = "Green Serpents",
    imageUrl = "https://cdn.pandascore.co/images/team/image/103/green_serpents.png".toUri(),
    players = listOf(samplePlayer1),
)


private val sampleOpponentsResponse = OpponentsResponseDto(
    opponents = listOf(sampleTeam1, sampleTeam2)
)

private val sampleOpponentsResponseThreeTeams = OpponentsResponseDto(
    opponents = listOf(sampleTeam1, sampleTeam2, sampleTeam3)
)

private val sampleOpponentsResponseTwelveTeams = OpponentsResponseDto(
    opponents = listOf(
        sampleTeam1,
        sampleTeam2,
        sampleTeam3,
        sampleTeam1,
        sampleTeam2,
        sampleTeam3,
        sampleTeam1,
        sampleTeam2,
        sampleTeam3,
        sampleTeam1,
        sampleTeam2,
        sampleTeam3
    )
)

@Preview(showBackground = true, name = "Details Screen Content - Loaded")
@Composable
fun DetailsScreenContentPreview_Loaded() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                opponents = sampleOpponentsResponse,
                error = null
            )
        )
    }
}

@Preview(showBackground = true, name = "Details Screen Content - Three Teams")
@Composable
fun DetailsScreenContentPreview_ThreeTeams() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                opponents = sampleOpponentsResponseThreeTeams,
                error = null
            )
        )
    }
}

@Preview(showBackground = true, name = "Details Screen Content - Loading")
@Composable
fun DetailsScreenContentPreview_Loading() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(isLoading = true, opponents = null, error = null),
        )
    }
}

@Preview(showBackground = true, name = "Details Screen Content - Error")
@Composable
fun DetailsScreenContentPreview_Error() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                opponents = null,
                error = "Failed to load match details. Please try again."
            )
        )
    }
}

@Preview(showBackground = true, name = "Details Screen Content - No Opponents Dto")
@Composable
fun DetailsScreenContentPreview_NoOpponentsDto() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                opponents = null,
                error = null
            )
        )
    }
}

@Preview(showBackground = true, name = "Details Screen Content - Empty Opponents List")
@Composable
fun DetailsScreenContentPreview_EmptyOpponentsList() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                opponents = OpponentsResponseDto(
                    opponents = emptyList()
                ), error = null
            )
        )
    }
}

@Preview(showBackground = true, name = "Team Section Preview")
@Composable
fun TeamSectionPreview() {
    FuzeCSGOMatchesThemeContent {
        TeamSection(team = sampleTeam1, false)
    }
}

@Preview(showBackground = true, name = "TeamsInfo Preview")
@Composable
fun TeamsInfoPreview() {
    FuzeCSGOMatchesThemeContent {
        TeamsInfo(
            teamA = OpponentInfoForHeader(sampleTeam1.name, sampleTeam1.imageUrl),
            teamB = OpponentInfoForHeader(sampleTeam2.name, sampleTeam2.imageUrl)
        )
    }
}

@Preview(showBackground = true, name = "TeamDisplay Preview")
@Composable
fun TeamDisplayPreview() {
    FuzeCSGOMatchesThemeContent {
        TeamDisplay(name = "Team Awesome", imageUrl = sampleTeam1.imageUrl)
    }
}

@Preview(showBackground = true, name = "PlayerCard Preview - Normal")
@Composable
fun PlayerCardPreviewNormal() {
    FuzeCSGOMatchesThemeContent {
        PlayerCard(player = samplePlayer1, mirrorHorizontally = false)
    }
}

@Preview(showBackground = true, name = "PlayerCard Preview - Mirrored")
@Composable
fun PlayerCardPreviewMirrored() {
    FuzeCSGOMatchesThemeContent {
        PlayerCard(player = samplePlayer1, mirrorHorizontally = true)
    }
}

@Preview(showBackground = true, name = "PlayerCard Preview - Long Name Mirrored")
@Composable
fun PlayerCardPreviewLongNameMirrored() {
    FuzeCSGOMatchesThemeContent {
        PlayerCard(
            player = samplePlayer1.copy(name = "A_Player_With_A_Very_Long_Nickname_Indeed"),
            mirrorHorizontally = false
        )
    }
}

@Preview(showBackground = true, name = "PlayerCard Preview - Long Real Name Mirrored")
@Composable
fun PlayerCardPreviewLongRealNameMirrored() {
    FuzeCSGOMatchesThemeContent {
        PlayerCard(
            player = samplePlayer1.copy(
                firstName = "VeryLongFirstName",
                lastName = "EvenLongerLastName"
            ),
            mirrorHorizontally = true
        )
    }
}

@Preview(
    name = "MainScreenContent Phone - Data",
    showBackground = true,
    device = Devices.PHONE
)
@Composable
private fun MainScreenContentPreviewPhoneWithData() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                opponents = sampleOpponentsResponseTwelveTeams,
                error = null
            )
        )
    }
}

@Preview(
    name = "MainScreenContent Foldable - Data",
    showBackground = true,
    device = Devices.FOLDABLE
)
@Composable
private fun MainScreenContentPreviewFoldableWithData() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                opponents = sampleOpponentsResponseTwelveTeams,
                error = null
            )
        )
    }
}

@Preview(
    name = "MainScreenContent Tablet - Data",
    showBackground = true,
    device = Devices.TABLET
)
@Composable
private fun MainScreenContentPreviewTabletWithData() {
    FuzeCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                opponents = sampleOpponentsResponseTwelveTeams,
                error = null
            )
        )
    }
}