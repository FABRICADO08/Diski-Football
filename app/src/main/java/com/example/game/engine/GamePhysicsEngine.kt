package com.example.game.engine

import com.example.game.model.GameBall
import com.example.game.model.GameMatchPhase
import com.example.game.model.GamePlayer
import com.example.game.model.MatchGameState
import com.example.game.model.PlayerActionState
import com.example.game.model.PlayerTeamSide
import com.example.game.model.Vector2D
import kotlin.math.abs

class GamePhysicsEngine {

    val pitchWidth = 1000f
    val pitchHeight = 650f
    val goalTopY = 275f
    val goalBottomY = 375f
    val gravity = 850f
    val grassFriction = 0.94f
    val airResistance = 0.985f

    fun update(
        dt: Float,
        ball: GameBall,
        players: List<GamePlayer>,
        gameState: MatchGameState,
        onPostHit: (() -> Unit)? = null,
        onGoalScored: (PlayerTeamSide, scorerName: String) -> Unit
    ) {
        if (gameState.phase != GameMatchPhase.PLAYING) return

        // 1. Ball Movement & Gravity
        if (ball.ownerPlayerId == null) {
            // Free ball physics
            ball.position = ball.position + (ball.velocity * dt)

            if (ball.isAirborne) {
                ball.heightZ += ball.velocityZ * dt
                ball.velocityZ -= gravity * dt
                ball.velocity = ball.velocity * airResistance

                if (ball.heightZ <= 0f) {
                    ball.heightZ = 0f
                    // Bounce with restitution
                    ball.velocityZ = -ball.velocityZ * 0.55f
                    if (abs(ball.velocityZ) < 40f) {
                        ball.velocityZ = 0f
                    }
                }
            } else {
                // Ground friction
                ball.velocity = ball.velocity * grassFriction
                if (ball.velocity.length() < 5f) {
                    ball.velocity = Vector2D.ZERO
                }
            }
        } else {
            // Ball is controlled by a dribbler
            val owner = players.find { it.id == ball.ownerPlayerId }
            if (owner != null) {
                val dribbleOffset = owner.facingDirection.normalized() * 18f
                ball.position = owner.position + dribbleOffset
                ball.heightZ = 0f
                ball.velocity = owner.velocity
                ball.lastTouchTeam = owner.teamSide
            } else {
                ball.ownerPlayerId = null
            }
        }

        // 2. Goal Detection
        checkGoal(ball, players, gameState, onPostHit, onGoalScored)

        // 3. Pitch Boundary Collisions (Out of bounds rebound / clamp)
        clampToPitch(ball)

        // 4. Player Interceptions & Ball Pickup
        if (ball.ownerPlayerId == null && ball.heightZ < 25f) {
            for (player in players) {
                val dist = player.position.distanceTo(ball.position)
                if (dist < 24f && ball.velocity.length() < 320f) {
                    ball.ownerPlayerId = player.id
                    ball.lastTouchTeam = player.teamSide
                    gameState.possessionTeam = player.teamSide
                    break
                }
            }
        }

        // 5. Tackles Collision
        for (player in players) {
            if (player.state == PlayerActionState.TACKLING || player.state == PlayerActionState.SLIDING) {
                val ballOwner = players.find { it.id == ball.ownerPlayerId }
                if (ballOwner != null && ballOwner.teamSide != player.teamSide) {
                    val distToOwner = player.position.distanceTo(ballOwner.position)
                    if (distToOwner < 36f) {
                        // Successful dispossess!
                        ball.ownerPlayerId = null
                        val tackleDirection = (player.facingDirection + Vector2D((0..10).random() - 5f, (0..10).random() - 5f)).normalized()
                        ball.velocity = tackleDirection * 240f
                        ball.lastTouchTeam = player.teamSide
                        gameState.possessionTeam = player.teamSide
                        gameState.commentaryText = "Sharp tackle by ${player.name} to win possession!"
                        break
                    }
                }
            }
        }
    }

    private fun checkGoal(
        ball: GameBall,
        players: List<GamePlayer>,
        gameState: MatchGameState,
        onPostHit: (() -> Unit)?,
        onGoalScored: (PlayerTeamSide, scorerName: String) -> Unit
    ) {
        // Post / Crossbar check on Right Goal
        if (ball.position.x in 990f..1005f) {
            val distToTopPost = abs(ball.position.y - goalTopY)
            val distToBottomPost = abs(ball.position.y - goalBottomY)
            if ((distToTopPost < 14f || distToBottomPost < 14f) && ball.velocity.length() > 180f) {
                ball.velocity = Vector2D(-abs(ball.velocity.x) * 0.7f, ball.velocity.y * 0.7f)
                gameState.commentaryText = "OFF THE WOODWORK! Struck against the goal frame!"
                onPostHit?.invoke()
                return
            }
        }
        // Post / Crossbar check on Left Goal
        if (ball.position.x in -5f..10f) {
            val distToTopPost = abs(ball.position.y - goalTopY)
            val distToBottomPost = abs(ball.position.y - goalBottomY)
            if ((distToTopPost < 14f || distToBottomPost < 14f) && ball.velocity.length() > 180f) {
                ball.velocity = Vector2D(abs(ball.velocity.x) * 0.7f, ball.velocity.y * 0.7f)
                gameState.commentaryText = "DENIED BY THE POST! What a dramatic strike!"
                onPostHit?.invoke()
                return
            }
        }

        // Left Goal (Home defending, Away scores if in)
        if (ball.position.x <= 8f && ball.position.y in goalTopY..goalBottomY) {
            val scorer = players.find { it.id == ball.ownerPlayerId }?.name ?: "Striker"
            onGoalScored(PlayerTeamSide.AWAY, scorer)
            ball.velocity = Vector2D(20f, 0f)
            ball.position = Vector2D(5f, ball.position.y)
        }

        // Right Goal (Away defending, Home scores if in)
        if (ball.position.x >= 992f && ball.position.y in goalTopY..goalBottomY) {
            val scorer = players.find { it.id == ball.ownerPlayerId }?.name ?: "Striker"
            onGoalScored(PlayerTeamSide.HOME, scorer)
            ball.velocity = Vector2D(-20f, 0f)
            ball.position = Vector2D(995f, ball.position.y)
        }
    }

    private fun clampToPitch(ball: GameBall) {
        // Rebound softly off side touchlines
        if (ball.position.y < 12f) {
            ball.position = Vector2D(ball.position.x, 12f)
            ball.velocity = Vector2D(ball.velocity.x, abs(ball.velocity.y) * 0.5f)
        } else if (ball.position.y > pitchHeight - 12f) {
            ball.position = Vector2D(ball.position.x, pitchHeight - 12f)
            ball.velocity = Vector2D(ball.velocity.x, -abs(ball.velocity.y) * 0.5f)
        }

        // Clamp goal lines outside of net
        if (ball.position.x < 10f && (ball.position.y < goalTopY || ball.position.y > goalBottomY)) {
            ball.position = Vector2D(10f, ball.position.y)
            ball.velocity = Vector2D(abs(ball.velocity.x) * 0.5f, ball.velocity.y)
        } else if (ball.position.x > pitchWidth - 10f && (ball.position.y < goalTopY || ball.position.y > goalBottomY)) {
            ball.position = Vector2D(pitchWidth - 10f, ball.position.y)
            ball.velocity = Vector2D(-abs(ball.velocity.x) * 0.5f, ball.velocity.y)
        }
    }
}
