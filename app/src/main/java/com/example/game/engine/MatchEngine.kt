package com.example.game.engine

import androidx.compose.ui.graphics.Color
import com.example.data.model.PlayerEntity
import com.example.data.model.TeamEntity
import com.example.game.model.GameBall
import com.example.game.model.GameMatchPhase
import com.example.game.model.GamePlayer
import com.example.game.model.MatchGameState
import com.example.game.model.PlayerActionState
import com.example.game.model.PlayerTeamSide
import com.example.game.model.Vector2D
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

data class MatchStats(
    val homeScore: Int,
    val awayScore: Int,
    val homeShots: Int,
    val awayShots: Int,
    val homePossessionPercent: Int,
    val awayPossessionPercent: Int,
    val homeTackles: Int,
    val awayTackles: Int,
    val scorers: List<String>
)

enum class MatchAudioHapticEvent {
    KICK_NORMAL,
    KICK_HARD,
    TACKLE_COLLISION,
    POST_HIT,
    GOAL_SCORED,
    KASI_SKILL,
    WHISTLE_START,
    WHISTLE_HALF_TIME,
    WHISTLE_FULL_TIME
}

class MatchEngine(
    val homeTeam: TeamEntity,
    val awayTeam: TeamEntity,
    val homeSquad: List<PlayerEntity> = emptyList(),
    val awaySquad: List<PlayerEntity> = emptyList(),
    val tournamentName: String = "Betway Premiership",
    val matchDurationSeconds: Float = 180f // 3 minutes total real-time duration (90 sec per half)
) {
    val physicsEngine = GamePhysicsEngine()
    val aiController = GameAiController()

    val ball = GameBall()
    val gameState = MatchGameState()

    val players = mutableListOf<GamePlayer>()

    var onAudioHapticEvent: ((MatchAudioHapticEvent) -> Unit)? = null

    var cameraPosition = Vector2D(500f, 325f)
    var homeTacklesCount = 0
    var awayTacklesCount = 0
    var homePossessionSeconds = 0f
    var awayPossessionSeconds = 0f
    val goalScorersList = mutableListOf<String>()

    // Team Kit Colors
    val homeKitPrimary: Color
    val homeKitSecondary: Color
    val awayKitPrimary: Color
    val awayKitSecondary: Color

    init {
        homeKitPrimary = parseHexColor(homeTeam.primaryColorHex, Color(0xFFFFD200)) // Sundowns gold default
        homeKitSecondary = parseHexColor(homeTeam.secondaryColorHex, Color(0xFF003399)) // Blue default
        awayKitPrimary = parseHexColor(awayTeam.primaryColorHex, Color(0xFF1E1E1E)) // Pirates black default
        awayKitSecondary = parseHexColor(awayTeam.secondaryColorHex, Color(0xFFCCCCCC))

        setupStartingLineups()
        resetPositionsForKickoff(kickingTeam = PlayerTeamSide.HOME)
    }

    private fun parseHexColor(hex: String, fallback: Color): Color {
        return try {
            val clean = hex.removePrefix("#")
            val colorLong = clean.toLong(16)
            if (clean.length == 6) Color(0xFF000000 or colorLong) else Color(colorLong)
        } catch (e: Exception) {
            fallback
        }
    }

    private fun setupStartingLineups() {
        players.clear()

        // 4-3-3 Home Slots (Defending Left X=0, Attacking Right X=1000)
        val homeSlots = listOf(
            Pair("GK", Vector2D(45f, 325f)),
            Pair("LB", Vector2D(190f, 130f)),
            Pair("CB", Vector2D(160f, 250f)),
            Pair("CB", Vector2D(160f, 400f)),
            Pair("RB", Vector2D(190f, 520f)),
            Pair("CDM", Vector2D(310f, 325f)),
            Pair("CM", Vector2D(370f, 220f)),
            Pair("CAM", Vector2D(400f, 430f)),
            Pair("LW", Vector2D(470f, 120f)),
            Pair("ST", Vector2D(485f, 325f)),
            Pair("RW", Vector2D(470f, 530f))
        )

        // 4-3-3 Away Slots (Defending Right X=1000, Attacking Left X=0)
        val awaySlots = listOf(
            Pair("GK", Vector2D(955f, 325f)),
            Pair("RB", Vector2D(810f, 130f)),
            Pair("CB", Vector2D(840f, 250f)),
            Pair("CB", Vector2D(840f, 400f)),
            Pair("LB", Vector2D(810f, 520f)),
            Pair("CDM", Vector2D(690f, 325f)),
            Pair("CM", Vector2D(630f, 220f)),
            Pair("CAM", Vector2D(600f, 430f)),
            Pair("RW", Vector2D(530f, 120f)),
            Pair("ST", Vector2D(515f, 325f)),
            Pair("LW", Vector2D(530f, 530f))
        )

        // Spawn Home Team Players
        for (i in 0 until 11) {
            val (posLabel, slot) = homeSlots[i]
            val entity = homeSquad.getOrNull(i)
            val name = entity?.name ?: "Home Player ${i + 1}"
            val jersey = entity?.jerseyNumber ?: (i + 1)
            val pace = entity?.pace?.toFloat() ?: 75f
            val shooting = entity?.shooting?.toFloat() ?: 74f
            val passing = entity?.passing?.toFloat() ?: 76f
            val defending = entity?.defending?.toFloat() ?: 75f
            val overall = entity?.overall ?: 76

            players.add(
                GamePlayer(
                    id = "home_$i",
                    name = name,
                    jerseyNumber = jersey,
                    teamSide = PlayerTeamSide.HOME,
                    positionLabel = posLabel,
                    position = slot,
                    baseFormationSlot = slot,
                    pace = pace,
                    shooting = shooting,
                    passing = passing,
                    defending = defending,
                    diskiFlavour = entity?.dribbling ?: 80,
                    overallRating = overall,
                    facingDirection = Vector2D(1f, 0f)
                )
            )
        }

        // Spawn Away Team Players
        for (i in 0 until 11) {
            val (posLabel, slot) = awaySlots[i]
            val entity = awaySquad.getOrNull(i)
            val name = entity?.name ?: "Away Player ${i + 1}"
            val jersey = entity?.jerseyNumber ?: (i + 1)
            val pace = entity?.pace?.toFloat() ?: 74f
            val shooting = entity?.shooting?.toFloat() ?: 73f
            val passing = entity?.passing?.toFloat() ?: 74f
            val defending = entity?.defending?.toFloat() ?: 74f
            val overall = entity?.overall ?: 75

            players.add(
                GamePlayer(
                    id = "away_$i",
                    name = name,
                    jerseyNumber = jersey,
                    teamSide = PlayerTeamSide.AWAY,
                    positionLabel = posLabel,
                    position = slot,
                    baseFormationSlot = slot,
                    pace = pace,
                    shooting = shooting,
                    passing = passing,
                    defending = defending,
                    diskiFlavour = entity?.dribbling ?: 78,
                    overallRating = overall,
                    facingDirection = Vector2D(-1f, 0f)
                )
            )
        }

        // Default controlled player is Home Striker (index 9)
        selectUserControlledPlayer(players.find { it.id == "home_9" } ?: players.first())
    }

    fun selectUserControlledPlayer(target: GamePlayer) {
        if (target.teamSide != PlayerTeamSide.HOME) return
        for (p in players) {
            p.isControlledByUser = (p.id == target.id)
        }
        gameState.activeControlledPlayerId = target.id
    }

    fun resetPositionsForKickoff(kickingTeam: PlayerTeamSide) {
        for (p in players) {
            p.position = p.baseFormationSlot
            p.velocity = Vector2D.ZERO
            p.state = PlayerActionState.IDLE
            p.facingDirection = if (p.teamSide == PlayerTeamSide.HOME) Vector2D(1f, 0f) else Vector2D(-1f, 0f)
        }

        ball.position = Vector2D(500f, 325f)
        ball.velocity = Vector2D.ZERO
        ball.heightZ = 0f
        ball.velocityZ = 0f

        val kicker = if (kickingTeam == PlayerTeamSide.HOME) {
            players.find { it.id == "home_9" } ?: players[9]
        } else {
            players.find { it.id == "away_9" } ?: players[20]
        }
        kicker.position = Vector2D(500f, 325f)
        ball.ownerPlayerId = kicker.id
        gameState.possessionTeam = kickingTeam
        gameState.phase = GameMatchPhase.PLAYING

        if (kickingTeam == PlayerTeamSide.HOME) {
            selectUserControlledPlayer(kicker)
        }
    }

    // --- Core Match Game Loop Tick ---
    fun update(dt: Float, onMatchFinished: (MatchStats) -> Unit) {
        if (gameState.phase == GameMatchPhase.PAUSED || gameState.phase == GameMatchPhase.FULL_TIME) {
            return
        }

        // 1. Goal Celebration Timer
        if (gameState.phase == GameMatchPhase.GOAL_SCORED) {
            gameState.goalCelebrationTimer -= dt
            if (gameState.goalCelebrationTimer <= 0f) {
                // Resume play after celebration
                val restartingTeam = if (gameState.possessionTeam == PlayerTeamSide.HOME) PlayerTeamSide.AWAY else PlayerTeamSide.HOME
                resetPositionsForKickoff(restartingTeam)
                gameState.commentaryText = "Play resumes from the center circle."
            }
            return
        }

        // 2. Advance Match Clock
        // 90 minutes / matchDurationSeconds gives rate of game seconds per real second
        val gameSpeed = (90f * 60f) / matchDurationSeconds
        gameState.matchClockSeconds += dt * gameSpeed

        // Half-Time Check (45:00 = 2700 game seconds)
        if (gameState.currentHalf == 1 && gameState.matchClockSeconds >= 2700f) {
            gameState.matchClockSeconds = 2700f
            gameState.phase = GameMatchPhase.HALF_TIME
            gameState.commentaryText = "Referee blows the whistle for HALF-TIME! What an intense opening 45 minutes!"
            onAudioHapticEvent?.invoke(MatchAudioHapticEvent.WHISTLE_HALF_TIME)
            return
        }

        // Full-Time Check (90:00 = 5400 game seconds)
        if (gameState.currentHalf == 2 && gameState.matchClockSeconds >= 5400f) {
            gameState.matchClockSeconds = 5400f
            gameState.phase = GameMatchPhase.FULL_TIME
            gameState.commentaryText = "FULL TIME! That is the final whistle of a captivating contest!"
            onAudioHapticEvent?.invoke(MatchAudioHapticEvent.WHISTLE_FULL_TIME)
            onMatchFinished(getMatchStats())
            return
        }

        // 3. Track Possession
        if (gameState.possessionTeam == PlayerTeamSide.HOME) {
            homePossessionSeconds += dt
        } else {
            awayPossessionSeconds += dt
        }
        val totalPoss = max(1f, homePossessionSeconds + awayPossessionSeconds)
        gameState.homePossessionPercent = ((homePossessionSeconds / totalPoss) * 100).toInt().coerceIn(20, 80)
        gameState.isUserAttacking = (gameState.possessionTeam == PlayerTeamSide.HOME)

        // 4. Update Timers on Players (e.g. tackle duration & stunned status)
        for (player in players) {
            if (player.actionTimer > 0f) {
                player.actionTimer -= dt
                if (player.actionTimer <= 0f) {
                    player.state = PlayerActionState.IDLE
                    player.activeSkillName = null
                }
            }
            if (player.stunTimer > 0f) {
                player.stunTimer -= dt
                if (player.stunTimer <= 0f) {
                    player.isStunned = false
                }
            }
        }

        // 5. Physics Engine Step
        physicsEngine.update(
            dt = dt,
            ball = ball,
            players = players,
            gameState = gameState,
            onPostHit = { onAudioHapticEvent?.invoke(MatchAudioHapticEvent.POST_HIT) },
            onGoalScored = { scoringTeam, scorer -> handleGoalScored(scoringTeam, scorer) }
        )

        // 6. AI Controller Step (Controls all players except active user player)
        aiController.updateAi(dt, players, ball, gameState, userTeamSide = PlayerTeamSide.HOME)

        // 7. Auto-switch controlled player if user's team gains ball
        if (gameState.possessionTeam == PlayerTeamSide.HOME && ball.ownerPlayerId != null) {
            val owner = players.find { it.id == ball.ownerPlayerId }
            if (owner != null && owner.teamSide == PlayerTeamSide.HOME && !owner.isControlledByUser) {
                selectUserControlledPlayer(owner)
            }
        }

        // 8. Smooth Camera Tracking
        val activePlayer = players.find { it.isControlledByUser }
        val targetCam = if (activePlayer != null) {
            (activePlayer.position * 0.4f) + (ball.position * 0.6f)
        } else {
            ball.position
        }
        cameraPosition = (cameraPosition * 0.92f) + (targetCam * 0.08f)
    }

    private fun handleGoalScored(scoringTeam: PlayerTeamSide, scorer: String) {
        if (scoringTeam == PlayerTeamSide.HOME) {
            gameState.homeScore += 1
            gameState.lastScorerName = scorer
            goalScorersList.add("${homeTeam.shortName} - $scorer (${gameState.displayGameTime})")
            gameState.commentaryText = "LADUMA! GOAAAL for ${homeTeam.name}! Phenomenal finish by $scorer!"
        } else {
            gameState.awayScore += 1
            gameState.lastScorerName = scorer
            goalScorersList.add("${awayTeam.shortName} - $scorer (${gameState.displayGameTime})")
            gameState.commentaryText = "GOAL! ${awayTeam.name} find the back of the net! Stunning strike by $scorer!"
        }

        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.GOAL_SCORED)
        gameState.phase = GameMatchPhase.GOAL_SCORED
        gameState.goalCelebrationTimer = 3.5f
    }

    fun startSecondHalf() {
        gameState.currentHalf = 2
        gameState.phase = GameMatchPhase.PLAYING
        gameState.commentaryText = "The second half is underway! 45 minutes remaining."
        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.WHISTLE_START)
        resetPositionsForKickoff(kickingTeam = PlayerTeamSide.AWAY)
    }

    // --- User Controls Input Handling ---

    fun onJoystickMove(inputVector: Vector2D, isSprinting: Boolean) {
        val userPlayer = players.find { it.isControlledByUser } ?: return
        if (userPlayer.state == PlayerActionState.TACKLING || userPlayer.state == PlayerActionState.SLIDING) return

        if (inputVector.length() > 0.08f) {
            val dir = inputVector.normalized()
            userPlayer.facingDirection = dir
            val moveSpeed = if (isSprinting) userPlayer.speed * 1.3f else userPlayer.speed
            userPlayer.velocity = dir * moveSpeed
            userPlayer.position = userPlayer.position + (userPlayer.velocity * 0.016f)

            // Clamp inside pitch
            userPlayer.position = Vector2D(
                userPlayer.position.x.coerceIn(15f, 985f),
                userPlayer.position.y.coerceIn(15f, 635f)
            )
            userPlayer.state = if (isSprinting) PlayerActionState.SPRINTING else PlayerActionState.RUNNING
        } else {
            userPlayer.velocity = Vector2D.ZERO
            userPlayer.state = PlayerActionState.IDLE
        }
    }

    fun onPassPressed() {
        val userPlayer = players.find { it.isControlledByUser } ?: return
        if (ball.ownerPlayerId != userPlayer.id) return

        // Find best teammate in facing direction
        val forwardTeammates = players.filter {
            it.teamSide == PlayerTeamSide.HOME && it.id != userPlayer.id
        }

        val targetTeammate = forwardTeammates.minByOrNull { tm ->
            val toTm = (tm.position - userPlayer.position).normalized()
            val dot = (toTm.x * userPlayer.facingDirection.x) + (toTm.y * userPlayer.facingDirection.y)
            // Prioritize teammates in line with facing direction
            val dist = userPlayer.position.distanceTo(tm.position)
            dist - (dot * 80f)
        }

        if (targetTeammate != null) {
            val passDir = (targetTeammate.position - userPlayer.position).normalized()
            ball.kick(passDir, 320f + (userPlayer.passing * 1.2f))
            userPlayer.state = PlayerActionState.PASSING
            userPlayer.actionTimer = 0.2f
            gameState.commentaryText = "Silky ground pass from ${userPlayer.name} towards ${targetTeammate.name}!"
        } else {
            ball.kick(userPlayer.facingDirection, 300f)
        }
        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.KICK_NORMAL)
    }

    fun onThroughBallPressed() {
        val userPlayer = players.find { it.isControlledByUser } ?: return
        if (ball.ownerPlayerId != userPlayer.id) return

        // Lead forward teammate into space
        val forwards = players.filter {
            it.teamSide == PlayerTeamSide.HOME && it.id != userPlayer.id && it.position.x > userPlayer.position.x - 30f
        }
        val target = forwards.maxByOrNull { it.position.x }

        val targetSpot = if (target != null) {
            target.position + Vector2D(65f, (target.facingDirection.y * 20f))
        } else {
            userPlayer.position + (userPlayer.facingDirection.normalized() * 120f)
        }

        val passDir = (targetSpot - userPlayer.position).normalized()
        ball.kick(passDir, 380f)
        userPlayer.state = PlayerActionState.PASSING
        userPlayer.actionTimer = 0.25f
        gameState.commentaryText = "Sensational through ball carving through the defense!"
        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.KICK_NORMAL)
    }

    fun onShootPressed(powerMultiplier: Float) {
        val userPlayer = players.find { it.isControlledByUser } ?: return
        if (ball.ownerPlayerId != userPlayer.id) return

        // Target opponent's goal (Right Goal: X=995, Y=325)
        val goalCenter = Vector2D(995f, 325f)
        val aimOffset = (userPlayer.facingDirection.y * 35f) + ((Random.nextFloat() - 0.5f) * 30f)
        val goalTarget = Vector2D(goalCenter.x, (goalCenter.y + aimOffset).coerceIn(285f, 365f))

        val shotDir = (goalTarget - userPlayer.position).normalized()
        val clampedPower = powerMultiplier.coerceIn(0.3f, 1.0f)
        val shotPower = (280f + (userPlayer.shooting * 2.5f)) * clampedPower
        val loftAngle = 70f * clampedPower

        ball.kick(shotDir, shotPower, loftAngle = loftAngle)
        userPlayer.state = PlayerActionState.SHOOTING
        userPlayer.actionTimer = 0.35f
        gameState.homeShots += 1

        val commentary = when {
            clampedPower > 0.85f -> "THUNDERBOLT! ${userPlayer.name} strikes it with venom towards the top corner!"
            clampedPower > 0.5f -> "Curling shot fired on target by ${userPlayer.name}!"
            else -> "Low stinging drive towards the bottom corner!"
        }
        gameState.commentaryText = commentary
        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.KICK_HARD)
    }

    fun onLobCrossPressed() {
        val userPlayer = players.find { it.isControlledByUser } ?: return
        if (ball.ownerPlayerId != userPlayer.id) return

        // Cross high towards the penalty area
        val crossTarget = Vector2D(880f, 325f)
        val crossDir = (crossTarget - userPlayer.position).normalized()
        ball.kick(crossDir, 340f, loftAngle = 200f)
        userPlayer.state = PlayerActionState.PASSING
        userPlayer.actionTimer = 0.3f
        gameState.commentaryText = "Floating cross lofted into the danger box!"
        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.KICK_NORMAL)
    }

    fun onKasiSkillPressed() {
        val userPlayer = players.find { it.isControlledByUser } ?: return
        if (ball.ownerPlayerId != userPlayer.id) return
        if (userPlayer.state == PlayerActionState.SKILL_MOVE) return

        val skills = listOf(
            "TSAMAYA",
            "SHOW ME YOUR NUMBER",
            "KASI SHUFFLE",
            "SCISSOR STEPOVER",
            "RAINBOW FLICK"
        )
        val chosenSkill = skills.random()

        userPlayer.state = PlayerActionState.SKILL_MOVE
        userPlayer.activeSkillName = chosenSkill
        userPlayer.actionTimer = 0.7f

        // Boost player forward with flair
        val burstDir = userPlayer.facingDirection.normalized()
        userPlayer.velocity = burstDir * (userPlayer.speed * 1.35f)
        userPlayer.position = userPlayer.position + (userPlayer.velocity * 0.08f)

        if (chosenSkill == "RAINBOW FLICK") {
            ball.heightZ = 45f
            ball.velocityZ = 130f
        }

        // Stun nearby opponent defenders
        val stunnedOpponents = players.filter {
            it.teamSide != PlayerTeamSide.HOME && it.position.distanceTo(userPlayer.position) < 70f
        }
        for (opp in stunnedOpponents) {
            opp.isStunned = true
            opp.stunTimer = 0.9f
            opp.velocity = Vector2D.ZERO
            opp.state = PlayerActionState.IDLE
        }

        val commentary = when (chosenSkill) {
            "SHOW ME YOUR NUMBER" -> "AYEBA! Show Me Your Number! Pure Kasi flavour by ${userPlayer.name}!"
            "TSAMAYA" -> "TSAMAYA! ${userPlayer.name} sends the defender looking for a taxi to Soweto!"
            "KASI SHUFFLE" -> "Kasi Shuffle! Shoe-shine and piano football in full effect!"
            "SCISSOR STEPOVER" -> "Devastating scissor stepover! The defender is completely bamboozled!"
            "RAINBOW FLICK" -> "OH MY DISKI! Rainbow flick over the defender's head!"
            else -> "Spectacular Diski flair by ${userPlayer.name}!"
        }
        gameState.commentaryText = commentary
        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.KASI_SKILL)
    }

    fun onTacklePressed() {
        val userPlayer = players.find { it.isControlledByUser } ?: return
        userPlayer.state = PlayerActionState.TACKLING
        userPlayer.actionTimer = 0.35f
        homeTacklesCount += 1
        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.TACKLE_COLLISION)

        // Check if close to ball owner
        val ballOwner = players.find { it.id == ball.ownerPlayerId }
        if (ballOwner != null && ballOwner.teamSide == PlayerTeamSide.AWAY) {
            val dist = userPlayer.position.distanceTo(ballOwner.position)
            if (dist < 38f) {
                ball.ownerPlayerId = userPlayer.id
                ball.lastTouchTeam = PlayerTeamSide.HOME
                gameState.possessionTeam = PlayerTeamSide.HOME
                gameState.commentaryText = "Brilliant standing tackle by ${userPlayer.name} to win the ball back!"
            }
        }
    }

    fun onSlideTacklePressed() {
        val userPlayer = players.find { it.isControlledByUser } ?: return
        userPlayer.state = PlayerActionState.SLIDING
        userPlayer.actionTimer = 0.5f
        homeTacklesCount += 1
        onAudioHapticEvent?.invoke(MatchAudioHapticEvent.TACKLE_COLLISION)

        // Slide momentum forward
        userPlayer.velocity = userPlayer.facingDirection.normalized() * 260f
        userPlayer.position = userPlayer.position + (userPlayer.velocity * 0.1f)

        val ballOwner = players.find { it.id == ball.ownerPlayerId }
        if (ballOwner != null && ballOwner.teamSide == PlayerTeamSide.AWAY) {
            val dist = userPlayer.position.distanceTo(ballOwner.position)
            if (dist < 42f) {
                // Dispossess cleanly!
                ball.ownerPlayerId = null
                ball.kick(userPlayer.facingDirection, 280f)
                gameState.possessionTeam = PlayerTeamSide.HOME
                gameState.commentaryText = "TREMENDOUS SLIDE TACKLE! Diski magic on display!"
            }
        }
    }

    fun onSwitchPlayerPressed() {
        // Switch to the outfield teammate closest to ball
        val bestTeammate = players
            .filter { it.teamSide == PlayerTeamSide.HOME && it.positionLabel != "GK" && it.id != gameState.activeControlledPlayerId }
            .minByOrNull { it.position.distanceTo(ball.position) }

        if (bestTeammate != null) {
            selectUserControlledPlayer(bestTeammate)
        }
    }

    fun getMatchStats(): MatchStats {
        val totalPoss = max(1f, homePossessionSeconds + awayPossessionSeconds)
        val homePoss = ((homePossessionSeconds / totalPoss) * 100).toInt().coerceIn(20, 80)
        return MatchStats(
            homeScore = gameState.homeScore,
            awayScore = gameState.awayScore,
            homeShots = gameState.homeShots,
            awayShots = gameState.awayShots,
            homePossessionPercent = homePoss,
            awayPossessionPercent = 100 - homePoss,
            homeTackles = homeTacklesCount,
            awayTackles = awayTacklesCount,
            scorers = goalScorersList
        )
    }
}
