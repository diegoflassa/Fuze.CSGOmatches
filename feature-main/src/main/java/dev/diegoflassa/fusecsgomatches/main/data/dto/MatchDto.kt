package dev.diegoflassa.fusecsgomatches.main.data.dto

import android.net.Uri
import com.squareup.moshi.Json
import dev.diegoflassa.fusecsgomatches.core.data.enums.MatchStatus
import java.time.Instant

data class MatchDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "begin_at") val beginAt: Instant? = null,
    @field:Json(name = "end_at") val endAt: Instant? = null,
    @field:Json(name = "scheduled_at") val scheduledAt: Instant? = null,
    @field:Json(name = "original_scheduled_at") val originalScheduledAt: Instant? = null,
    @field:Json(name = "status") val status: MatchStatus = MatchStatus.UNKNOWN,
    @field:Json(name = "match_type") val matchType: String? = null,
    @field:Json(name = "number_of_games") val numberOfGames: Int? = null,
    @field:Json(name = "slug") val slug: String? = null,
    @field:Json(name = "modified_at") val modifiedAt: Instant? = null,
    @field:Json(name = "forfeit") val forfeit: Boolean? = null,
    @field:Json(name = "rescheduled") val rescheduled: Boolean? = null,
    @field:Json(name = "draw") val draw: Boolean? = null,
    @field:Json(name = "winner_id") val winnerId: Long? = null,
    @field:Json(name = "winner_type") val winnerType: String? = null,
    @field:Json(name = "winner") val winner: WinnerDto? = null,
    @field:Json(name = "league_id") val leagueId: Long? = null,
    @field:Json(name = "league") val league: LeagueDto? = null,
    @field:Json(name = "serie_id") val serieId: Long? = null,
    @field:Json(name = "serie") val serie: SerieDto? = null,
    @field:Json(name = "tournament_id") val tournamentId: Long? = null,
    @field:Json(name = "tournament") val tournament: TournamentDto? = null,
    @field:Json(name = "videogame") val videogame: VideogameDto? = null,
    @field:Json(name = "videogame_title") val videogameTitle: VideogameTitleDto? = null,
    @field:Json(name = "videogame_version") val videogameVersion: Map<String?, String?>? = null,
    @field:Json(name = "opponents") val opponents: List<OpponentWrapperDto?>? = null,
    @field:Json(name = "streams_list") val streamsList: List<StreamDto?>? = null,
    @field:Json(name = "games") val games: List<GameDto?>? = null,
    @field:Json(name = "results") val results: List<ResultDto?>? = null,
    @field:Json(name = "live") val live: LiveDto? = null,
    @field:Json(name = "detailed_stats") val detailedStats: Boolean? = null,
    @field:Json(name = "game_advantage") val gameAdvantage: Long? = null)


data class OpponentWrapperDto(
    @field:Json(name = "opponent") val opponent: OpponentDetailsDto? = null,
    @field:Json(name = "type") val type: String? = null)


data class OpponentDetailsDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "slug") val slug: String? = null,
    @field:Json(name = "image_url") val imageUrl: Uri? = null,
    @field:Json(name = "acronym") val acronym: String? = null)


data class StreamDto(
    @field:Json(name = "main") val main: Boolean? = null,
    @field:Json(name = "language") val language: String? = null,
    @field:Json(name = "embed_url") val embedUrl: String? = null,
    @field:Json(name = "official") val official: Boolean? = null,
    @field:Json(name = "raw_url") val rawUrl: String? = null)


data class GameDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "position") val position: Int? = null,
    @field:Json(name = "status") val status: MatchStatus = MatchStatus.UNKNOWN,
    @field:Json(name = "length") val length: Int? = null,
    @field:Json(name = "finished") val finished: Boolean? = null,
    @field:Json(name = "complete") val complete: Boolean? = null,
    @field:Json(name = "match_id") val matchId: Long? = null,
    @field:Json(name = "begin_at") val beginAt: Instant? = null,
    @field:Json(name = "end_at") val endAt: Instant? = null,
    @field:Json(name = "detailed_stats") val detailedStats: Boolean? = null,
    @field:Json(name = "forfeit") val forfeit: Boolean? = null,
    @field:Json(name = "winner") val winner: WinnerDto? = null,
    @field:Json(name = "winner_type") val winnerType: String? = null)


data class WinnerDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "type") val type: String? = null)


data class LeagueDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "slug") val slug: String? = null,
    @field:Json(name = "image_url") val imageUrl: Uri? = null,
    @field:Json(name = "url") val url: String? = null,
    @field:Json(name = "modified_at") val modifiedAt: Instant? = null)


data class SerieDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "full_name") val fullName: String? = null,
    @field:Json(name = "slug") val slug: String? = null,
    @field:Json(name = "begin_at") val beginAt: Instant? = null,
    @field:Json(name = "end_at") val endAt: Instant? = null,
    @field:Json(name = "year") val year: Int? = null,
    @field:Json(name = "season") val season: String? = null,
    @field:Json(name = "league_id") val leagueId: Long? = null,
    @field:Json(name = "winner_id") val winnerId: Long? = null,
    @field:Json(name = "winner_type") val winnerType: String? = null,
    @field:Json(name = "modified_at") val modifiedAt: Instant? = null)


data class TournamentDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "slug") val slug: String? = null,
    @field:Json(name = "begin_at") val beginAt: Instant? = null,
    @field:Json(name = "end_at") val endAt: Instant? = null,
    @field:Json(name = "league_id") val leagueId: Long? = null,
    @field:Json(name = "serie_id") val serieId: Long? = null,
    @field:Json(name = "winner_id") val winnerId: Long? = null,
    @field:Json(name = "winner_type") val winnerType: String? = null,
    @field:Json(name = "modified_.at") val modifiedAt: Instant? = null,
    @field:Json(name = "prizepool") val prizepool: String? = null,
    @field:Json(name = "tier") val tier: String? = null,
    @field:Json(name = "type") val type: String? = null,
    @field:Json(name = "country") val country: String? = null,
    @field:Json(name = "detailed_stats") val detailedStats: Boolean? = null,
    @field:Json(name = "has_bracket") val hasBracket: Boolean? = null,
    @field:Json(name = "region") val region: String? = null,
    @field:Json(name = "live_supported") val liveSupported: Boolean? = null)


data class VideogameDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "slug") val slug: String? = null)


data class VideogameTitleDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "slug") val slug: String? = null,
    @field:Json(name = "videogame_id") val videogameId: Long? = null)


data class ResultDto(
    @field:Json(name = "score") val score: Int? = null,
    @field:Json(name = "team_id") val teamId: Long? = null)


data class LiveDto(
    @field:Json(name = "supported") val supported: Boolean? = null,
    @field:Json(name = "opens_at") val opensAt: Instant? = null,
    @field:Json(name = "url") val url: String? = null)
