package dev.diegoflassa.fusecsgomatches.details.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.diegoflassa.fusecsgomatches.core.extensions.hiltActivityViewModel
import dev.diegoflassa.fusecsgomatches.core.navigation.NavigationViewModel
import dev.diegoflassa.fusecsgomatches.core.theme.FuseCSGOMatchesColors
import dev.diegoflassa.fusecsgomatches.core.theme.FuseCSGOMatchesTheme
import dev.diegoflassa.fusecsgomatches.core.theme.FuseCSGOMatchesThemeContent
import dev.diegoflassa.fusecsgomatches.details.R
import dev.diegoflassa.fusecsgomatches.details.data.dto.MatchOpponentsResponseDto
import dev.diegoflassa.fusecsgomatches.details.data.dto.OpponentTeamDetailDto
import dev.diegoflassa.fusecsgomatches.details.data.dto.PlayerDetailDto
import dev.diegoflassa.fusecsgomatches.details.data.dto.VideogameDto
import kotlinx.coroutines.flow.collectLatest
import androidx.core.net.toUri

private const val tag = "DetailsScreen"

@Composable
fun DetailsScreen(
    matchIdOrSlug: String? = null,
    navigationViewModel: NavigationViewModel = hiltActivityViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel(),
) {
    val uiState = detailsViewModel.uiState.collectAsState().value

    LaunchedEffect(matchIdOrSlug) {
        if (!matchIdOrSlug.isNullOrEmpty()) {
            detailsViewModel.reduce(DetailsIntent.LoadDetails(matchIdOrSlug))
        }
    }

    LaunchedEffect(detailsViewModel.effect) {
        detailsViewModel.effect.collectLatest { effect ->
            when (effect) {
                is DetailsEffect.ShowError -> {
                    Log.e(tag, "Error effect: ${effect.message}")
                }

                is DetailsEffect.NavigateToMain -> {
                    navigationViewModel.navigateToMain()
                }
            }
        }
    }

    DetailsScreenContent(
        uiState = uiState,
        navigationViewModel = navigationViewModel,
        onRetry = {
            if (!matchIdOrSlug.isNullOrEmpty()) {
                detailsViewModel.reduce(DetailsIntent.LoadDetails(matchIdOrSlug))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: DetailsUIState,
    navigationViewModel: NavigationViewModel?,
    onRetry: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FuseCSGOMatchesTheme.colorScheme.background)
            .padding(
                top = FuseCSGOMatchesTheme.dimen.mediumLargePadding,
                start = FuseCSGOMatchesTheme.dimen.mediumLargePadding,
                end = FuseCSGOMatchesTheme.dimen.mediumLargePadding,
                bottom = FuseCSGOMatchesTheme.dimen.noPadding
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.details_screen_opponents_title),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigationViewModel?.navigateToMain() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = stringResource(id = R.string.back_button_description),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = uiState.error,
                                color = FuseCSGOMatchesTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = FuseCSGOMatchesTheme.dimen.mediumPadding)
                            )
                            Button(onClick = onRetry) {
                                Text(text = stringResource(id = R.string.retry))
                            }
                        }
                    }
                }

                uiState.opponents == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(id = R.string.no_matches_found),
                            color = FuseCSGOMatchesTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    OpponentDetailsBody(opponentsResponse = uiState.opponents)
                }
            }
        }
    }
}

@Composable
fun OpponentDetailsBody(opponentsResponse: MatchOpponentsResponseDto) {
    if (opponentsResponse.opponents.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                stringResource(id = R.string.no_opponents_message),
                color = FuseCSGOMatchesTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = FuseCSGOMatchesTheme.dimen.mediumPadding),
        verticalArrangement = Arrangement.spacedBy(FuseCSGOMatchesTheme.dimen.largePadding)
    ) {
        items(items = opponentsResponse.opponents, key = { it.id }) { team ->
            TeamSection(team = team)
        }
    }
}

@Composable
fun TeamSection(team: OpponentTeamDetailDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FuseCSGOMatchesTheme.dimen.smallPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = FuseCSGOMatchesTheme.dimen.mediumPadding)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(team.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = ColorPainter(FuseCSGOMatchesColors.imageAbsentColor),
                error = rememberVectorPainter(image = Icons.Outlined.BrokenImage),
                fallback = ColorPainter(FuseCSGOMatchesColors.imageAbsentColor),
                contentDescription = stringResource(R.string.team_logo_desc, team.name),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(FuseCSGOMatchesTheme.dimen.mediumPadding))
            Text(
                text = team.name,
                //style = FuseCSGOMatchesTheme.typography.titleMedium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (team.players.isEmpty()) {
            Text(
                text = stringResource(R.string.details_screen_no_players_in_team_message),
                color = FuseCSGOMatchesTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = FuseCSGOMatchesTheme.dimen.mediumPadding)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(FuseCSGOMatchesTheme.dimen.smallPadding),
                verticalArrangement = Arrangement.spacedBy(FuseCSGOMatchesTheme.dimen.cardsPadding),
                horizontalArrangement = Arrangement.spacedBy(FuseCSGOMatchesTheme.dimen.cardsPadding),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(determinePlayerGridHeight(team.players.size))
            ) {
                items(items = team.players, key = { it.id }) { player ->
                    PlayerCard(player = player)
                }
            }
        }
    }
}

@Composable
private fun determinePlayerGridHeight(
    playerCount: Int,
    columns: Int = 2,
    rowHeight: Dp = 100.dp
): Dp {
    if (playerCount == 0) return 0.dp
    val rows = (playerCount + columns - 1) / columns
    return (rowHeight * rows) + (FuseCSGOMatchesTheme.dimen.cardsPadding * (rows - 1).coerceAtLeast(
        0
    ))
}


@Composable
fun PlayerCard(player: PlayerDetailDto?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(containerColor = FuseCSGOMatchesTheme.colorScheme.surfaceContainerHighest),
        shape = RoundedCornerShape(FuseCSGOMatchesTheme.dimen.mediumPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(FuseCSGOMatchesTheme.dimen.mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(player?.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = ColorPainter(FuseCSGOMatchesColors.imageAbsentColor),
                error = rememberVectorPainter(image = Icons.Outlined.BrokenImage),
                fallback = ColorPainter(FuseCSGOMatchesColors.imageAbsentColor),
                contentDescription = stringResource(
                    R.string.player_image_desc,
                    player?.name ?: stringResource(id = R.string.unknown_player_real_name)
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(FuseCSGOMatchesTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f))
            )
            Spacer(modifier = Modifier.width(FuseCSGOMatchesTheme.dimen.mediumPadding))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player?.name ?: stringResource(id = R.string.unknown_player_real_name),
                    //style = FuseCSGOMatchesTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val realName = ("${player?.firstName ?: ""} ${player?.lastName ?: ""}").trim()
                if (realName.isNotEmpty()) {
                    Text(
                        text = realName,
                        //style = FuseCSGOMatchesTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                player?.role?.let {
                    Text(
                        text = "Role: $it",
                        //style = FuseCSGOMatchesTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun samplePlayer(id: Int, name: String, role: String? = "attacker"): PlayerDetailDto {
    return PlayerDetailDto(
        active = true,
        id = id,
        name = name,
        role = role,
        slug = name.lowercase().replace(" ", "-"),
        modifiedAt = "2023-01-01T12:00:00Z",
        birthday = "1990-01-01",
        firstName = name.split(" ").firstOrNull() ?: "-",
        lastName = name.split(" ").getOrNull(1) ?: "-",
        nationality = "US",
        age = 30,
        imageUrl = "https://cdn.pandascore.co/images/player/image/${id}/${name.lowercase()}.png".toUri()
    )
}

private fun sampleTeam(id: Int, teamName: String, playerCount: Int): OpponentTeamDetailDto {
    return OpponentTeamDetailDto(
        id = id,
        name = teamName,
        location = "US",
        players = List(playerCount) { playerIndex ->
            samplePlayer(id = id * 100 + playerIndex, name = "Player ${'A' + playerIndex}")
        },
        slug = teamName.lowercase().replace(" ", "-"),
        modifiedAt = "2023-01-01T12:00:00Z",
        acronym = teamName.take(3).uppercase(),
        imageUrl = "https://cdn.pandascore.co/images/team/image/${id}/${teamName.lowercase()}.png".toUri(),
        currentVideogame = VideogameDto(id = 1, name = "GameName", slug = "game-name")
    )
}

private fun sampleOpponentsResponse(
    teamCount: Int = 2,
    playersPerTeam: Int = 5
): MatchOpponentsResponseDto {
    return MatchOpponentsResponseDto(
        opponentType = "Team",
        opponents = List(teamCount) { teamIndex ->
            sampleTeam(
                id = teamIndex + 1,
                teamName = "Team ${'A' + teamIndex}",
                playerCount = playersPerTeam
            )
        }
    )
}

@Preview(name = "PlayerCard Preview", showBackground = true)
@Composable
private fun PlayerCardPreview() {
    FuseCSGOMatchesThemeContent {
        Surface(color = FuseCSGOMatchesTheme.colorScheme.background) {
            PlayerCard(player = samplePlayer(1, "Player X", "Mid"))
        }
    }
}

@Preview(name = "TeamSection Preview", showBackground = true)
@Composable
private fun TeamSectionPreview() {
    FuseCSGOMatchesThemeContent {
        Surface(color = FuseCSGOMatchesTheme.colorScheme.background) {
            TeamSection(team = sampleTeam(1, "The Great Team", 3))
        }
    }
}

@Preview(name = "OpponentDetailsBody Preview", showBackground = true, device = Devices.PHONE)
@Composable
private fun OpponentDetailsBodyPreview() {
    FuseCSGOMatchesThemeContent {
        OpponentDetailsBody(
            opponentsResponse = sampleOpponentsResponse(
                teamCount = 3,
                playersPerTeam = 4
            )
        )
    }
}

@Preview(name = "DetailsScreenContent - Loading", showBackground = true)
@Composable
private fun DetailsScreenContentLoadingPreview() {
    FuseCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(isLoading = true, error = null, opponents = null),
            navigationViewModel = null,
            onRetry = {}
        )
    }
}

@Preview(name = "DetailsScreenContent - Error", showBackground = true)
@Composable
private fun DetailsScreenContentErrorPreview() {
    FuseCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                error = "Failed to load opponent details. Please try again.",
                opponents = null
            ),
            navigationViewModel = null,
            onRetry = {}
        )
    }
}

@Preview(name = "DetailsScreenContent - No Data", showBackground = true)
@Composable
private fun DetailsScreenContentNoDataPreview() {
    FuseCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(isLoading = false, error = null, opponents = null),
            navigationViewModel = null,
            onRetry = {}
        )
    }
}

@Preview(name = "DetailsScreenContent - With Data", device = Devices.PHONE, showBackground = true)
@Composable
private fun DetailsScreenContentWithDataPreview() {
    FuseCSGOMatchesThemeContent {
        DetailsScreenContent(
            uiState = DetailsUIState(
                isLoading = false,
                error = null,
                opponents = sampleOpponentsResponse()
            ),
            navigationViewModel = null,
            onRetry = {}
        )
    }
}
