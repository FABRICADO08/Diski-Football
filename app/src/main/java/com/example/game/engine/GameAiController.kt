package com.example.game.engine

import com.example.game.model.GameBall
import com.example.game.model.GamePlayer
import com.example.game.model.MatchGameState
import com.example.game.model.PlayerActionState
import com.example.game.model.PlayerTeamSide
import com.example.game.model.Vector2D
import kotlin.random.Random

class GameAiController(
    var difficulty: String = "Medium"
) {

    fun updateAi(
        dt: Float,
        players: List<GamePlayer>,
        ball: GameBall,
        gameState: MatchGameState,
        userTeamSide: PlayerTeamSide = PlayerTeamSide.HOME
    ) {
        val homeGoalTarget = Vector2D(985f, 325f)
        val awayGoalTarget = Vector2D(15f, 325f)

        val speedMultiplier = when (difficulty.lowercase()) {
            "easy" -> 0.78f
            "hard" -> 1.15f
            "legendary" -> 1.28f
            else -> 1.0f
        }

        for (player in players) {
            // Skip user-controlled player (they are controlled by Virtual Joystick!)
            if (player.isControlledByUser) continue

            // Stunned by Kasi Flavour skill move
            if (player.isStunned) {
                player.velocity = Vector2D.ZERO
                player.state = PlayerActionState.IDLE
                continue
            }

            val isAiControlledTeam = player.teamSide != userTeamSide
            val isTeammateOfUser = player.teamSide == userTeamSide

            // Goalkeeper specific logic
            if (player.positionLabel == "GK") {
                updateGoalkeeper(dt, player, ball)
                continue
            }

            // Normal Outfield Players
            if (isAiControlledTeam) {
                // Opponent AI
                if (ball.ownerPlayerId == player.id) {
                    // This AI player has the ball: Dribble or Pass or Shoot!
                    updateAiBallCarrier(dt, player, ball, players, awayGoalTarget, speedMultiplier)
                } else if (gameState.possessionTeam == player.teamSide) {
                    // AI team is attacking, make supporting runs
                    val target = player.baseFormationSlot + Vector2D(-50f + (ball.position.x * 0.1f), 0f)
                    moveTowards(dt, player, target, isSprint = false, speedMultiplier = speedMultiplier)
                } else {
                    // AI team is defending: Press or Mark
                    updateAiDefending(dt, player, ball, players, speedMultiplier)
                }
            } else if (isTeammateOfUser) {
                // User's teammates (controlled by AI)
                if (gameState.possessionTeam == userTeamSide) {
                    // Attacking support run into open space
                    val supportOffset = Vector2D(ball.position.x * 0.25f, (ball.position.y - 325f) * 0.2f)
                    val target = player.baseFormationSlot + supportOffset
                    moveTowards(dt, player, target, isSprint = false)
                } else {
                    // Defending: hold formation and cover spaces
                    val pressBall = player.position.distanceTo(ball.position) < 140f
                    val target = if (pressBall) ball.position else player.baseFormationSlot
                    moveTowards(dt, player, target, isSprint = pressBall)
                }
            }
        }
    }

    private fun updateGoalkeeper(dt: Float, gk: GamePlayer, ball: GameBall) {
        val goalX = if (gk.teamSide == PlayerTeamSide.HOME) 35f else 965f
        val clampedY = (ball.position.y).coerceIn(285f, 365f)
        val target = Vector2D(goalX, clampedY)

        val dir = target - gk.position
        if (dir.length() > 5f) {
            gk.position = gk.position + (dir.normalized() * 140f * dt)
        }

        // Dive if ball approaches goal mouth
        if (ball.position.distanceTo(gk.position) < 40f && ball.velocity.length() > 200f) {
            gk.state = PlayerActionState.TACKLING
            ball.velocity = Vector2D(-ball.velocity.x * 0.4f, ball.velocity.y * 0.4f)
        }
    }

    private fun updateAiBallCarrier(
        dt: Float,
        player: GamePlayer,
        ball: GameBall,
        allPlayers: List<GamePlayer>,
        goalTarget: Vector2D,
        speedMultiplier: Float = 1.0f
    ) {
        val distToGoal = player.position.distanceTo(goalTarget)
        val shootThreshold = when (difficulty.lowercase()) {
            "easy" -> 220f
            "hard" -> 320f
            "legendary" -> 360f
            else -> 280f
        }

        // Shoot if near the box!
        if (distToGoal < shootThreshold && Random.nextInt(100) < (if (difficulty.equals("legendary", true)) 12 else 6)) {
            val shotDir = (goalTarget - player.position).normalized()
            val shotPower = 380f + (player.shooting * 2.2f)
            ball.kick(shotDir, shotPower, loftAngle = 80f)
            player.state = PlayerActionState.SHOOTING
            return
        }

        // Check if user defender is closing down
        val closestOpponent = allPlayers
            .filter { it.teamSide != player.teamSide }
            .minByOrNull { it.position.distanceTo(player.position) }

        if (closestOpponent != null && closestOpponent.position.distanceTo(player.position) < 65f) {
            // Find teammate to pass to
            val openTeammate = allPlayers
                .filter { it.teamSide == player.teamSide && it.id != player.id && it.positionLabel != "GK" }
                .minByOrNull { it.position.distanceTo(goalTarget) }

            if (openTeammate != null && Random.nextInt(100) < 60) {
                val passDir = (openTeammate.position - player.position).normalized()
                ball.kick(passDir, 280f)
                player.state = PlayerActionState.PASSING
                return
            }
        }

        // Otherwise dribble towards goal
        moveTowards(dt, player, goalTarget, isSprint = true, speedMultiplier = speedMultiplier)
    }

    private fun updateAiDefending(
        dt: Float,
        player: GamePlayer,
        ball: GameBall,
        allPlayers: List<GamePlayer>,
        speedMultiplier: Float = 1.0f
    ) {
        val distToBall = player.position.distanceTo(ball.position)

        // If player is closest to ball, press and tackle!
        val isClosestToBall = allPlayers
            .filter { it.teamSide == player.teamSide && it.positionLabel != "GK" }
            .minByOrNull { it.position.distanceTo(ball.position) }?.id == player.id

        val pressThreshold = when (difficulty.lowercase()) {
            "easy" -> 130f
            "hard" -> 210f
            "legendary" -> 250f
            else -> 180f
        }

        if (isClosestToBall && distToBall < pressThreshold) {
            moveTowards(dt, player, ball.position, isSprint = true, speedMultiplier = speedMultiplier)

            if (distToBall < 30f && player.state != PlayerActionState.TACKLING) {
                val tackleChance = if (difficulty.equals("legendary", true)) 85 else if (difficulty.equals("easy", true)) 30 else 50
                if (Random.nextInt(100) < tackleChance) {
                    player.state = if (Random.nextBoolean()) PlayerActionState.TACKLING else PlayerActionState.SLIDING
                    player.actionTimer = 0.4f
                }
            }
        } else {
            // Fall back towards base formation
            moveTowards(dt, player, player.baseFormationSlot, isSprint = false, speedMultiplier = speedMultiplier)
        }
    }

    private fun moveTowards(
        dt: Float,
        player: GamePlayer,
        target: Vector2D,
        isSprint: Boolean,
        speedMultiplier: Float = 1.0f
    ) {
        val dir = target - player.position
        val dist = dir.length()

        if (dist > 8f) {
            val normDir = dir.normalized()
            player.facingDirection = normDir
            val baseSpeed = if (isSprint) player.speed * 1.25f else player.speed * 0.85f
            val moveSpeed = baseSpeed * speedMultiplier
            player.velocity = normDir * moveSpeed
            player.position = player.position + (player.velocity * dt)
            player.state = if (isSprint) PlayerActionState.SPRINTING else PlayerActionState.RUNNING
        } else {
            player.velocity = Vector2D.ZERO
            player.state = PlayerActionState.IDLE
        }
    }
}
