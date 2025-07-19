package dev.diegoflassa.fusecsgomatches.main.data.dto

import android.net.Uri
import dev.diegoflassa.fusecsgomatches.core.data.enums.MatchStatus
import dev.diegoflassa.fusecsgomatches.core.data.serializers.InstantSerializer
import dev.diegoflassa.fusecsgomatches.core.data.serializers.UriSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class MatchDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("begin_at") val beginAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("end_at") val endAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("scheduled_at") val scheduledAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("original_scheduled_at") val originalScheduledAt: Instant? = null,
    @SerialName("status") val status: MatchStatus? = MatchStatus.UNKNOWN,
    @SerialName("match_type") val matchType: String? = null,
    @SerialName("number_of_games") val numberOfGames: Int? = null,
    @SerialName("slug") val slug: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("modified_at") val modifiedAt: Instant? = null,
    @SerialName("forfeit") val forfeit: Boolean? = null,
    @SerialName("rescheduled") val rescheduled: Boolean? = null,
    @SerialName("draw") val draw: Boolean? = null,
    @SerialName("winner_id") val winnerId: Long? = null,
    @SerialName("winner_type") val winnerType: String? = null,
    @SerialName("winner") val winner: WinnerDto? = null,
    @SerialName("league_id") val leagueId: Long? = null,
    @SerialName("league") val league: LeagueDto? = null,
    @SerialName("serie_id") val serieId: Long? = null,
    @SerialName("serie") val serie: SerieDto? = null,
    @SerialName("tournament_id") val tournamentId: Long? = null,
    @SerialName("tournament") val tournament: TournamentDto? = null,
    @SerialName("videogame") val videogame: VideogameDto? = null,
    @SerialName("videogame_title") val videogameTitle: VideogameTitleDto? = null,
    @SerialName("videogame_version") val videogameVersion: Map<String?, String?>? = null,
    @SerialName("opponents") val opponents: List<OpponentWrapperDto?>? = null,
    @SerialName("streams_list") val streamsList: List<StreamDto?>? = null,
    @SerialName("games") val games: List<GameDto?>? = null,
    @SerialName("results") val results: List<ResultDto?>? = null,
    @SerialName("live") val live: LiveDto? = null,
    @SerialName("detailed_stats") val detailedStats: Boolean? = null,
    @SerialName("game_advantage") val gameAdvantage: Long? = null
)

@Serializable
data class OpponentWrapperDto(
    @SerialName("opponent") val opponent: OpponentDetailsDto? = null,
    @SerialName("type") val type: String? = null
)

@Serializable
data class OpponentDetailsDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("slug") val slug: String? = null,
    @Serializable(with = UriSerializer::class)
    @SerialName("image_url") val imageUrl: Uri? = null,
    @SerialName("acronym") val acronym: String? = null,
    @SerialName("location") val location: String? = null,
)

@Serializable
data class StreamDto(
    @SerialName("main") val main: Boolean? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("embed_url") val embedUrl: String? = null,
    @SerialName("official") val official: Boolean? = null,
    @SerialName("raw_url") val rawUrl: String? = null
)

@Serializable
data class GameDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("position") val position: Int? = null,
    @SerialName("status") val status: MatchStatus = MatchStatus.UNKNOWN,
    @SerialName("length") val length: Int? = null,
    @SerialName("finished") val finished: Boolean? = null,
    @SerialName("complete") val complete: Boolean? = null,
    @SerialName("match_id") val matchId: Long? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("begin_at") val beginAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("end_at") val endAt: Instant? = null,
    @SerialName("detailed_stats") val detailedStats: Boolean? = null,
    @SerialName("forfeit") val forfeit: Boolean? = null,
    @SerialName("winner") val winner: WinnerDto? = null,
    @SerialName("winner_type") val winnerType: String? = null
)

@Serializable
data class WinnerDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("type") val type: String? = null
)

@Serializable
data class LeagueDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("slug") val slug: String? = null,
    @Serializable(with = UriSerializer::class)
    @SerialName("image_url") val imageUrl: Uri? = null,
    @SerialName("url") val url: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("modified_at") val modifiedAt: Instant? = null
)

@Serializable
data class SerieDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("slug") val slug: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("begin_at") val beginAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("end_at") val endAt: Instant? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("season") val season: String? = null,
    @SerialName("league_id") val leagueId: Long? = null,
    @SerialName("winner_id") val winnerId: Long? = null,
    @SerialName("winner_type") val winnerType: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("modified_at") val modifiedAt: Instant? = null
)


@Serializable
data class TournamentDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("slug") val slug: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("begin_at") val beginAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("end_at") val endAt: Instant? = null,
    @SerialName("league_id") val leagueId: Long? = null,
    @SerialName("serie_id") val serieId: Long? = null,
    @SerialName("winner_id") val winnerId: Long? = null,
    @SerialName("winner_type") val winnerType: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("modified_at") val modifiedAt: Instant? = null,
    @SerialName("prizepool") val prizepool: String? = null,
    @SerialName("tier") val tier: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("detailed_stats") val detailedStats: Boolean? = null,
    @SerialName("has_bracket") val hasBracket: Boolean? = null,
    @SerialName("region") val region: String? = null,
    @SerialName("live_supported") val liveSupported: Boolean? = null
)


@Serializable
data class VideogameDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("slug") val slug: String? = null
)


@Serializable
data class VideogameTitleDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("slug") val slug: String? = null,
    @SerialName("videogame_id") val videogameId: Long? = null
)


@Serializable
data class ResultDto(
    @SerialName("score") val score: Int? = null,
    @SerialName("team_id") val teamId: Long? = null
)


@Serializable
data class LiveDto(
    @SerialName("supported") val supported: Boolean? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("opens_at") val opensAt: Instant? = null,
    @SerialName("url") val url: String? = null
)
