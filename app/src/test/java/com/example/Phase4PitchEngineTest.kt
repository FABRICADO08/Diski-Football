package com.example

import com.example.data.model.LeagueType
import com.example.data.model.PlayerEntity
import com.example.data.model.TeamEntity
import com.example.game.engine.MatchEngine
import com.example.game.engine.MatchStats
import com.example.game.model.GameMatchPhase
import com.example.game.model.PlayerActionState
import com.example.game.model.PlayerTeamSide
import com.example.game.model.Vector2D
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Phase4PitchEngineTest {

    private lateinit var homeTeam: TeamEntity
    private lateinit var awayTeam: TeamEntity
    private lateinit var engine: MatchEngine

    @Before
    fun setup() {
        homeTeam = com.example.data.seed.SouthAfricanTeamsData.pslTeams.first { it.id == "mamelodi_sundowns" }
        awayTeam = com.example.data.seed.SouthAfricanTeamsData.pslTeams.first { it.id == "orlando_pirates" }

        val homeSquad = (1..11).map { i ->
            PlayerEntity(
                id = "sun_$i",
                teamId = "mamelodi_sundowns",
                name = "Sundowns Star $i",
                position = if (i == 1) com.example.data.model.PlayerPosition.GK else if (i <= 5) com.example.data.model.PlayerPosition.CB else if (i <= 8) com.example.data.model.PlayerPosition.CM else com.example.data.model.PlayerPosition.ST,
                jerseyNumber = i,
                age = 26,
                overall = 80 + (i % 3),
                pace = 80,
                shooting = 78,
                passing = 82,
                dribbling = 80,
                defending = 75,
                physical = 76,
                stamina = 90,
                marketValue = 10_000_000L,
                weeklyWage = 25_000L,
                contractYearsRemaining = 3
            )
        }

        val awaySquad = (1..11).map { i ->
            PlayerEntity(
                id = "pir_$i",
                teamId = "orlando_pirates",
                name = "Pirates Star $i",
                position = if (i == 1) com.example.data.model.PlayerPosition.GK else if (i <= 5) com.example.data.model.PlayerPosition.CB else if (i <= 8) com.example.data.model.PlayerPosition.CM else com.example.data.model.PlayerPosition.ST,
                jerseyNumber = i,
                age = 25,
                overall = 79,
                pace = 79,
                shooting = 76,
                passing = 78,
                dribbling = 77,
                defending = 77,
                physical = 75,
                stamina = 88,
                marketValue = 8_000_000L,
                weeklyWage = 22_000L,
                contractYearsRemaining = 2
            )
        }

        engine = MatchEngine(
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeSquad = homeSquad,
            awaySquad = awaySquad,
            tournamentName = "Betway Premiership",
            matchDurationSeconds = 180f
        )
    }

    @Test
    fun testVector2DMathematics() {
        val v1 = Vector2D(3f, 4f)
        assertEquals(5f, v1.length(), 0.001f)

        val norm = v1.normalized()
        assertEquals(0.6f, norm.x, 0.001f)
        assertEquals(0.8f, norm.y, 0.001f)

        val v2 = Vector2D(1f, 2f)
        val sum = v1 + v2
        assertEquals(4f, sum.x, 0.001f)
        assertEquals(6f, sum.y, 0.001f)

        val dist = v1.distanceTo(Vector2D(6f, 8f))
        assertEquals(5f, dist, 0.001f)
    }

    @Test
    fun testStartingLineupsInitialization() {
        assertEquals(22, engine.players.size)
        val homePlayers = engine.players.filter { it.teamSide == PlayerTeamSide.HOME }
        val awayPlayers = engine.players.filter { it.teamSide == PlayerTeamSide.AWAY }

        assertEquals(11, homePlayers.size)
        assertEquals(11, awayPlayers.size)

        // Check active controlled player is Home
        val userPlayer = engine.players.find { it.isControlledByUser }
        assertNotNull(userPlayer)
        assertEquals(PlayerTeamSide.HOME, userPlayer?.teamSide)
        assertEquals("home_9", engine.gameState.activeControlledPlayerId)

        // Ball starts at center circle
        assertEquals(500f, engine.ball.position.x, 0.1f)
        assertEquals(325f, engine.ball.position.y, 0.1f)
    }

    @Test
    fun testJoystickMovement() {
        val initialPlayer = engine.players.find { it.isControlledByUser }!!
        val startPos = initialPlayer.position

        // Move right with sprint
        engine.onJoystickMove(Vector2D(1f, 0f), isSprinting = true)

        assertEquals(PlayerActionState.SPRINTING, initialPlayer.state)
        assertTrue(initialPlayer.position.x > startPos.x)
        assertEquals(1f, initialPlayer.facingDirection.x, 0.01f)

        // Release joystick
        engine.onJoystickMove(Vector2D.ZERO, isSprinting = false)
        assertEquals(PlayerActionState.IDLE, initialPlayer.state)
    }

    @Test
    fun testShootAction() {
        val userPlayer = engine.players.find { it.isControlledByUser }!!
        // Give ball to user player
        engine.ball.ownerPlayerId = userPlayer.id

        val initialShots = engine.gameState.homeShots
        engine.onShootPressed(0.8f)

        // Ball should be kicked towards opponent goal
        assertFalse(engine.ball.ownerPlayerId == userPlayer.id)
        assertTrue(engine.ball.velocity.length() > 200f)
        assertTrue(engine.ball.velocityZ > 0f)
        assertEquals(initialShots + 1, engine.gameState.homeShots)
        assertEquals(PlayerActionState.SHOOTING, userPlayer.state)
    }

    @Test
    fun testPassAction() {
        val userPlayer = engine.players.find { it.isControlledByUser }!!
        engine.ball.ownerPlayerId = userPlayer.id

        engine.onPassPressed()

        // Ball should be released with ground velocity
        assertEquals(null, engine.ball.ownerPlayerId)
        assertTrue(engine.ball.velocity.length() > 150f)
        assertEquals(0f, engine.ball.heightZ, 0.01f)
    }

    @Test
    fun testTackleAction() {
        val userPlayer = engine.players.find { it.isControlledByUser }!!
        engine.onTacklePressed()

        assertEquals(PlayerActionState.TACKLING, userPlayer.state)
        assertEquals(1, engine.homeTacklesCount)
    }

    @Test
    fun testSlideTackleAction() {
        val userPlayer = engine.players.find { it.isControlledByUser }!!
        engine.onSlideTacklePressed()

        assertEquals(PlayerActionState.SLIDING, userPlayer.state)
        assertTrue(userPlayer.actionTimer > 0f)
    }

    @Test
    fun testGoalScoredDetection() {
        // Kick ball into away goal mouth (Right goal: X >= 995, Y in 275..375)
        engine.ball.ownerPlayerId = null
        engine.ball.position = Vector2D(996f, 325f)
        engine.ball.velocity = Vector2D(50f, 0f)

        var finishedStats: MatchStats? = null
        engine.update(0.016f) { stats ->
            finishedStats = stats
        }

        // Home score should increase by 1 and goal celebration trigger
        assertEquals(1, engine.gameState.homeScore)
        assertEquals(GameMatchPhase.GOAL_SCORED, engine.gameState.phase)
        assertTrue(engine.gameState.goalCelebrationTimer > 0f)
        assertTrue(engine.gameState.commentaryText.contains("LADUMA"))
    }

    @Test
    fun testMatchStatsSummary() {
        engine.gameState.homeScore = 2
        engine.gameState.awayScore = 1
        engine.gameState.homeShots = 7
        engine.gameState.awayShots = 3
        engine.homeTacklesCount = 5
        engine.awayTacklesCount = 4

        val stats = engine.getMatchStats()
        assertEquals(2, stats.homeScore)
        assertEquals(1, stats.awayScore)
        assertEquals(7, stats.homeShots)
        assertEquals(3, stats.awayShots)
        assertEquals(5, stats.homeTackles)
        assertEquals(4, stats.awayTackles)
    }
}
