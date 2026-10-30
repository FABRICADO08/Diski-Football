package com.example.game.engine

import com.example.game.model.GameBall
import com.example.game.model.GamePlayer
import com.example.game.model.MatchGameState
import com.example.game.model.PlayerActionState
import com.example.game.model.PlayerTeamSide
import com.example.game.model.Vector2D
import kotlin.random.Random

class GameAiController {

    fun updateAi(
        dt: Float,
        players: List<GamePlayer>,
        ball: GameBall,
        gameState: MatchGameState,
        userTeamSide: PlayerTeamSide = PlayerTeamSide.HOME
    ) {
        val homeGoalTarget = Vector2D(985f, 325f)
        val awayGoalTarget = Vector2D(15f, 325f)

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
                    updateAiBallCarrier(dt, player, ball, players, awayGoalTarget)
                } else if (gameState.possessionTeam == player.teamSide) {
                    // AI team is attacking, make supporting runs
                    val target = player.baseFormationSlot + Vector2D(-50f + (ball.position.x * 0.1f), 0f)
                    moveTowards(dt, player, target, isSprint = false)
                } else {
                    // AI team is defending: Press or Mark
                    updateAiDefending(dt, player, ball, players)
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
        goalTarget: Vector2D
    ) {
        val distToGoal = player.position.distanceTo(goalTarget)

        // Shoot if near the box!
        if (distToGoal < 280f && Random.nextInt(100) < 6) {
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
        moveTowards(dt, player, goalTarget, isSprint = true)
    }

    private fun updateAiDefending(
        dt: Float,
        player: GamePlayer,
        ball: GameBall,
        allPlayers: List<GamePlayer>
    ) {
        val distToBall = player.position.distanceTo(ball.position)

        // If player is closest to ball, press and tackle!
        val isClosestToBall = allPlayers
            .filter { it.teamSide == player.teamSide && it.positionLabel != "GK" }
            .minByOrNull { it.position.distanceTo(ball.position) }?.id == player.id

        if (isClosestToBall && distToBall < 180f) {
            moveTowards(dt, player, ball.position, isSprint = true)

            if (distToBall < 30f && player.state != PlayerActionState.TACKLING) {
                player.state = if (Random.nextBoolean()) PlayerActionState.TACKLING else PlayerActionState.SLIDING
                player.actionTimer = 0.4f
            }
        } else {
            // Fall back towards base formation
            moveTowards(dt, player, player.baseFormationSlot, isSprint = false)
        }
    }

    private fun moveTowards(dt: Float, player: GamePlayer, target: Vector2D, isSprint: Boolean) {
        val dir = target - player.position
        val dist = dir.length()

        if (dist > 8f) {
            val normDir = dir.normalized()
            player.facingDirection = normDir
            val moveSpeed = if (isSprint) player.speed * 1.25f else player.speed * 0.85f
            player.velocity = normDir * moveSpeed
            player.position = player.position + (player.velocity * dt)
            player.state = if (isSprint) PlayerActionState.SPRINTING else PlayerActionState.RUNNING
        } else {
            player.velocity = Vector2D.ZERO
            player.state = PlayerActionState.IDLE
        }
    }
}
