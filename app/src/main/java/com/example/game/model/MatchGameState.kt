package com.example.game.model

enum class GameMatchPhase {
    KICKOFF,
    PLAYING,
    GOAL_SCORED,
    CORNER,
    THROW_IN,
    HALF_TIME,
    FULL_TIME,
    PAUSED
}

data class MatchGameState(
    var phase: GameMatchPhase = GameMatchPhase.KICKOFF,
    var homeScore: Int = 0,
    var awayScore: Int = 0,
    var matchClockSeconds: Float = 0f,     // 0 to 5400 (90 min * 60)
    var currentHalf: Int = 1,
    var possessionTeam: PlayerTeamSide = PlayerTeamSide.HOME,
    var activeControlledPlayerId: String = "",
    var commentaryText: String = "Welcome to the clash! Referee signals kickoff.",
    var goalCelebrationTimer: Float = 0f,
    var lastScorerName: String = "",
    var homeShots: Int = 0,
    var awayShots: Int = 0,
    var homePossessionPercent: Int = 50,
    var isUserAttacking: Boolean = true
) {
    val displayGameTime: String
        get() {
            val totalGameMinutes = (matchClockSeconds / 60f).toInt()
            return "$totalGameMinutes'"
        }
}
