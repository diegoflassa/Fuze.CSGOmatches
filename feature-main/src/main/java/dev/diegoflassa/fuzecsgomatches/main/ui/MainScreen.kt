package dev.diegoflassa.fuzecsgomatches.main.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.diegoflassa.fuzecsgomatches.core.data.enums.MatchStatus
import dev.diegoflassa.fuzecsgomatches.core.extensions.hiltActivityViewModel
import dev.diegoflassa.fuzecsgomatches.core.navigation.NavigationViewModel
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesColors
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesTheme
import dev.diegoflassa.fuzecsgomatches.core.theme.FuzeCSGOMatchesThemeContent
import dev.diegoflassa.fuzecsgomatches.core.ui.ButtonDialogOkConfirm
import dev.diegoflassa.fuzecsgomatches.core.ui.ButtonDialogText
import dev.diegoflassa.fuzecsgomatches.core.ui.DialogManager
import dev.diegoflassa.fuzecsgomatches.core.ui.DialogManagerFactory
import dev.diegoflassa.fuzecsgomatches.core.ui.DialogState
import dev.diegoflassa.fuzecsgomatches.core.ui.SystemCircularLoadingIndicator
import dev.diegoflassa.fuzecsgomatches.core.ui.filterDialog
import dev.diegoflassa.fuzecsgomatches.main.R
import dev.diegoflassa.fuzecsgomatches.main.data.dto.LeagueDto
import dev.diegoflassa.fuzecsgomatches.main.data.dto.LiveDto
import dev.diegoflassa.fuzecsgomatches.main.data.dto.MatchDto
import dev.diegoflassa.fuzecsgomatches.main.data.dto.OpponentDetailsDto
import dev.diegoflassa.fuzecsgomatches.main.data.dto.OpponentWrapperDto
import dev.diegoflassa.fuzecsgomatches.main.data.dto.SerieDto
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.abs

private const val tag = "MainScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigationViewModel: NavigationViewModel = hiltActivityViewModel(),
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val matchesLazyItems: LazyPagingItems<MatchDto> = uiState.matchesFlow.collectAsLazyPagingItems()

    var dialogManager by remember { mutableStateOf<DialogManager?>(null) }
    LaunchedEffect(Unit) {
        viewModel.reduce(MainIntent.LoadMatches)
        dialogManager = DialogManagerFactory.getDialogManager(tag)
    }
    val dialogsParaExibir = dialogManager?.dialogsParaExibir?.collectAsState()

    if (dialogsParaExibir?.value?.isNotEmpty() == true) {
        dialogManager?.dialogAtual()?.ExibirDialog()
    }

    val dialogTitle = stringResource(R.string.alert)
    val onDismiss: () -> Unit = {
        dialogManager?.removerDialog()
    }
    val onFilter: (onlyFutureEvents: Boolean) -> Unit =
        { onlyFutureEvents ->
            dialogManager?.removerDialog()
            viewModel.reduce(MainIntent.ApplyFilter(onlyFutureEvents))
        }
    val filterDialog = filterDialog(
        onDismissRequest = onDismiss,
        onFilter = onFilter,
        onlyFutureGames = uiState.onlyFutureGames,
    )
    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is MainEffect.ShowError -> {
                    Log.e(tag, "Error effect: ${effect.message}")
                    val onOkDismiss: () -> Unit = {
                        dialogManager?.removerDialog()
                    }
                    val dialog = DialogState.Builder().title(dialogTitle).text(effect.message)
                        .onDismissRequest(onOkDismiss).confirmButton {
                            ButtonDialogOkConfirm {
                                onOkDismiss.invoke()
                            }
                        }.build()

                    dialogManager?.adicionarDialog(dialog)
                }

                is MainEffect.ShowFilter -> {
                    dialogManager?.adicionarDialog(filterDialog)

                }

                is MainEffect.NavigateToDetails -> {
                    navigationViewModel.navigateToDetails(
                        matchIdOrSlug = effect.matchIdOrSlug,
                        leagueName = effect.leagueName,
                        serieFullName = effect.serieFullName,
                        scheduledAt = effect.scheduledAt
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(
                    top = FuzeCSGOMatchesTheme.dimen.mainTopAppContentTopPadding,
                    start = FuzeCSGOMatchesTheme.dimen.smallPadding,
                    end = FuzeCSGOMatchesTheme.dimen.mainTopAppContentEndPadding
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(FuzeCSGOMatchesTheme.dimen.mainTopAppBarHeight),
                            text = stringResource(id = R.string.matches_title),
                            style = FuzeCSGOMatchesTheme.typography.textStyleMainScreenTitle,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.reduce(MainIntent.ShowFilter)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = stringResource(id = R.string.apply_filter_description),
                            tint = FuzeCSGOMatchesTheme.colorScheme.onBackground
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
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.reduce(MainIntent.LoadMatches) },
            indicator = {
                Column(
                    modifier = Modifier
                        .zIndex(1f)
                        .align(Alignment.TopCenter)
                        .background(Color.Blue)
                        .wrapContentWidth()
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(FuzeCSGOMatchesTheme.dimen.mainRefreshIndicatorTopPadding)
                    )
                    Indicator(
                        state = pullToRefreshState,
                        isRefreshing = uiState.isLoading
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val refreshLoadState = matchesLazyItems.loadState.refresh) {
                    is LoadState.Loading -> {
                        SystemCircularLoadingIndicator()
                    }

                    is LoadState.Error -> {
                        val error = refreshLoadState.error
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(FuzeCSGOMatchesTheme.dimen.mediumPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ErrorOutline,
                                    contentDescription = null,
                                    tint = FuzeCSGOMatchesTheme.colorScheme.onError,
                                    modifier = Modifier.size(FuzeCSGOMatchesTheme.dimen.bigLargePadding)
                                )

                                Spacer(modifier = Modifier.height(FuzeCSGOMatchesTheme.dimen.smallPadding))
                                Text(
                                    text = error.localizedMessage
                                        ?: stringResource(id = R.string.error_loading_matches),
                                    color = FuzeCSGOMatchesTheme.colorScheme.onError,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(FuzeCSGOMatchesTheme.dimen.mediumPadding)
                                        .fillMaxSize()
                                )
                                Text(
                                    stringResource(id = R.string.please_try_to_refresh_the_page),
                                    color = FuzeCSGOMatchesTheme.colorScheme.onBackground,
                                    style = FuzeCSGOMatchesTheme.typography.textStyleNoMatchesFound,
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(FuzeCSGOMatchesTheme.dimen.mediumPadding)
                                        .fillMaxSize()
                                )
                                ButtonDialogText(
                                    text = stringResource(id = R.string.retry),
                                    buttonBackground = FuzeCSGOMatchesTheme.colorScheme.secondaryContainer
                                ) {
                                    viewModel.reduce(MainIntent.LoadMatches)
                                }
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (matchesLazyItems.itemCount == 0 && matchesLazyItems.loadState.append.endOfPaginationReached) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(FuzeCSGOMatchesTheme.dimen.mediumPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    stringResource(id = R.string.no_matches_found),
                                    color = FuzeCSGOMatchesTheme.colorScheme.onBackground,
                                    style = FuzeCSGOMatchesTheme.typography.textStyleNoMatchesFound,
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(FuzeCSGOMatchesTheme.dimen.mediumPadding)
                                        .fillMaxSize()
                                )
                                Text(
                                    stringResource(id = R.string.please_try_to_refresh_the_page),
                                    color = FuzeCSGOMatchesTheme.colorScheme.onBackground,
                                    style = FuzeCSGOMatchesTheme.typography.textStyleNoMatchesFound,
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(FuzeCSGOMatchesTheme.dimen.mediumPadding)
                                        .fillMaxSize()
                                )
                                Text(
                                    stringResource(id = R.string.or_try_disable_the_filter),
                                    color = FuzeCSGOMatchesTheme.colorScheme.onBackground,
                                    style = FuzeCSGOMatchesTheme.typography.textStyleNoMatchesFound,
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(FuzeCSGOMatchesTheme.dimen.mediumPadding)
                                        .fillMaxSize()
                                )
                                ButtonDialogText(
                                    text = stringResource(id = R.string.retry),
                                    buttonBackground = FuzeCSGOMatchesTheme.colorScheme.secondaryContainer
                                ) {
                                    viewModel.reduce(MainIntent.LoadMatches)
                                }
                            }
                        } else {
                            MainScreenContent(
                                matches = matchesLazyItems,
                                onIntent = viewModel::reduce
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    matches: LazyPagingItems<MatchDto>,
    onIntent: ((MainIntent) -> Unit)? = null,
) {
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
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = FuzeCSGOMatchesTheme.dimen.mediumLargePadding,
                start = FuzeCSGOMatchesTheme.dimen.mediumLargePadding,
                end = FuzeCSGOMatchesTheme.dimen.mediumLargePadding,
                bottom = FuzeCSGOMatchesTheme.dimen.noPadding
            )
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = columns,
            contentPadding = PaddingValues(bottom = FuzeCSGOMatchesTheme.dimen.mediumPadding),
            verticalArrangement = Arrangement.spacedBy(FuzeCSGOMatchesTheme.dimen.mediumLargePadding),
            horizontalArrangement = Arrangement.spacedBy(FuzeCSGOMatchesTheme.dimen.mediumLargePadding)
        ) {
            items(
                count = matches.itemCount, key = matches.itemKey { it.id ?: 0 }) { index ->
                val match = matches[index]
                if (match != null) {
                    MatchCard(match = match, onIntent = onIntent)
                }
            }

            item {
                when (matches.loadState.append) {
                    is LoadState.Loading -> {
                        SystemCircularLoadingIndicator()
                    }

                    is LoadState.Error -> {
                        val error = (matches.loadState.append as LoadState.Error).error
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(FuzeCSGOMatchesTheme.dimen.mediumPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.ErrorOutline,
                                    contentDescription = null,
                                    tint = FuzeCSGOMatchesTheme.colorScheme.onError,
                                    modifier = Modifier.size(FuzeCSGOMatchesTheme.dimen.bigLargePadding)
                                )
                                Spacer(modifier = Modifier.height(FuzeCSGOMatchesTheme.dimen.smallPadding))
                                Text(
                                    text = error.localizedMessage
                                        ?: stringResource(id = R.string.error_loading_matches),
                                    color = FuzeCSGOMatchesTheme.colorScheme.onError,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(FuzeCSGOMatchesTheme.dimen.mediumPadding))
                                Button(
                                    onClick = { matches.retry() },
                                    colors = ButtonDefaults.buttonColors(containerColor = FuzeCSGOMatchesTheme.colorScheme.error)
                                ) {
                                    Text(stringResource(id = R.string.retry))
                                }
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (matches.loadState.append.endOfPaginationReached && matches.itemCount > 0) {
                            Log.i(tag, "Stopped loading")
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun MainScreenContentPreview(
    modifier: Modifier = Modifier,
    matches: List<MatchDto>,
    onIntent: ((MainIntent) -> Unit)? = null,
) {
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
        modifier = modifier
            .fillMaxSize()
            .background(FuzeCSGOMatchesTheme.colorScheme.background)
            .padding(
                top = FuzeCSGOMatchesTheme.dimen.mediumLargePadding,
                start = FuzeCSGOMatchesTheme.dimen.mediumLargePadding,
                end = FuzeCSGOMatchesTheme.dimen.mediumLargePadding,
                bottom = FuzeCSGOMatchesTheme.dimen.noPadding
            )
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = columns,
            contentPadding = PaddingValues(bottom = FuzeCSGOMatchesTheme.dimen.mediumPadding),
            verticalArrangement = Arrangement.spacedBy(FuzeCSGOMatchesTheme.dimen.mediumLargePadding),
            horizontalArrangement = Arrangement.spacedBy(FuzeCSGOMatchesTheme.dimen.mediumLargePadding)
        ) {
            items(items = matches, key = { it.id ?: 0 }) { match ->
                MatchCard(match = match, onIntent = onIntent)
            }
        }
    }
}

@Composable
fun MatchCard(match: MatchDto, onIntent: ((MainIntent) -> Unit)? = null) {
    val scheduledDate = formatScheduledAt(
        match.scheduledAt
    )
    Card(
        modifier = Modifier
            .width(FuzeCSGOMatchesTheme.dimen.mainCardWidth)
            .sizeIn(maxWidth = FuzeCSGOMatchesTheme.dimen.mainCardWidth)
            .aspectRatio(16f / 9f)
            .clickable(enabled = onIntent != null) {
                onIntent?.invoke(
                    MainIntent.OnMatchClicked(
                        matchIdOrSlug = match.slug ?: match.id?.toString() ?: "",
                        leagueName = match.league?.name,
                        serieFullName = match.serie?.fullName ?: match.serie?.name,
                        scheduledAt = scheduledDate
                    )
                )
            },
        colors = CardDefaults.cardColors()
            .copy(containerColor = FuzeCSGOMatchesTheme.colorScheme.surfaceContainerHighest),
        shape = RoundedCornerShape(FuzeCSGOMatchesTheme.dimen.mediumPadding),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topRef, mainContentRef, bottomRowRef) = createRefs()

            val badgeModifier = Modifier.constrainAs(topRef) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }

            when (match.status) {
                MatchStatus.IN_PROGRESS -> {
                    if (match.live?.supported == true) {
                        StatusBadge(
                            text = stringResource(R.string.now),
                            backgroundColor = FuzeCSGOMatchesTheme.colorScheme.errorContainer,
                            textStyle = FuzeCSGOMatchesTheme.typography.textStyleNow,
                            modifier = badgeModifier
                        )
                    } else {
                        Spacer(modifier = badgeModifier.height(FuzeCSGOMatchesTheme.dimen.mainBadgeHeight))
                    }
                }

                MatchStatus.SCHEDULED,
                MatchStatus.NOT_STARTED -> {
                    StatusBadge(
                        text = scheduledDate,
                        backgroundColor = FuzeCSGOMatchesColors.containerColorScheduledBadge,
                        textStyle = FuzeCSGOMatchesTheme.typography.textStyleNow,
                        modifier = badgeModifier
                    )
                }

                MatchStatus.FINISHED,
                MatchStatus.ENDED -> {
                    StatusBadge(
                        text = stringResource(R.string.ended),
                        backgroundColor = FuzeCSGOMatchesColors.containerColorEndedBadge,
                        textStyle = FuzeCSGOMatchesTheme.typography.textStyleEnded,
                        modifier = badgeModifier
                    )
                }

                MatchStatus.CANCELED -> {
                    StatusBadge(
                        text = stringResource(R.string.canceled),
                        backgroundColor = FuzeCSGOMatchesTheme.colorScheme.onSurfaceVariant,
                        textStyle = FuzeCSGOMatchesTheme.typography.textStyleCancelled,
                        modifier = badgeModifier
                    )
                }

                MatchStatus.UNKNOWN,
                null -> {
                    Spacer(modifier = badgeModifier.height(FuzeCSGOMatchesTheme.dimen.mainBadgeHeight))
                }
            }

            val noPadding = FuzeCSGOMatchesTheme.dimen.noPadding
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(FuzeCSGOMatchesTheme.dimen.mainCardCentralBodyHeight)
                    .padding(vertical = FuzeCSGOMatchesTheme.dimen.smallMediumPadding)
                    .constrainAs(mainContentRef) {
                        top.linkTo(
                            topRef.bottom, margin = noPadding
                        )
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(bottomRowRef.top)
                    }
                    .padding(horizontal = FuzeCSGOMatchesTheme.dimen.largePadding)) {
                val (team1DisplayRef, vsTextRef, team2DisplayRef) = createRefs()

                val team1 = match.opponents?.getOrNull(0)?.opponent
                val team2 = match.opponents?.getOrNull(1)?.opponent

                val team1Name =
                    team1?.name?.takeIf { it.isNotBlank() } ?: match.name?.substringBefore(" vs ")
                        ?.trim() ?: stringResource(id = R.string.team_1)
                val team2Name =
                    team2?.name?.takeIf { it.isNotBlank() } ?: match.name?.substringAfter(
                        " vs ", missingDelimiterValue = stringResource(id = R.string.team_2)
                    )?.trim() ?: stringResource(id = R.string.team_2)

                val team1ImageUrl = team1?.imageUrl
                val team2ImageUrl = team2?.imageUrl

                val teamImageVsPadding = FuzeCSGOMatchesTheme.dimen.detailsTeamImageVsPadding
                TeamDisplay(
                    modifier = Modifier.constrainAs(team1DisplayRef) {
                        end.linkTo(
                            vsTextRef.start, margin = teamImageVsPadding
                        )
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        width = Dimension.fillToConstraints
                    },
                    name = team1Name,
                    imageUrl = team1ImageUrl,
                )
                TeamDisplay(
                    modifier = Modifier.constrainAs(team2DisplayRef) {
                        start.linkTo(vsTextRef.end, margin = teamImageVsPadding)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                    name = team2Name,
                    imageUrl = team2ImageUrl,
                )

                Text(
                    modifier = Modifier.constrainAs(vsTextRef) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(team1DisplayRef)
                    },
                    text = stringResource(id = R.string.vs),
                    style = FuzeCSGOMatchesTheme.typography.textStyleVs
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(bottomRowRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FuzeCSGOMatchesTheme.dimen.mainCardBottomBarHeight)
                        .background(FuzeCSGOMatchesColors.containerColorScheduledBadge)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FuzeCSGOMatchesTheme.dimen.largePadding)
                        .padding(
                            start = FuzeCSGOMatchesTheme.dimen.mediumPadding,
                            top = FuzeCSGOMatchesTheme.dimen.smallPadding,
                            end = FuzeCSGOMatchesTheme.dimen.mediumPadding,
                            bottom = FuzeCSGOMatchesTheme.dimen.smallPadding
                        ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val leagueAndSeries = stringResource(
                        R.string.league_and_series,
                        match.league?.name ?: "",
                        match.serie?.fullName ?: match.serie?.name ?: ""
                    )
                    LeagueImage(match.league?.imageUrl, leagueAndSeries)
                    Spacer(
                        modifier = Modifier
                            .width(FuzeCSGOMatchesTheme.dimen.smallPadding)
                            .fillMaxHeight()
                    )
                    Text(
                        text = leagueAndSeries,
                        style = FuzeCSGOMatchesTheme.typography.textStyleLeagueAndSeries,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun formatScheduledAt(
    scheduledAtInstant: Instant?, locale: Locale = Locale.getDefault()
): String {
    if (scheduledAtInstant == null) {
        return stringResource(id = dev.diegoflassa.fuzecsgomatches.core.R.string.not_available_short)
    }

    val systemZoneId = ZoneId.systemDefault()
    val now = Instant.now()

    val scheduledZonedDateTime = ZonedDateTime.ofInstant(scheduledAtInstant, systemZoneId)
    val nowZonedDateTime = ZonedDateTime.ofInstant(now, systemZoneId)

    val scheduledDate = scheduledZonedDateTime.toLocalDate()
    val nowDate = nowZonedDateTime.toLocalDate()

    val timeFormatter = remember(locale) { DateTimeFormatter.ofPattern("HH:mm", locale) }
    val timeString = scheduledZonedDateTime.format(timeFormatter)

    return when {
        scheduledDate.isEqual(nowDate) -> {
            stringResource(
                dev.diegoflassa.fuzecsgomatches.core.R.string.scheduled_today, timeString
            )
        }

        isSameWeek(scheduledDate, nowDate, locale) -> {
            var dayOfWeekString = scheduledZonedDateTime.dayOfWeek.getDisplayName(
                java.time.format.TextStyle.SHORT, locale
            ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
            if (dayOfWeekString.endsWith(".")) {
                dayOfWeekString = dayOfWeekString.dropLast(1)
            }
            "$dayOfWeekString, $timeString"
        }

        else -> {
            val dateFormatter = remember(locale) { DateTimeFormatter.ofPattern("dd.MM", locale) }
            "${scheduledDate.format(dateFormatter)} $timeString"
        }
    }
}


private fun isSameWeek(startDate: LocalDate, endDate: LocalDate, locale: Locale): Boolean {
    if (startDate.year != endDate.year) {
        return false
    }
    val weekFields = WeekFields.of(locale)
    return startDate.get(weekFields.weekOfYear()) == endDate.get(weekFields.weekOfYear()) && abs(
        ChronoUnit.DAYS.between(startDate, endDate)
    ) < 7
}

@Composable
fun TeamDisplay(
    name: String, imageUrl: Uri?, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = FuzeCSGOMatchesTheme.dimen.extraSmallPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val context = LocalContext.current

        val brokenImageErrorPainter = rememberVectorPainter(image = Icons.Outlined.BrokenImage)

        val whiteCirclePainter = remember { ColorPainter(FuzeCSGOMatchesColors.imageAbsentColor) }

        val actualModel = if (imageUrl == Uri.EMPTY) null else imageUrl

        AsyncImage(
            model = ImageRequest.Builder(context).data(actualModel).crossfade(true).build(),
            placeholder = whiteCirclePainter,
            error = brokenImageErrorPainter,
            fallback = whiteCirclePainter,
            contentDescription = stringResource(R.string.team_logo_desc, name),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(FuzeCSGOMatchesTheme.dimen.teamDisplayImageSize)
                .clip(CircleShape)
        )
        Spacer(
            modifier = Modifier
                .height(FuzeCSGOMatchesTheme.dimen.teamDisplaySpacerHeight)
                .fillMaxWidth()
        )
        Text(
            text = name,
            style = FuzeCSGOMatchesTheme.typography.textStyleMainScreenTeamName,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LeagueImage(imageUrl: Uri?, name: String) {
    val context = LocalContext.current

    val brokenImageErrorPainter = rememberVectorPainter(image = Icons.Outlined.BrokenImage)

    val whiteCirclePainter = remember { ColorPainter(FuzeCSGOMatchesColors.imageAbsentColor) }

    val actualModel = if (imageUrl == Uri.EMPTY) null else imageUrl

    AsyncImage(
        modifier = Modifier
            .size(FuzeCSGOMatchesTheme.dimen.mediumPadding)
            .clip(CircleShape),
        model = ImageRequest.Builder(context).data(actualModel).crossfade(true).build(),
        placeholder = whiteCirclePainter,
        error = brokenImageErrorPainter,
        fallback = whiteCirclePainter,
        contentDescription = stringResource(R.string.team_logo_desc, name),
        contentScale = ContentScale.Fit,
    )
}

@Composable
fun StatusBadge(
    text: String,
    backgroundColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .sizeIn(minWidth = FuzeCSGOMatchesTheme.dimen.mainBadgeMinWidth)
            .height(FuzeCSGOMatchesTheme.dimen.mainBadgeHeight)
            .wrapContentWidth()
            .clip(FuzeCSGOMatchesTheme.shapes.agora)
            .background(backgroundColor)
            .padding(
                horizontal = FuzeCSGOMatchesTheme.dimen.smallMediumPadding,
                vertical = FuzeCSGOMatchesTheme.dimen.mainBadgeVerticalPadding
            )
    ) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = FuzeCSGOMatchesTheme.dimen.mainBadgeTextTopPadding),
            text = text,
            style = textStyle,
        )
    }
}

@Preview
@Composable
fun NowBadgePreview() {
    FuzeCSGOMatchesThemeContent {
        Surface(modifier = Modifier.padding(FuzeCSGOMatchesTheme.dimen.smallPadding)) {
            StatusBadge(
                text = stringResource(R.string.now),
                backgroundColor = FuzeCSGOMatchesTheme.colorScheme.errorContainer,
                textStyle = FuzeCSGOMatchesTheme.typography.textStyleNow
            )
        }
    }
}

@Preview
@Composable
fun ScheduledNotStartedBadgePreview() {
    FuzeCSGOMatchesThemeContent {
        Surface(modifier = Modifier.padding(FuzeCSGOMatchesTheme.dimen.smallPadding)) {
            val formattedText = formatScheduledAt(Instant.now())
            StatusBadge(
                text = formattedText,
                backgroundColor = FuzeCSGOMatchesColors.containerColorScheduledBadge,
                textStyle = FuzeCSGOMatchesTheme.typography.textStyleNow
            )
        }
    }
}

@Preview
@Composable
fun FinishedEndedBadgePreview() {
    FuzeCSGOMatchesThemeContent {
        Surface(modifier = Modifier.padding(FuzeCSGOMatchesTheme.dimen.smallPadding)) {
            StatusBadge(
                text = stringResource(R.string.ended),
                backgroundColor = FuzeCSGOMatchesColors.containerColorEndedBadge,
                textStyle = FuzeCSGOMatchesTheme.typography.textStyleEnded
            )
        }
    }
}

@Preview
@Composable
fun CancelledBadgePreview() {
    FuzeCSGOMatchesThemeContent {
        Surface(modifier = Modifier.padding(FuzeCSGOMatchesTheme.dimen.smallPadding)) {
            StatusBadge(
                text = stringResource(R.string.canceled),
                backgroundColor = FuzeCSGOMatchesTheme.colorScheme.onSurfaceVariant,
                textStyle = FuzeCSGOMatchesTheme.typography.textStyleCancelled,
            )
        }
    }
}

class SampleMatchDtoProvider : PreviewParameterProvider<MatchDto> {
    override val values = sequenceOf(
        createSampleMatchDto(
            id = 1,
            name = "Team Alpha vs Team Bravo",
            status = MatchStatus.ENDED,
            beginAt = Instant.now().minusSeconds(3600),
            isLive = false
        )
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun MatchCardPreview(@PreviewParameter(SampleMatchDtoProvider::class) match: MatchDto) {
    FuzeCSGOMatchesThemeContent {
        Surface {
            MatchCard(match = match)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamDisplayPreview() {
    FuzeCSGOMatchesThemeContent {
        Surface(color = FuzeCSGOMatchesTheme.colorScheme.surfaceContainer) {
            Row(Modifier.padding(FuzeCSGOMatchesTheme.dimen.mediumPadding)) {
                TeamDisplay(
                    name = "Team Alpha",
                    imageUrl = "https://cdn.pandascore.co/images/team/image/126709/9999_big.png".toUri(),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(FuzeCSGOMatchesTheme.dimen.mediumPadding))
                TeamDisplay(
                    name = "Team Bravo With A Very Long Name",
                    imageUrl = null,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun createSampleMatchDto(
    id: Long,
    name: String,
    status: MatchStatus = MatchStatus.UNKNOWN,
    beginAt: Instant?,
    leagueName: String = "ESL Pro League",
    team1Name: String = "Team Alpha",
    team1ImageUrl: Uri? = "https://via.placeholder.com/150/FF0000/FFFFFF?Text=A".toUri(),
    team2Name: String = "Team Bravo With A Very Long Name",
    team2ImageUrl: Uri? = "https://via.placeholder.com/150/0000FF/FFFFFF?Text=B".toUri(),
    isLive: Boolean = false
): MatchDto {
    return MatchDto(
        id = id,
        name = name,
        scheduledAt = if (beginAt == null && status == MatchStatus.SCHEDULED) Instant.now()
            .plusSeconds(3600) else beginAt,
        league = LeagueDto(
            imageUrl = team1ImageUrl,
            name = leagueName,
        ),
        live = LiveDto(
            supported = isLive,
        ),
        opponents = listOf(
            OpponentWrapperDto(
                opponent = OpponentDetailsDto(
                    imageUrl = team1ImageUrl,
                    name = team1Name,

                    ),
            ), OpponentWrapperDto(
                opponent = OpponentDetailsDto(
                    imageUrl = team2ImageUrl,
                    name = team2Name,
                ),
            )
        ),
        serie = SerieDto(
            fullName = "$leagueName Season ${id % 5 + 1}",
            name = "Season ${id % 5 + 1}",
        ),
        slug = name.lowercase().replace(" ", "-") + "-${id}",
        status = status,
    )
}

private fun sampleMatchesForPreview(count: Int): List<MatchDto> {
    return List(count) { index ->
        createSampleMatchDto(
            id = index.toLong() + 1,
            name = "Match ${index + 1}: Team ${'A' + index % 3} vs Team ${'X' + index % 4}",
            status = when (index % 5) {
                0 -> MatchStatus.IN_PROGRESS
                1 -> MatchStatus.SCHEDULED
                2 -> MatchStatus.ENDED
                3 -> MatchStatus.SCHEDULED
                else -> MatchStatus.ENDED
            },
            beginAt = Instant.now().minusSeconds((index * 3600).toLong()),
            isLive = (index % 5 == 0),
        )
    }
}

@Preview(
    name = "MainScreenContent Phone - Empty", showBackground = true, device = Devices.PHONE
)
@Composable
private fun MainScreenContentPreviewPhoneEmpty() {
    FuzeCSGOMatchesThemeContent {
        MainScreenContentPreview(
            matches = emptyList(), onIntent = { })
    }
}

@Preview(
    name = "MainScreenContent Phone - Data", showBackground = true, device = Devices.PHONE
)
@Composable
private fun MainScreenContentPreviewPhoneWithData() {
    FuzeCSGOMatchesThemeContent {
        val sampleMatches = sampleMatchesForPreview(10)
        MainScreenContentPreview(
            matches = sampleMatches, onIntent = { })
    }
}

@Preview(
    name = "MainScreenContent Foldable - Data", showBackground = true, device = Devices.FOLDABLE
)
@Composable
private fun MainScreenContentPreviewFoldableWithData() {
    FuzeCSGOMatchesThemeContent {
        val sampleMatches = sampleMatchesForPreview(15)
        MainScreenContentPreview(
            matches = sampleMatches, onIntent = { })
    }
}

@Preview(
    name = "MainScreenContent Tablet - Data", showBackground = true, device = Devices.TABLET
)
@Composable
private fun MainScreenContentPreviewTabletWithData() {
    FuzeCSGOMatchesThemeContent {
        val sampleMatches = sampleMatchesForPreview(20)
        MainScreenContentPreview(
            matches = sampleMatches, onIntent = { })
    }
}
